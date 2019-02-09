import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

/******************************************************************************
 * Returns the negated result of the function passed
 *
 * @param[f] Function that produces a boolean to negate
 *****************************************************************************/
inline fun not(f: () -> Boolean): Boolean = f().not()

/******************************************************************************
 * Returns true if the collection contains a single item
 *****************************************************************************/
fun <T> Collection<T>.isSingleton(): Boolean = size == 1

/******************************************************************************
 * Returns the value wrapped in an Optional
 *****************************************************************************/
fun <T> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

/******************************************************************************
 * Returns true if the trimmed string matches 'Y' or 'YES' (case insensitive)
 *****************************************************************************/
fun String.toBool(): Boolean = this.trim().matches(
  "([Yy])|([Yy][Ee][Ss])".toRegex()
)

/******************************************************************************
 * Returns the string representation of the boolean in the form 'Y' or 'N'
 *
 * @param[lowercase] True if the resultant string should be in lowercase
 *****************************************************************************/
fun Boolean.toYorN(lowercase: Boolean = false): String = when {
  lowercase && this -> "y"
  lowercase && !this -> "n"
  this -> "Y"
  else -> "N"
}

/******************************************************************************
 * Returns the string representation of the boolean in the form 'YES' or 'NO'
 *
 * @param[lowercase] True if the resultant string should be in lowercase
 *****************************************************************************/
fun Boolean.toYesOrNo(lowercase: Boolean = false): String = when {
  lowercase && this -> "yes"
  lowercase && !this -> "no"
  this -> "YES"
  else -> "NO"
}

/******************************************************************************
 * Splits a string into two on the first occurrence of the specified delimiter.
 * If no match is found the pairs first will contain the whole string and the
 * second will contain null
 *
 * @param[delim] Delimiter to split on
 *****************************************************************************/
fun String.bifurcate(delim: String): Pair<String, String?>
  = bifurcate(Regex.fromLiteral(delim))

/******************************************************************************
 * Splits a string into two on the first occurrence of the specified regex. If
 * no match is found the pairs first will contain the whole string and the
 * second will contain null
 *
 * @param[regex] Regular expression to split on
 *****************************************************************************/
fun String.bifurcate(regex: Regex): Pair<String, String?> = let {
  split(regex, 2).let { p ->
    Pair(p[0], p.getOrNull(1))
  }
}

/******************************************************************************
 * Returns the substring that matches the regular expression
 *
 * @param[regex] Regex to match
 *****************************************************************************/
fun String.substring(regex: Regex): String? = substring(regex, 0)

/******************************************************************************
 * Returns the first substring that matches the group found by the regular
 * expression or null if no match was found
 *
 * @param[regex] Regex to match
 * @param[group] Number of the group to find
 *****************************************************************************/
fun String.substring(regex: Regex, group: Int): String? {
  val m = regex.find(this)
  return m?.groups?.get(group)?.value
}

/******************************************************************************
 * Prepends a prefix string to this string but only if the prefix is not
 * already present
 *
 * @param[prefix] String to prefix
 *****************************************************************************/
fun String.prefix(prefix: String): String = when {
  this.startsWith(prefix) -> this
  else -> prefix.plus(this)
}

/******************************************************************************
 * Appends a suffix to the string only if it does not already have it
 *
 * @param[suffix] Suffix to lead with
 *****************************************************************************/
fun String.suffix(suffix: String): String = when {
  this.endsWith(suffix) -> this
  else -> this.plus(suffix)
}

/******************************************************************************
 * First removes leading and trailing whitespace the replaces line breaks
 * (including their leading and trailing whitespace) with the supplied
 * delimiter so that the string forms a single line. By default a space is
 * used as the separator
 *
 * @param[delim] Delimiter to replace with
 *****************************************************************************/
fun String.lineUp(delim: String = " "): String  = when {
  isBlank() -> ""
  else -> split("\n")
    .filter { it.isNotBlank() }
    .map { it.trim() }
    .joinToString(delim)
}

/******************************************************************************
 * Removes empty lines from a list of strings
 *****************************************************************************/
fun List<String>.stripBlanks(): List<String> = filterNot { it.isBlank() }

/******************************************************************************
 * Removes empty lines from the start and end of a list of strings
 *****************************************************************************/
fun List<String>.trimBlanks(): List<String> = trimStart().trimEnd()

/******************************************************************************
 * Removes empty lines from the start of a list of strings
 *****************************************************************************/
fun List<String>.trimStart(): List<String> = dropWhile { it.isBlank() }

/******************************************************************************
 * Removes empty lines from the end of a list of strings
 *****************************************************************************/
fun List<String>.trimEnd(): List<String> = dropLastWhile { it.isBlank() }

/******************************************************************************
 * Returns the index of the first string to match the specified regular
 * expression
 *
 * @param[regex] Regex to search with
 *****************************************************************************/
fun List<String>.indexOf(regex: Regex): Int {
  for ((i, line) in withIndex()) {
    regex.find(line) ?: continue
    return i
  }
  return -1
}

/******************************************************************************
 * Parses a hex string into a byte array
 *****************************************************************************/
fun String.hexToByteArray(): ByteArray {

  // TODO: Could be done with better performance?

  length.ifOdd {
    throw Exception("Odd length string cannot be a hex string")
  }

  return ByteArray(length / 2) { i ->
    val j = i * 2

    val first = Character.digit(this[j], 16)
    if (first == -1) {
      throw Exception("Not a valid hex character '${this[j]}'")
    }

    val second = Character.digit(this[j + 1], 16)
    if (second == -1) {
      throw Exception("Not a valid hex character '${this[j + 1]}'")
    }

    ((first shl 4) + second).toByte()
  }
}

/******************************************************************************
 * Generates a SHA-256 hash of the string
 *****************************************************************************/
fun String.sha256(): ByteArray = MessageDigest
  .getInstance("SHA-256")
  .digest(this.toByteArray())

/******************************************************************************
 * Generates a SHA-512 hash of the string
 *****************************************************************************/
fun String.sha512(): ByteArray = MessageDigest
  .getInstance("SHA-512")
  .digest(this.toByteArray())

/** Generates a SHA-256 hash of a file */
fun Path.sha256(): ByteArray {

  val buffer = ByteArray(1024 * 32)
  val stream = Files.newInputStream(this)
  val hasher = MessageDigest.getInstance("SHA-256")

  var linesRead = stream.read(buffer)
  while (linesRead != -1) {
    hasher.update(buffer)
    linesRead = stream.read(buffer)
  }

  return hasher.digest()
}

/** Generates a SHA-512 hash of a file */
private fun Path.sha512(): ByteArray {

  val buffer = ByteArray(1024 * 32)
  val stream = Files.newInputStream(this)
  val hasher = MessageDigest.getInstance("SHA-512")

  var linesRead = stream.read(buffer)
  while (linesRead != -1) {
    hasher.update(buffer)
    linesRead = stream.read(buffer)
  }

  return hasher.digest()
}

/**
 * Generates a SHA-256 hash of the file returning the
 * resultant bytes as ASCII
 */
fun Path.sha256AsAscii(): String = sha256().toString(Charsets.US_ASCII)

/**
 * Generates a SHA-512 hash of the file returning the
 * resultant bytes as ASCII
 */
fun Path.sha512AsAscii(): String = sha512().toString(Charsets.US_ASCII)

/**
 * Invokes the function if this object is null
 *
 * @param[f] Function to invoke
 */
inline fun <T> T?.ifNull(f: () -> T?): T? = this ?: f()

/**
 * Invokes the function if this object is real
 *
 * @param[f] Function to invoke
 */
inline fun <T> T?.ifNotNull(f: () -> T?): T? = when {
  this != null -> f()
  else -> null
}

/**
 * Invokes the function if this object is null returning
 * the result
 *
 * @param[f] Function to invoke
 */
inline fun <T> T?.onlyReal(f: () -> T): T = this ?: f()

/**
 * Invokes the function if this boolean is true
 *
 * @param[f] Function to invoke
 */
inline fun Boolean.ifTrue(f: () -> Boolean): Boolean = when {
  this -> f()
  else -> this
}

/**
 * Invokes the function if this boolean is false
 *
 * @param[f] Function to invoke
 */
inline fun Boolean.ifFalse(f: () -> Boolean): Boolean = when {
  this -> this
  else -> f()
}

/**
 * Invokes the first function if this boolean is true
 * else invokes the second
 *
 * @param[whenTrue] Function to invoke if true
 * @param[whenFalse] Function to invoke if false
 */
inline fun <T> Boolean.fold(
  whenTrue: () -> T,
  whenFalse: () -> T
): T = when {
  this -> whenTrue()
  else -> whenFalse()
}

/**
 * Invokes the first function if this boolean is false
 * else invokes the second
 *
 * @param[whenFalse] Function to invoke if false
 * @param[whenTrue] Function to invoke if true
 */
inline fun <T> Boolean.fold2(
  whenFalse: () -> T,
  whenTrue: () -> T
): T = when {
  this -> whenTrue()
  else -> whenFalse()
}

/**
 * Invokes the function if this [Number] is negative
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNegative(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) < 0) f(this) else this

/**
 * Invokes the function if this [Number] is not negative
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNonNegative(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) >= 0) f(this) else this

/**
 * Invokes the function if this [Number] is strictly
 * positive, as in, positive and not zero
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifPositive(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) > 0) f(this) else this

/**
 * Invokes the function if this [Number] is not positive
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNonPositive(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) <= 0) f(this) else this

/**
 * Invokes the function if this [Number] is exactly zero
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifZero(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) == 0) f(this) else this

/**
 * Invokes the function if this [Number] is not zero
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNotZero(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) != 0) f(this) else this

/**
 * Invokes the function if this [Int] is odd
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun Int.ifOdd(f: Int.() -> Int): Int
  = if(this % 2 != 0) f(this) else this

/**
 * Invokes the function if this [Long] is odd
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun Long.ifOdd(f: Long.() -> Long): Long
  = if(this % 2 != 0L) f(this) else this


/**
 * Invokes the function if this [Int] is even
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun Int.ifEven(f: Int.() -> Int): Int
  = if(this % 2 == 0) f(this) else this

/**
 * Invokes the function if this [Long] is even
 *
 * @param[f] Function to invoke
 */
@Suppress("UNCHECKED_CAST")
inline fun Long.ifEven(f: Long.() -> Long): Long
  = if(this % 2 == 0L) f(this) else this


/*
TODO: Redo loading SQL resources (BELOW)
 */

/** Returns true if the SQL line is a comment */
fun String.isSqlLineComment(): Boolean = startsWith("\\s--")

/**
 * Filters SQL line comments from a list of SQL lines
 *
 * @param[fileName] Name of the resource file
 * @param[T] Class to relatively load the resource from
 */
inline fun <reified T> loadSqlResource(fileName: String): String {
  return T::class.java.getResource(fileName)
    .openStream()
    .bufferedReader(Charsets.UTF_8)
    .use {
      it.lineSequence()
        .map { it.trim() }
        .filterNot { it.isSqlLineComment() }
        .joinToString(" ")
    }
}

/*
TODO: Redo loading SQL resources (ABOVE)
 */

/**
 * Creates a time sensitive greeting
 *
 * @param[msg] Custom part of the message
 */
fun createGreeting(msg: String = ""): String {
  val now = OffsetDateTime.now(ZoneOffset.UTC)
  val local = now.toLocalTime()

  val body = when {
    msg.isBlank() -> ""
    else -> ", $msg"
  }

  return when {
    local.isAfter(LocalTime.MIDNIGHT) && local.isBefore(LocalTime.NOON) -> "Good morning (UTC)$body"
    local.isBefore(LocalTime.of(18, 0)) -> "Good afternoon (UTC)$body"
    else -> "Good evening (UTC)$body"
  }
}