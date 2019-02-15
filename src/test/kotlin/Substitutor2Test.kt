import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class Substitutor2Test {

  private val stampVars: (String) -> String? = {
    when (it) {
      "Weather" -> "wax"
      "Rince" -> "wind"
      else -> null
    }
  }

  @Test
  fun stamp___1() {
    """
      ...stamp___1()
      When given a stencil only containing a single variable
      Returns that value
    """.describe()

    val sub = Substitutor2.bashStyle(stampVars)
    val actual = sub.stamp("\${Weather}")
    assertEquals("wax", actual)
  }

  @Test
  fun stamp___2() {
    """
      ...stamp___2()
      When given a stencil containing two variables
      Returns a string containing mapped values for both variables
    """.describe()

    val sub = Substitutor2.bashStyle(stampVars)
    val actual = sub.stamp("\${Weather}\${Rince}")
    assertEquals("waxwind", actual)
  }

  @Test
  fun stamp___3() {
    """
      ...stamp___3()
      When given a stencil containing two variables & other text
      Returns the exact expected string
    """.describe()

    val sub = Substitutor2.bashStyle(stampVars)
    val actual = sub.stamp("Weather\${Weather} & Rince\${Rince}")
    assertEquals("Weatherwax & Rincewind", actual)
  }

  @Test
  fun stamp___4() {
    """
      ...stamp___4()
      When given an empty stencil
      Returns an empty string
    """.describe()

    val sub = Substitutor2.bashStyle(stampVars)
    val actual = sub.stamp("")
    assertEquals("", actual)
  }

  @Test
  fun stamp___5() {
    """
      ...stamp___5()
      When given a stencil not containing any variables
      Returns the input
    """.describe()

    val expected = "\n luggage \n"
    val sub = Substitutor2.bashStyle(stampVars)
    val actual = sub.stamp(expected)
    assertEquals(expected, actual)
  }

  @Test
  fun stamp___6() {
    """
      ...stamp___6()
      When given a stencil containing a variable
      But does not have a mapped value
      Throws an exception
    """.describe()

    assertFails {
      val sub = Substitutor2.bashStyle { null }
      sub.stamp("\${abc}")
    }
  }

  @Test
  fun stamp___7() {
    """
      ...stamp___7()
      When given a stencil containing multiple occurrences of the same variable
      Returns the input with all occurrences replaced with the mapped value
    """.describe()

    val sub = Substitutor2.bashStyle(stampVars)
    val actual = sub.stamp("\${Rince}_\${Rince}_\${Rince}")
    assertEquals("wind_wind_wind", actual)
  }
}