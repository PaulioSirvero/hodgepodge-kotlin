import AnyResult.Companion.bad
import java.lang.Exception

/******************************************************************************
 * Substitutes variable declarations within template strings (stencils)
 *
 * @property[regex] Regular expression used to find variable declarations
 * within strings
 * @property[variables] Function that accepts variable names and returns the
 * associated value
 *****************************************************************************/
class Substitutor2(val regex: Regex, val variables: (String) -> String?) {

  /****************************************************************************
   * Populates the variables within the supplied stencil string throwing an
   * exception if any problems occur
   *
   * @param[stencil] The string containing placeholder variables to populate
   ***************************************************************************/
  fun stamp(stencil: String): String {

    var result = stencil

    while(true) {
      val match = regex.find(result)
      match ?: break

      if (match.groups.size < 2) throw Exception(
        "Regular expression must contain an explicit group '$regex'"
      )

      val key = match.groups[1]!!.value
      val value = variables(key) ?: throw Exception(
        "Could not find value for variable named '${match.value}'"
      )

      result = result.replaceRange(
        match.range,
        value
      )
    }

    return result
  }

  /****************************************************************************
   * Populates the variables within the supplied stencil string returning an
   * error result instead of throwing an exception
   *
   * @param[stencil] The string containing placeholder variables to populate
   ***************************************************************************/
  fun safeStamp(stencil: String): AnyResult<String> {

    var result = stencil

    while(true) {
      val match = regex.find(result)
      match ?: break

      if (match.groups.size < 2) return bad(
        "Regular expression must contain an explicit group '$regex'"
      )

      val key = match.groups[1]!!.value
      val value = variables(key) ?: return bad(
        "Could not find value for variable named '${match.value}'"
      )

      result = result.replaceRange(
        match.range,
        value
      )
    }

    return result.realResult()
  }

  companion object {

    /****************************************************************************
     * Creates a new [Substitutor2] using a bash style '${...}' regular
     * expression to identify variables
     *
     * @param[variables] Function that accepts variable names and returns the
     * associated value
     ***************************************************************************/
    fun bashStyle(variables: (String) -> String?)
      = Substitutor2("\\\$\\{([a-zA-Z0-9_]+)}".toRegex(), variables)


  }
}