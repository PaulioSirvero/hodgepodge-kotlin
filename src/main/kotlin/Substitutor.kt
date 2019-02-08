import java.util.*

/** Substitutes parameters within a string  */
@FunctionalInterface
interface Substitutor {

  /**
   * Substitutes all templates within a stencil string
   *
   * @param[stencil] String containing templates to be substituted
   */
  fun format(stencil: String): AnyResult<String>

    companion object {

    /**
     * Returns a new substitutor that works with Bash style templates
     *
     * @param[variableSource] Source for values of variables
     */
    fun bashStyle(variableSource: VariableSource): Substitutor
      = BashStyleSubstitutor(variableSource)
  }
}

/** Returns the value of variables by there name or group and index  */
interface VariableSource {

  /**
   * Returns the variable value of the supplied variable name
   *
   * @param[name] Name of the variable to get
   */
  fun valueOf(name: String): Optional<String>

  /**
   * Returns the variable value at a specific index within
   * a specific group (Control file)
   *
   * @param[group] Group containing the variable to get
   * @param[index] Index of the variable value within its group
   */
  fun valueOf(group: String, index: Int): Optional<String>
}

/**
 * Represents a template bounds within a stencil string
 *
 * @property[start] Index of the first char in the template
 * @property[last] Index of the last char in the template
 */
private sealed class Template(
  val start: Int,
  val last: Int
) {

  init {
    start.assertNotNegative("Start index is negative")
    last.assertNotNegative("Last index is negative")
    start.assertLessThan(last, "Start index is NOT less than the last index")
  }
}

/**
 * Represents a groupless template within a stencil string
 *
 * @property[name] Name of the property
 */
private class GrouplessTemplate(
  start: Int,
  last: Int,
  val name: String
): Template(start, last) {

  init {
    name.assertNotEmpty("Variable name is empty")
  }
}

/**
 * Represents a template within a stencil string
 *
 * @param[group] Name of the group found
 * @param[index] Index of the value within its group
 */
private class GroupTemplate(
  start: Int,
  last: Int,
  val group: String,
  val index: Int
): Template(start, last)  {

  init {
    group.assertNotEmpty("Group name is empty")
    index.assertNotNegative("Index is negative")
  }
}

/**
 * Bash style implementation of [Substitutor]
 *
 * Searches the stencil for '${group:index}' and '${variable}'
 * templates first then replaces each with the value returned from
 * the variable source
 *
 * Because template replacements could contain another template
 * the above process is repeated until no templates exist within
 * the stencil after replacement
 *
 * @param[variables] Source for variables of templates
 */
private class BashStyleSubstitutor(
  private val variables: VariableSource
) : Substitutor {

  /** {@inheritDoc}  */
  override fun format(stencil: String): AnyResult<String> = try {
    AnyResult.good(formatInternal(stencil))
  } catch (ex: Exception) {
    AnyResult.bad(ex.message!!, ex)
  }

  /** Internal version of the format function */
  private val formatInternal: (String) -> String = { stencil ->

    var result = stencil

    do {
      val grouped = findGroupTemplates(result)
      val groupless = findGrouplessTemplates(result)

      if (grouped.isEmpty() and groupless.isEmpty()) break

      if (grouped.isNotEmpty()) result = replaceGroupTemplates(grouped, result)
      if (groupless.isNotEmpty()) result = replaceGrouplessTemplates(groupless, result)

    } while (true)

    result
  }

  /**
   * Replaces all group templates within a stencil. The template list must
   * be in the order the templates appear within the stencil
   */
  private val replaceGroupTemplates: (List<GroupTemplate>, String) -> String = { templates, stencil ->

    val sb = StringBuilder(stencil)
    templates.reversed().forEach { t ->

      val opt = variables.valueOf(t.group, t.index)

      opt.ifPresentOrElse({ s ->
        sb.replace(t.start, 1 + t.last, s)
      }) {
        throw Exception(
          "Variable at index " + t.index + " of group '" + t.group + "' returned" +
            " NULL from the variable source, probably because it could not be found"
        )
      }
    }

    sb.toString()
  }

  /**
   * Replaces all groupless templates within a stencil. The template list must
   * be in the order the templates appear within the stencil
   */
  private val replaceGrouplessTemplates: (List<GrouplessTemplate>, String) -> String = { templates, stencil ->

    val sb = StringBuilder(stencil)
    templates.reversed().forEach { t ->

      val opt = variables.valueOf(t.name)

      opt.ifPresentOrElse({ s ->
        sb.replace(t.start, 1 + t.last, s)
      }) {
        throw Exception(
          "Variable '" + t.name + "' returned NULL from the variable source," +
            " probably because it could not be found"
        )
      }
    }

    sb.toString()
  }

  /**
   * Returns a list of variable templates within the
   * supplied command stencil
   */
  private val findGrouplessTemplates: (String) -> List<GrouplessTemplate> = { stencil ->
    "\\\$\\{([a-zA-Z]+)}".toRegex()
      .findAll(stencil)
      .map {
        GrouplessTemplate(
          start = it.range.start,
          last = it.range.last,
          name = it.groups[1]!!.value
        )
      }.toList()
  }

  /**
   * Returns a list of control file templates within the
   * supplied command stencil
   */
  private val findGroupTemplates: (String) -> List<GroupTemplate> = { stencil ->
    "\\$\\{([a-zA-Z]+):(\\d+)}".toRegex()
      .findAll(stencil)
      .map {
        GroupTemplate(
          start = it.range.start,
          last = it.range.last,
          group = it.groups[1]!!.value,
          index = it.groups[2]!!.value.toInt()
        )
      }.toList()
  }
}