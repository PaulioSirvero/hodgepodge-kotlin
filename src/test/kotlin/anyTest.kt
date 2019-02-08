import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnyTest {

  @Test
  fun String_toBool__y_or_yes__returnsTrue() {
    assertTrue("y".toBool())
    assertTrue("Y".toBool())
    assertTrue("yes".toBool())
    assertTrue("YES".toBool())
    assertTrue("yEs".toBool())
    assertTrue("YeS".toBool())
  }

  @Test
  fun String_toBool__not_y_or_yes__returnsTrue() {
    assertFalse("n".toBool())
    assertFalse("N".toBool())
    assertFalse("no".toBool())
    assertFalse("NO".toBool())
    assertFalse("No".toBool())
    assertFalse("oN".toBool())
    assertFalse("".toBool())
    assertFalse(" ".toBool())
    assertFalse("asdfsdf".toBool())
    assertFalse("2134fd\n\t23sdff".toBool())
    assertFalse("yesyes".toBool())
    assertFalse("yy".toBool())
  }

  val phrase = """
    The more that you read
    the more things you will know
    the more that you learn
    the more places you’ll go
  """

  fun expectedResult(delim: String) = "The more that you read${delim}the more things you will know${delim}the more that you learn${delim}the more places you’ll go"

  @Test
  fun lineUp_emptyDelim_throwsException() {
    assertFails {
      phrase.lineUp("")
    }
  }

  @Test
  fun lineUp_commaDelim_removesLeadingAndTrailingWhitespace() {
    val actual = phrase.lineUp(",")
    assertFalse(actual.matches("^\\s.*".toRegex()))
    assertFalse(actual.matches(".*\\s$".toRegex()))
  }

  @Test
  fun lineUp_commaDelim_returnsStringWithNoLineBreaks() {
    val actual = phrase.lineUp(",")
    val containsNewLines = actual.contains("\n")
    assertFalse(containsNewLines)
  }

  @Test
  fun lineUp_commaDelim_returnsStringWithCommas() {
    val actual = phrase.lineUp(",")
    val containsCommas = actual.contains(",")
    assertTrue(containsCommas)
  }

  @Test
  fun lineUp_commaDelim_returnsExpectedOneLineSentence() {
    val expected = expectedResult(",")
    val actual = phrase.lineUp(",")
    assertEquals(expected, actual)
  }

  @Test
  fun lineUp_tabDelim_returnsExpectedOneLineSentence() {
    val expected = expectedResult("\t")
    val actual = phrase.lineUp("\t")
    assertEquals(expected, actual)
  }

  @Test
  fun lineUp_emptyPhrase_returnsEmptyString() {
    val expected = ""
    val actual = "".lineUp(",")
    assertEquals(expected, actual)
  }

  @Test
  fun lineUp_blankPhrase_returnsEmptyString() {
    val expected = ""
    val actual = "  \t\n   \t \t\n   ".lineUp(",")
    assertEquals(expected, actual)
  }
}