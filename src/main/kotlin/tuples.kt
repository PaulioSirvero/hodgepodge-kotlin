
import kotlin.random.Random

/******************************************************************************
 * Swaps the first and second values around
 *****************************************************************************/
fun <A, B> Pair<A, B>.swap(): Pair<B, A> = second to first

/******************************************************************************
 * Randomly shuffles the members
 *****************************************************************************/
fun <T> Pair<T, T>.shuffle(): Pair<T, T>
  = if(Random.nextBoolean()) this else swap()

/******************************************************************************
 * Returns true if the supplied value is contained within the pair
 *
 * @param[value] Value to check for
 *****************************************************************************/
operator fun <T> Pair<T, T>.contains(value: T): Boolean
  = (value == first) or (value == second)

/******************************************************************************
 * Maps a pair into another pair
 *
 * @param[f] Transformation function
 *****************************************************************************/
inline fun <A, B, A2, B2> Pair<A, B>.map(
  f: (Pair<A, B>) -> Pair<A2, B2>
): Pair<A2, B2> = f(this)

/******************************************************************************
 * Appends another value to the pair to form a [Triple]
 *
 * @param[item] Value to insert at the end
 *****************************************************************************/
infix operator fun <A, B, C> Pair<A, B>.plus(
  item: C
): Triple<A, B, C> = Triple(first, second, item)

/******************************************************************************
 * Appends another value to the pair to form a [Triple]
 *
 * @param[f] Supplier function
 *****************************************************************************/
inline operator fun <A, B, C> Pair<A, B>.plus(
  f: Pair<A, B>.() -> C
): Triple<A, B, C> = Triple(first, second, f(this))

/******************************************************************************
 * Transforms a pair into a [Triple] by inserting a value into the center
 *
 * @param[f] Supplier function
 *****************************************************************************/
fun <A, B, C> Pair<A, C>.midsert(
  f: Pair<A, C>.() -> B
): Triple<A, B, C> = Triple(first, f(this), second)

/******************************************************************************
 * Transforms a pair into a [Triple] by inserting a value into the center
 *
 * @param[item] Value to insert in the middle
 *****************************************************************************/
fun <A, B, C> Pair<A, C>.midsert(
  item: B
): Triple<A, B, C> = Triple(first, item, second)

/******************************************************************************
 * Transforms a pair into a [Triple] by prepending a value
 *
 * @param[item] Value to insert at the front
 *****************************************************************************/
fun <A, B, C> Pair<B, C>.insert(
  item: A
): Triple<A, B, C> = Triple(item, first, second)

/******************************************************************************
 * Transforms a pair into a [Triple] by prepending a value
 *
 * @param[f] Supplier function
 *****************************************************************************/
fun <A, B, C> Pair<B, C>.insert(
  f: Pair<B, C>.() -> A
): Triple<A, B, C> = Triple(f(), first, second)

/******************************************************************************
 * Returns a value from the pair by index
 *
 * @param[index] Index of the value to get
 *****************************************************************************/
infix operator fun <T> Pair<T, T>.get(
  index: Int
): T = when (index) {
  0 -> first
  1 -> second
  else -> throw IndexOutOfBoundsException(
    "Pairs have a size of two, only 0 or 1 may be used as indexes"
  )
}

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