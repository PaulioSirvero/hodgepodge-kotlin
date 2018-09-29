package paulio.hodgepodge

import java.lang.Exception


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