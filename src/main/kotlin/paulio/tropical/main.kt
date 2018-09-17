package paulio.tropical

import java.lang.Exception
import kotlin.math.max
import kotlin.math.min


fun main(args: Array<String>) {
  println(parse("21"))



}



fun parse(input: String): Int =
  Try.invoke { input.toInt() }
    .checkpoint {
      Exception("Input not parsable to an integer", it)
    }
    .test(
      { it > 0 },
      { Exception("IDs start at 1") }
    )
    .unwrap()