package paulio.tropical

import java.lang.Exception
import java.util.*

sealed class Try<out R> {

  companion object {

    fun <R> ok(value: R): Try<R> = Ok(value)

    fun <R> err(error: Throwable): Try<R> = Err(error)

    fun <R> invoke(f: () -> R): Try<R> = try {
      Ok(f.invoke())
    } catch (ex: Throwable) {
      Err(ex)
    }
  }

  abstract fun isOk(): Boolean
  abstract fun isErr(): Boolean
  abstract fun unwrap(exceptional: (Throwable) -> Throwable
                      = { Exception("Can't unwrap value from an erroneous result", it) }): R
  abstract fun unwrapErr(): Throwable
  abstract fun <T> map(mapper: (R) -> T): Try<T>
  abstract fun <T> mapNullable(mapper: (R) -> T?): NullTry<T?>
  abstract fun test(tester: (R) -> Throwable?): Try<R>
  abstract fun test(condition: (R) -> Boolean, exceptional: (R) -> Throwable): Try<R>
  abstract fun testPoint(tester: (R) -> Throwable?): Try<R>
  abstract fun testPoint(condition: (R) -> Boolean, exceptional: (R) -> Throwable): Try<R>
  abstract fun checkpoint(exceptional: (Throwable) -> Throwable
                          = { Exception("Failed to pass checkpoint", it) }): Try<R>
}

private data class Ok<out R>(val value: R) : Try<R>() {
  override fun isOk(): Boolean = true
  override fun isErr(): Boolean = false
  override fun unwrap(exceptional: (Throwable) -> Throwable): R = value
  override fun unwrapErr(): Throwable
    = throw Exception("Can't unwrap error from an OK result")
  override fun <T> map(mapper: (R) -> T): Try<T>
    = Try.invoke { mapper.invoke(value) }
  override fun <T> mapNullable(mapper: (R) -> T?): NullTry<T?>
    = NullTry.invoke { mapper.invoke(value) }
  override fun test(tester: (R) -> Throwable?): Try<R>
    = tester.invoke(value)?.let { Err<R>(it) } ?: this
  override fun test(condition: (R) -> Boolean, exceptional: (R) -> Throwable): Try<R>
    = if(condition.invoke(value)) this else Err(exceptional.invoke(value))
  override fun testPoint(tester: (R) -> Throwable?): Try<R>
    = tester.invoke(value)?.let { throw it } ?: this
  override fun testPoint(condition: (R) -> Boolean, exceptional: (R) -> Throwable): Try<R>
    = if(condition.invoke(value)) this else throw exceptional.invoke(value)
  override fun checkpoint(exceptional: (Throwable) -> Throwable): Try<R> = this
}

private data class Err<out R>(val error: Throwable) : Try<R>() {
  override fun isOk(): Boolean = false
  override fun isErr(): Boolean = true
  override fun unwrap(exceptional: (Throwable) -> Throwable): R
    = throw exceptional.invoke(error)
  override fun unwrapErr(): Throwable = error
  override fun <T> map(mapper: (R) -> T): Try<T> = err(error)
  override fun <T> mapNullable(mapper: (R) -> T?): NullTry<T?> = NullTry.err(error)
  override fun test(tester: (R) -> Throwable?): Try<R> = this
  override fun test(condition: (R) -> Boolean, exceptional: (R) -> Throwable): Try<R> = this
  override fun testPoint(tester: (R) -> Throwable?): Try<R>
    = throw Exception("Can't invoke test function on an erroneous result, call checkpoint({...}) first")
  override fun testPoint(condition: (R) -> Boolean, exceptional: (R) -> Throwable): Try<R>
    = throw Exception("Can't invoke test function on an erroneous result, call checkpoint({...}) first")
  override fun checkpoint(exceptional: (Throwable) -> Throwable): Try<R>
    = throw exceptional.invoke(error)
}

inline fun <R> Try<R>.unwrapOr(mapper: () -> R): R = when (this) {
  is Ok -> unwrap()
  is Err -> mapper.invoke()
}

fun <R> Try<R>.nullable(): R? = when (this) {
  is Ok -> unwrap()
  is Err -> null
}

fun <R> Try<R>.toOptional(): Optional<R> = when (this) {
  is Ok -> Optional.of(unwrap())
  is Err -> Optional.empty()
}

sealed class NullTry<out R> {

  companion object {

    fun <R> ok(value: R?): NullTry<R?> = NullOk(value)

    fun <R> err(error: Throwable): NullTry<R?> = NullErr(error)

    fun <R> invoke(f: () -> R?): NullTry<R?> = try {
      NullOk(f.invoke())
    } catch (ex: Throwable) {
      NullErr(ex)
    }
  }

  abstract fun isOk(): Boolean
  abstract fun isErr(): Boolean
  abstract fun isNull(): Boolean
  abstract fun unwrap(exceptional: (Throwable) -> Throwable
                      = { Exception("Can't unwrap value from an erroneous result", it) }): R?
  abstract fun unwrapErr(): Throwable
  abstract fun <T> map(mapper: (R?) -> T?): NullTry<T?>
  abstract fun <T> mapNotNull(mapper: (R?) -> T): Try<T>
  abstract fun test(tester: (R?) -> Throwable?): NullTry<R?>
  abstract fun test(condition: (R?) -> Boolean, exceptional: (R?) -> Throwable): NullTry<R?>
  abstract fun testPoint(tester: (R?) -> Throwable?): NullTry<R?>
  abstract fun testPoint(condition: (R?) -> Boolean, exceptional: (R?) -> Throwable): NullTry<R?>
  abstract fun checkpoint(exceptional: (Throwable) -> Throwable
                          = { Exception("Failed to pass checkpoint", it) }): NullTry<R?>
}

private data class NullOk<out R>(val value: R?) : NullTry<R?>() {
  override fun isOk(): Boolean = true
  override fun isErr(): Boolean = false
  override fun isNull(): Boolean = value == null
  override fun unwrap(exceptional: (Throwable) -> Throwable): R? = value
  override fun unwrapErr(): Throwable = throw Exception("Can't unwrap error from an OK result")
  override fun <T> map(mapper: (R?) -> T?): NullTry<T?> = NullTry.invoke { mapper.invoke(value) }
  override fun <T> mapNotNull(mapper: (R?) -> T): Try<T> = Try.invoke { mapper.invoke(value) }
  override fun test(tester: (R?) -> Throwable?): NullTry<R?>
    = tester.invoke(value)?.let { NullErr<R?>(it) } ?: this
  override fun test(condition: (R?) -> Boolean, exceptional: (R?) -> Throwable): NullTry<R?>
    = if(condition.invoke(value)) this else NullErr(exceptional.invoke(value))
  override fun testPoint(tester: (R?) -> Throwable?): NullTry<R?>
    = tester.invoke(value)?.let { throw it } ?: this
  override fun testPoint(condition: (R?) -> Boolean, exceptional: (R?) -> Throwable): NullTry<R?>
    = if(condition.invoke(value)) this else throw exceptional.invoke(value)
  override fun checkpoint(exceptional: (Throwable) -> Throwable): NullTry<R?> = this
}

private data class NullErr<out R>(val error: Throwable) : NullTry<R?>() {
  override fun isOk(): Boolean = false
  override fun isErr(): Boolean = true
  override fun isNull(): Boolean = true
  override fun unwrap(exceptional: (Throwable) -> Throwable): R?
    = throw exceptional.invoke(error)
  override fun unwrapErr(): Throwable = error
  override fun <T> map(mapper: (R?) -> T?): NullTry<T?> = err(error)
  override fun <T> mapNotNull(mapper: (R?) -> T): Try<T> = Try.err(error)
  override fun test(tester: (R?) -> Throwable?): NullTry<R?> = this
  override fun test(condition: (R?) -> Boolean, exceptional: (R?) -> Throwable): NullTry<R?> = this
  override fun testPoint(tester: (R?) -> Throwable?): NullTry<R?>
    = throw Exception("Can't run test function on an erroneous result, call checkpoint({...}) first")
  override fun testPoint(condition: (R?) -> Boolean, exceptional: (R?) -> Throwable): NullTry<R?>
    = throw Exception("Can't run test function on an erroneous result, call checkpoint({...}) first")
  override fun checkpoint(exceptional: (Throwable) -> Throwable): NullTry<R?>
    = throw exceptional.invoke(error)
}

inline fun <R> NullTry<R?>.unwrapOr(mapper: () -> R?): R? = when (this) {
  is NullOk -> unwrap()
  is NullErr -> mapper.invoke()
}

fun <R> NullTry<R?>.toOptional(): Optional<R> = when (this) {
  is NullOk -> Optional.ofNullable(unwrap())
  is NullErr -> Optional.empty()
}