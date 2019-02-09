import org.junit.jupiter.api.Test
import kotlin.test.*

class AnyTest {

  // TODO: Split these tests into different test files but leave 'any.kt'
  // TODO: as a monofile so it's easy to do editor searches

  @Test
  fun not___1() {
    assertFalse( not { true } )
    assertTrue( not { false } )
  }

  @Test
  fun Collection_isSingleton___1() {
    assertFalse( listOf<Any>().isSingleton() )
    assertFalse( listOf<Any>(1,2,3).isSingleton() )
    assertTrue( listOf<Any>(1).isSingleton() )
  }

  @Test
  fun Any_toOptional___1() {
    assertFalse( null.toOptional().isPresent )
    assertTrue( "".toOptional().isPresent )
  }

  @Test
  fun String_toBool___1() {
    assertTrue("y".toBool())
    assertTrue("Y".toBool())
    assertTrue("yes".toBool())
    assertTrue("YES".toBool())
    assertTrue("yEs".toBool())
    assertTrue("YeS".toBool())
  }

  @Test
  fun String_toBool___2() {
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

  @Test
  fun Boolean_toYesOrNo___1() {
    assertEquals("YES", true.toYesOrNo())
    assertEquals("yes", true.toYesOrNo(true))
    assertEquals("NO", false.toYesOrNo())
    assertEquals("no", false.toYesOrNo(true))
  }

  @Test
  fun Boolean_toYorN___1() {
    assertEquals("Y", true.toYorN())
    assertEquals("y", true.toYorN(true))
    assertEquals("N", false.toYorN())
    assertEquals("n", false.toYorN(true))
  }

  @Test
  fun String_bifurcate___String___1() {
    val (a, b) = "a,b".bifurcate(",")
    assertEquals("a", a)
    assertNotNull<Any>(b)
    assertEquals("b", b)
  }

  @Test
  fun String_bifurcate___String___2() {
    val (a, b) = "a,".bifurcate(",")
    assertEquals("a", a)
    assertNotNull<Any>(b)
    assertEquals("", b)
  }

  @Test
  fun String_bifurcate___String___3() {
    val (a, b) = ",b".bifurcate(",")
    assertEquals("", a)
    assertNotNull<Any>(b)
    assertEquals("b", b)
  }

  @Test
  fun String_bifurcate___String___4() {
    val (a, bc) = "a,bc".bifurcate(",")
    assertEquals("a", a)
    assertNotNull<Any>(bc)
    assertEquals("bc", bc)
  }

  @Test
  fun String_bifurcate___String___5() {
    val (a, bc) = "a,b,c".bifurcate(",")
    assertEquals("a", a)
    assertNotNull<Any>(bc)
    assertEquals("b,c", bc)
  }

  @Test
  fun String_bifurcate___String___6() {
    val (ab, n) = "ab".bifurcate(",")
    assertEquals("ab", ab)
    assertNull(n)
  }

  @Test
  fun String_bifurcate___Regex___1() {
    val (a, b) = "a  ,  b".bifurcate("\\s*,\\s*".toRegex())
    assertEquals("a", a)
    assertNotNull<Any>(b)
    assertEquals("b", b)
  }

  @Test
  fun String_bifurcate___Regex___2() {
    val (a, b) = "a ".bifurcate("\\s".toRegex())
    assertEquals("a", a)
    assertNotNull<Any>(b)
    assertEquals("", b)
  }

  @Test
  fun String_bifurcate___Regex___3() {
    val (a, b) = " b".bifurcate("\\s".toRegex())
    assertEquals("", a)
    assertNotNull<Any>(b)
    assertEquals("b", b)
  }

  @Test
  fun String_bifurcate___Regex___4() {
    val (a, bc) = "a[delim]bc".bifurcate("\\[.*]".toRegex())
    assertEquals("a", a)
    assertNotNull<Any>(bc)
    assertEquals("bc", bc)
  }

  @Test
  fun String_bifurcate___Regex___5() {
    val (a, bc) = "a b c".bifurcate(" ".toRegex())
    assertEquals("a", a)
    assertNotNull<Any>(bc)
    assertEquals("b c", bc)
  }

  @Test
  fun String_bifurcate___Regex___6() {
    val (ab, n) = "ab".bifurcate("\\n".toRegex())
    assertEquals("ab", ab)
    assertNull(n)
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