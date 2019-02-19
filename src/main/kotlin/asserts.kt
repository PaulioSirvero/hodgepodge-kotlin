
/******************************************************************************
 * Thrown when an assertion fails
 *
 * @param[errorMessage] Message to initialise any error messages with
 * @param[cause] Root cause of the failed assertion if appropriate
 *****************************************************************************/
class FailedAssert(
  errorMessage: String,
  cause: Throwable? = null
): Exception(errorMessage, cause)

/******************************************************************************
 * Asserts the value is not null throwing an exception if it is. Always returns
 * the non-nullable version of the type
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <T> T?.assertNotNull(errorMessage: String): T = when {
  this == null -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the string is not empty throwing an exception if it is. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.assertNotEmpty(errorMessage: String): String = when {
  isEmpty() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the string is not blank throwing an exception if it is. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.assertNotBlank(errorMessage: String): String = when {
  isBlank() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the string is of a specified length throwing an exception if it is
 * not. Always returns the value unmodified
 *
 * @param[length] Length to assert
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.assertLength(length: Int, errorMessage: String): String = when {
  this.length != length -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the string matches the regular expression throwing an exception if
 * not. Always returns the value unmodified
 *
 * @param[regex] Regex to match
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.assertMatches(regex: Regex, errorMessage: String): String = when {
  matches(regex) -> this
  else -> throw FailedAssert(errorMessage)
}

/******************************************************************************
 * Asserts the collection is not empty throwing an exception if it is. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <C, V> C.assertNotEmpty(errorMessage: String): C
  where C: Collection<V> = when {
  this.isEmpty() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the collection is of a specified size throwing an exception if it is
 * not. Always returns the value unmodified
 *
 * @param[length] Length to assert
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <C, V> C.assertLength(length: Int, errorMessage: String): C
  where C: Collection<V> = when {
  this.size != length -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the collection does NOT contain any duplicate entries throwing an
 * exception if it is. Always returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <C, V> C.assertNoDuplicates(errorMessage: String): C
  where C: Collection<V> {
  for ((outerIndex, outer) in this.withIndex()) {
    this.forEachIndexed { innerIndex, inner ->
      when {
        innerIndex == outerIndex -> return@forEachIndexed
        outer == null && inner == null -> throw FailedAssert(errorMessage)
        outer != null && outer == inner -> throw FailedAssert(errorMessage)
      }
    }
  }
  return this
}

/******************************************************************************
 * Asserts the map is not empty throwing an exception if it is. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <K,V> Map<K,V>.assertNotEmpty(errorMessage: String): Map<K,V> = when {
  this.isEmpty() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the map is of a specified size throwing an exception if it is not.
 * Always returns the value unmodified
 *
 * @param[length] Length to assert
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <K,V> Map<K,V>.assertLength(
  length: Int,
  errorMessage: String
): Map<K,V> = when {
  this.size != length -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is not zero throwing an exception if not. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertZero(errorMessage: String): Number = when {
  this.toDouble() != 0.0 -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is not negative throwing an exception if not. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertNotNegative(errorMessage: String): Number = when {
  this.toDouble() < 0 -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is not positive throwing an exception if not. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertNotPositive(errorMessage: String): Number = when {
  this.toDouble() > 0 -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is negative throwing an exception if not. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertNegative(errorMessage: String): Number = when {
  this.toDouble() >= 0  -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is positive throwing an exception if not. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertPositive(errorMessage: String): Number = when {
  this.toDouble() <= 0 -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is less than the supplied throwing an exception if not.
 * Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertLessThan(other: Number, errorMessage: String): Number = when {
  this.toDouble() >= other.toDouble() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is less than or equal to the supplied throwing an
 * exception if not. Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertLessThanOrEqual(
  other: Number,
  errorMessage: String
): Number = when {
  this.toDouble() > other.toDouble() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is greater than the supplied throwing an exception if not.
 * Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertGreaterThan(
  other: Number,
  errorMessage: String
): Number = when {
  this.toDouble() <= other.toDouble() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is greater than or equal to the supplied throwing an
 * exception if not. Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.assertGreaterThanOrEqual(
  other: Number,
  errorMessage: String
): Number = when {
  this.toDouble() < other.toDouble() -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the integer is even throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Int.assertEven(errorMessage: String): Int = when {
  this % 2 == 0 -> this
  else -> throw FailedAssert(errorMessage)
}

/******************************************************************************
 * Asserts the integer is odd throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Int.assertOdd(errorMessage: String): Int = when {
  this % 2 == 0 -> throw FailedAssert(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the long is even throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Long.assertEven(errorMessage: String): Long = when {
  this % 2 == 0L -> this
  else -> throw FailedAssert(errorMessage)
}

/******************************************************************************
 * Asserts the long is odd throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Long.assertOdd(errorMessage: String): Long = when {
  this % 2 == 0L -> throw FailedAssert(errorMessage)
  else -> this
}
