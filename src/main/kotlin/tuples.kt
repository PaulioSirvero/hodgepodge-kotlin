
import kotlin.random.Random

/**
 * Swaps the member values around
 *
 * @return New pair
 */
fun <A, B> Pair<A, B>.swap(): Pair<B, A> = second to first

/**
 * Randomly shuffles the members
 *
 * @return New pair or this
 */
fun <T> Pair<T, T>.shuffle(): Pair<T, T>
  = if(Random.nextBoolean()) this else swap()

/**
 * Returns true if `value in pair`
 *
 * @param[value] Value to check for
 *
 * @return True if value is in the pair
 */
infix operator fun <T> Pair<T, T>.contains(value: T): Boolean
  = (value == first) or (value == second)

/**
 * Maps a pair into another pair
 *
 * @param[f] Transformation function
 *
 * @return New pair
 */
inline fun <A, B, A2, B2> Pair<A, B>.map(
  f: (Pair<A, B>) -> Pair<A2, B2>
): Pair<A2, B2> = f(this)

/**
 * Transforms a pair into a [Triple]
 *
 * @param[f] Transformation function
 *
 * @return New triple
 */
inline fun <A, B, X, Y, Z> Pair<A, B>.grow(
  f: (Pair<A, B>) -> Triple<X, Y, Z>
): Triple<X, Y, Z> = f(this)

/**
 * Transforms a pair into a [Triple] by appending a value
 *
 * @param[third] Value to grow with
 *
 * @return New triple
 */
infix fun <A, B, C> Pair<A, B>.append(
  third: C
): Triple<A, B, C> = Triple(
  first, second, third
)

/**
 * Transforms a pair into a [Triple] by appending a value
 *
 * @param[third] Value to grow with
 *
 * @return New triple
 */
infix operator fun <A, B, C> Pair<A, B>.plus(
  third: C
): Triple<A, B, C> = Triple(
  first, second, third
)

/**
 * Transforms a pair into a [Triple] by inserting a value
 * into the center
 *
 * @param[mid] Value to grow with
 *
 * @return New triple
 */
infix fun <A, B, C> Pair<A, C>.insert(
  mid: B
): Triple<A, B, C> = Triple(
  first, mid, second
)

/**
 * Transforms a pair into a [Triple] by prepending a value
 *
 * @param[prefix] Value to grow with
 *
 * @return New triple
 */
infix fun <A, B, C> Pair<B, C>.prepend(
  prefix: A
): Triple<A, B, C> = Triple(
  prefix, first, second
)

/**
 * Sets a value from the pair by index
 *
 * @param[index] Index of the value to set
 *
 * @return New pair
 */
operator fun <T> Pair<T, T>.set(
  index: Int, value: T
): Pair<T, T> = when (index) {
  0 -> value to second
  1 -> first to value
  else -> throw IndexOutOfBoundsException(
    "Pairs have fixed length of two, only 0 or 1 may be used to reference by index")
}

/**
 * Returns a value from the pair by index
 *
 * @param[index] Index of the value to get
 *
 * @return Referenced value
 */
infix operator fun <T> Pair<T, T>.get(
  index: Int
): T = when (index) {
  0 -> first
  1 -> second
  else -> throw IndexOutOfBoundsException(
    "Pairs have fixed length of two, only 0 or 1 may be used to reference by index")
}

/**
 * Joins another pair onto this one to form a [Quartet]
 *
 * @param[other] Other pair
 *
 * @return New quartet
 */
infix fun <A, B, C, D> Pair<A, B>.couple(
  other: Pair<C, D>
): Quartet<A, B, C, D> = double(other.first, other.second)

/**
 * Transforms a pair into a [Quartet]
 *
 * @param[f] Transformation function
 *
 * @return New quartet
 */
inline fun <A, B, W, X, Y, Z> Pair<A, B>.double(
  f: (Pair<A, B>) -> Quartet<W, X, Y, Z>
): Quartet<W, X, Y, Z> = f(this)

/**
 * Transforms a pair into a [Quartet] by appending two values
 *
 * @param[third] Value to grow with
 * @param[fourth] Value to grow with
 *
 * @return New quartet
 */
fun <A, B, C, D> Pair<A, B>.double(
  third: C,
  fourth: D
): Quartet<A, B, C, D> = Quartet(
  first, second, third, fourth
)

/**
 * Replaces the first value with another
 *
 * @param[f] Transformation function
 *
 * @return New pair
 */
inline fun <A, B, A2> Pair<A, B>.mapFirst(
  f: (Pair<A, B>) -> A2
): Pair<A2, B> = f(this) to second

/**
 * Replaces the second value with another
 *
 * @param[f] Transformation function
 *
 * @return New pair
 */
inline fun <A, B, B2> Pair<A, B>.mapSecond(
  f: (Pair<A, B>) -> B2
): Pair<A, B2> = first to f(this)

/**
 * Converts this pair into a singleton map
 *
 * @return Singleton map
 */
fun <A, B> Pair<A, B>.toMap(): Map<A, B> = mapOf(this)

/**
 * Joins the pair into a string with a user supplied
 * delimiter between them
 *
 * @param[delimiter] Delimiter to join with
 *
 * @return Joined values
 */
fun <A, B> Pair<A, B>.join(delimiter: String): String
  = "$first$delimiter$second"

/**
 * Reverse the members so first swaps with third
 *
 * @return New triple
 */
fun <A, B, C> Triple<A, B, C>.reverse(): Triple<C, B, A>
  = Triple(third, second, first)

/**
 * Shifts all values one member to the left
 *
 * @return New triple
 */
fun <A, B, C> Triple<A, B, C>.shiftLeft(): Triple<B, C, A>
  = Triple(second, third, first)

/**
 * Shifts all values one member to the right
 *
 * @return New triple
 */
fun <A, B, C> Triple<A, B, C>.shiftRight(): Triple<C, A, B>
  = Triple(third, first, second)

/**
 * Randomly shuffles the members
 *
 * @return New Triple
 */
fun <T> Triple<T, T, T>.shuffle(): Triple<T, T, T>
  = toList().shuffled().let {
  Triple(it[0], it[1], it[2])
}

/**
 * Returns true if `value in triple`
 *
 * @param[value] Value to check for
 *
 * @return True if value is in the triple
 */
infix operator fun <T> Triple<T, T, T>.contains(value: T): Boolean
  = (value == first) or (value == second) or (value == third)

/**
 * Maps the triple into another triple
 *
 * @param[f] Transformation function
 *
 * @return New triple
 */
inline fun <A, B, C, A2, B2, C2> Triple<A, B, C>.map(
  f: (Triple<A, B, C>) -> Triple<A2, B2, C2>
): Triple<A2, B2, C2> = f(this)

/**
 * Shrinks the triple to a [Pair] by dropping the first member
 *
 * @return New pair
 */
fun <A, B, C> Triple<A, B, C>.dropFirst(): Pair<B, C> = second to third

/**
 * Shrinks the triple to a [Pair] by dropping the middle member
 *
 * @return New pair
 */
fun <A, B, C> Triple<A, B, C>.dropSecond(): Pair<A, C> = first to third

/**
 * Shrinks the triple to a [Pair] by dropping the last member
 *
 * @return New pair
 */
fun <A, B, C> Triple<A, B, C>.dropThird(): Pair<A, B> = first to second

/**
 * Transforms the triple into a [Pair]
 *
 * @param[f] Transformation function
 *
 * @return New pair
 */
inline fun <A, B, C, X, Y> Triple<A, B, C>.shrink(
  f: (Triple<A, B, C>) -> Pair<X, Y>
): Pair<X, Y> = f(this)

/**
 * Splits the triple into a [Pair] with the
 * first value being returned as first and the
 * rest returned as a [Pair]
 *
 * @return New pair
 */
fun <A, B, C>  Triple<A, B, C>.splitFirst()
  : Pair<A, Pair<B, C>>
  = first to Pair(second, third)

/**
 * Splits the triple into a [Pair] with the
 * last value being returned as second and the
 * rest returned as a [Pair]
 *
 * @return New pair
 */
fun <A, B, C>  Triple<A, B, C>.splitLast()
  : Pair<Pair<A, B>, C>
  = Pair(first, second) to third

/**
 * Transforms the triple into a [Quartet]
 *
 * @param[f] Transformation function
 *
 * @return New quartet
 */
inline fun <A, B, C, W, X, Y, Z> Triple<A, B, C>.grow(
  f: (Triple<A, B, C>) -> Quartet<W, X, Y, Z>
): Quartet<W, X, Y, Z> = f(this)

/**
 * Transforms a triple into a [Quartet] by appending a value
 *
 * @param[fourth] Value to grow with
 *
 * @return New quartet
 */
infix fun <A, B, C, D> Triple<A, B, C>.append(
  fourth: D
): Quartet<A, B, C, D> = Quartet(
  first, second, third, fourth
)

/**
 * Transforms a triple into a [Quartet] by appending a value
 *
 * @param[fourth] Value to grow with
 *
 * @return New quartet
 */
infix operator fun <A, B, C, D> Triple<A, B, C>.plus(
  fourth: D
): Quartet<A, B, C, D> = Quartet(
  first, second, third, fourth
)

/**
 * Transforms a pair into a [Quartet] by prepending a value
 *
 * @param[prefix] Value to grow with
 *
 * @return New quartet
 */
infix fun <A, B, C, D> Triple<B, C, D>.prepend(
  prefix: A
): Quartet<A, B, C, D> = Quartet(
  prefix, first, second, third
)

/**
 * Sets a value from the triple by index
 *
 * @param[index] Index of the value to set
 *
 * @return New triple
 */
operator fun <T> Triple<T, T, T>.set(
  index: Int, value: T
): Triple<T, T, T> = when (index) {
  0 -> Triple(value, second, third)
  1 -> Triple(first, value, third)
  2 -> Triple(first, second, value)
  else -> throw IndexOutOfBoundsException(
    "Triples have fixed length of three, only 0, 1 or 2 may be used to reference by index")
}

/**
 * Returns a value from the triple by index
 *
 * @param[index] Index of the value to get
 *
 * @return Referenced value
 */
infix operator fun <T> Triple<T, T, T>.get(
  index: Int
): T = when (index) {
  0 -> first
  1 -> second
  2 -> third
  else -> throw IndexOutOfBoundsException(
    "Triples have fixed length of three, only 0, 1 or 2 may be used to reference by index")
}

/**
 * Replaces the first value with another
 *
 * @param[f] Transformation function
 *
 * @return New triple
 */
inline fun <A, B, C, A2> Triple<A, B, C>.mapFirst(
  f: (Triple<A, B, C>) -> A2
): Triple<A2, B, C> = Triple(f(this), second, third)

/**
 * Replaces the second value with another
 *
 * @param[f] Transformation function
 *
 * @return New triple
 */
inline fun <A, B, C, B2> Triple<A, B, C>.mapSecond(
  f: (Triple<A, B, C>) -> B2
): Triple<A, B2, C> = Triple(first, f(this), third)

/**
 * Replaces the third value with another
 *
 * @param[f] Transformation function
 *
 * @return New triple
 */
inline fun <A, B, C, C2> Triple<A, B, C>.mapThird(
  f: (Triple<A, B, C>) -> C2
): Triple<A, B, C2> = Triple(first, second, f(this))

/**
 * Converts this triple into a set
 *
 * @return Set of values in member order
 */
fun <T> Triple<T, T, T>.toSet(): Set<T>
  = setOf(first, second, third)

/**
 * Joins the triple into a string with a user supplied
 * delimiter between them
 *
 * @param[delimiter] Delimiter to join with
 *
 * @return Joined values
 */
fun <A, B, C> Triple<A, B, C>.join(delimiter: String): String
  = "$first$delimiter$second$delimiter$third"

/**
 * A four value version of [Pair]
 *
 * @property[first] First value
 * @property[second] Second value
 * @property[third] Third value
 * @property[fourth] Fourth value
 */
data class Quartet<out A, out B, out C, out D>(
  val first: A,
  val second: B,
  val third: C,
  val fourth: D
)

/**
 * Reverse the members so first swaps with fourth
 * and second swaps with third
 *
 * @return New quartet
 */
fun <A, B, C, D> Quartet<A, B, C, D>.reverse(): Quartet<D, C, B, A>
  = Quartet(fourth, third, second, first)

/**
 * Shifts all values one member to the left
 *
 * @return New quartet
 */
fun <A, B, C, D> Quartet<A, B, C, D>.shiftLeft(): Quartet<B, C, D, A>
  = Quartet(second, third, fourth, first)

/**
 * Shifts all values one member to the right
 *
 * @return New quartet
 */
fun <A, B, C, D> Quartet<A, B, C, D>.shiftRight(): Quartet<D, A, B, C>
  = Quartet(fourth, first, second, third)

/**
 * Randomly shuffles the members
 *
 * @return New Triple
 */
fun <T> Quartet<T, T, T, T>.shuffle(): Quartet<T, T, T, T>
  = toList().shuffled().let {
  Quartet(it[0], it[1], it[2], it[3])
}

/**
 * Returns true if `value in quartet`
 *
 * @param[value] Value to check for
 *
 * @return True if value is in the quartet
 */
infix operator fun <T> Quartet<T, T, T, T>.contains(value: T): Boolean
  = (value == first) or (value == second) or (value == third) or (value == fourth)

/**
 * Maps the quartet into another quartet
 *
 * @param[f] Transformation function
 *
 * @return New quartet
 */
inline fun <A, B, C, D, A2, B2, C2, D2>  Quartet<A, B, C, D>.map(
  f: (Quartet<A, B, C, D>) -> Quartet<A2, B2, C2, D2>
): Quartet<A2, B2, C2, D2> = f(this)

/**
 * Shrinks the quartet to a [Triple] by dropping the first member
 *
 * @return New triple
 */
fun <A, B, C, D>  Quartet<A, B, C, D>.dropFirst(): Triple<B, C, D>
  = Triple(second, third, fourth)

/**
 * Shrinks the quartet to a [Triple] by dropping the second member
 *
 * @return New triple
 */
fun <A, B, C, D>  Quartet<A, B, C, D>.dropSecond(): Triple<A, C, D>
  = Triple(first, third, fourth)

/**
 * Shrinks the quartet to a [Triple] by dropping the third member
 *
 * @return New triple
 */
fun <A, B, C, D>  Quartet<A, B, C, D>.dropThird(): Triple<A, B, D>
  = Triple(first, second, fourth)

/**
 * Shrinks the quartet to a [Triple] by dropping the fourth member
 *
 * @return New triple
 */
fun <A, B, C, D>  Quartet<A, B, C, D>.dropFourth(): Triple<A, B, C>
  = Triple(first, second, third)

/**
 * Transforms the quartet into a [Triple]
 *
 * @param[f] Transformation function
 *
 * @return New triple
 */
inline fun <A, B, C, D, X, Y, Z>  Quartet<A, B, C, D>.shrink(
  f: (Quartet<A, B, C, D>) -> Triple<X, Y, Z>
): Triple<X, Y, Z> = f(this)

/**
 * Transforms the quartet into a [Pair]
 *
 * @param[f] Transformation function
 *
 * @return New pair
 */
inline fun <A, B, C, D, X, Y>  Quartet<A, B, C, D>.halve(
  f: (Quartet<A, B, C, D>) -> Pair<X, Y>
): Pair<X, Y> = f(this)

/**
 * Splits the quartet into a [Pair] so first and
 * second are in first while third and fourth are
 * in second
 *
 * @return New pair
 */
fun <A, B, C, D>  Quartet<A, B, C, D>.split()
  : Pair<Pair<A, B>, Pair<C, D>>
  = (first to second) to (third to fourth)

/**
 * Splits the quartet into a [Pair] with the
 * first value being returned as first and the
 * rest returned as a [Triple]
 *
 * @return New pair
 */
fun <A, B, C, D>  Quartet<A, B, C, D>.splitFirst()
  : Pair<A, Triple<B, C, D>>
  = first to Triple(second, third, fourth)

/**
 * Splits the quartet into a [Pair] with the
 * last value being returned as second and the
 * rest returned as a [Triple]
 *
 * @return New pair
 */
fun <A, B, C, D>  Quartet<A, B, C, D>.splitLast()
  : Pair<Triple<A, B, C>, D>
  = Triple(first, second, third) to fourth

/**
 * Sets a value from the quartet by index
 *
 * @param[index] Index of the value to set
 *
 * @return New quartet
 */
operator fun <T> Quartet<T, T, T, T>.set(
  index: Int, value: T
): Quartet<T, T, T, T> = when (index) {
  0 -> Quartet(value, second, third, fourth)
  1 -> Quartet(first, value, third, fourth)
  2 -> Quartet(first, second, value, fourth)
  3 -> Quartet(first, second, third, value)
  else -> throw IndexOutOfBoundsException(
    "Quartets have fixed length of four, only 0, 1, 2 or 3 may be used to reference by index")
}

/**
 * Returns a value from the quartet by index
 *
 * @param[index] Index of the value to get
 *
 * @return Referenced value
 */
infix operator fun <T> Quartet<T, T, T, T>.get(
  index: Int
): T = when (index) {
  0 -> first
  1 -> second
  2 -> third
  3 -> fourth
  else -> throw IndexOutOfBoundsException(
    "Quartets have fixed length of four, only 0, 1, 2 or 3 may be used to reference by index")
}

/**
 * Replaces the first value with another
 *
 * @param[f] Transformation function
 *
 * @return New quartet
 */
inline fun <A, B, C, D, A2>  Quartet<A, B, C, D>.mapFirst(
  f: (Quartet<A, B, C, D>) -> A2
): Quartet<A2, B, C, D>
  = Quartet(f(this), second, third, fourth)

/**
 * Replaces the second value with another
 *
 * @param[f] Transformation function
 *
 * @return New quartet
 */
inline fun <A, B, C, D, B2>  Quartet<A, B, C, D>.mapSecond(
  f: (Quartet<A, B, C, D>) -> B2
): Quartet<A, B2, C, D>
  = Quartet(first, f(this), third, fourth)

/**
 * Replaces the third value with another
 *
 * @param[f] Transformation function
 *
 * @return New quartet
 */
inline fun <A, B, C, D, C2>  Quartet<A, B, C, D>.mapThird(
  f: (Quartet<A, B, C, D>) -> C2
): Quartet<A, B, C2, D>
  = Quartet(first, second, f(this), fourth)

/**
 * Replaces the fourth value with another
 *
 * @param[f] Transformation function
 *
 * @return New quartet
 */
inline fun <A, B, C, D, D2>  Quartet<A, B, C, D>.mapFourth(
  f: (Quartet<A, B, C, D>) -> D2
): Quartet<A, B, C, D2>
  = Quartet(first, second, third, f(this))

/**
 * Transforms a quartet into a [List] by appending a value
 *
 * @param[fifth] Value to grow with
 *
 * @return New list
 */
infix operator fun <T> Quartet<T, T, T, T>.plus(
  fifth: T
): List<T> = toList().plus(fifth)

/**
 * Converts this quartet into a list
 *
 * @return List of values in member order
 */
fun <T> Quartet<T, T, T, T>.toList(): List<T>
  = listOf(first, second, third, fourth)

/**
 * Converts this quartet into a set
 *
 * @return Set of values in member order
 */
fun <T> Quartet<T, T, T, T>.toSet(): Set<T>
  = setOf(first, second, third, fourth)

/**
 * Joins the quartet into a string with a user supplied
 * delimiter between them
 *
 * @param[delimiter] Delimiter to join with
 *
 * @return Joined values
 */
fun <A, B, C, D> Quartet<A, B, C, D>.join(delimiter: String): String
  = "$first$delimiter$second$delimiter$third$delimiter$fourth"