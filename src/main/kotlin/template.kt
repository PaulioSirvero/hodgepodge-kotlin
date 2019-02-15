
/******************************************************************************
 * Substitutes variable declarations within template strings (stencils)
 *
 * @property[regex] Regular expression used to find variable declarations
 * within strings
 * @property[variables] Function that accepts variable names and returns the
 * associated value
 *****************************************************************************/
class Substitutor2(val regex: Regex, val variables: (String) -> String) {

  /****************************************************************************
   * Populates the variables within the supplied stencil string throwing an
   * exception if any problems occur
   *
   * @param[stencil] The string containing placeholder variables to populate
   ***************************************************************************/
  fun stamp(stencil: String): String {
    TODO("Not yet implemented")
  }

  /****************************************************************************
   * Populates the variables within the supplied stencil string returning an
   * error result instead of throwing an exception
   *
   * @param[stencil] The string containing placeholder variables to populate
   ***************************************************************************/
  fun safeStamp(stencil: String): AnyResult<String> {
    TODO("Not yet implemented")
  }

  companion object {

    /****************************************************************************
     * Creates a new [Substitutor2] using a bash style '${...}' regular
     * expression to identify variables
     *
     * @param[variables] Function that accepts variable names and returns the
     * associated value
     ***************************************************************************/
    fun bashStyle(variables: (String) -> String)
      = Substitutor2("\\\$\\{([a-zA-Z]+)}".toRegex(), variables)


  }
}