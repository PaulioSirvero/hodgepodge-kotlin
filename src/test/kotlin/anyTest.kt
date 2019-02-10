import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.*

class AnyTest {

  private val file_1 = Paths.get("src/test/resources/test.txt")
    .toAbsolutePath()
    .normalize()

  @AfterEach
  fun afterEachTest() {
    Files.deleteIfExists(file_1)
  }

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

  @Test
  fun String_substring___Regex___1() {
    val actual = "a\tbcd".substring("\\s+bc".toRegex())
    assertNotNull<Any>(actual)
    assertEquals("\tbc", actual)
  }

  @Test
  fun String_substring___Regex___2() {
    val actual = "abcd".substring("\\s+bc".toRegex())
    assertNull(actual)
  }

  @Test
  fun String_substring___Regex___3() {
    val input = "The timestamp is 2019-02-09T12:01:00."
    val actual = input.substring("[0-9]{4}-[0-1][0-9]-[0-3][0-9]".toRegex())
    assertNotNull<Any>(actual)
    assertEquals("2019-02-09", actual)
  }

  @Test
  fun String_substring___Regex_Int___1() {
    val actual = "abcd".substring("a(bc)d".toRegex(), 1)
    assertNotNull<Any>(actual)
    assertEquals("bc", actual)
  }

  @Test
  fun String_substring___Regex_Int___2() {
    val actual = "abcdefghijk".substring("b(cd(efg)hi)j".toRegex(), 2)
    assertNotNull<Any>(actual)
    assertEquals("efg", actual)
  }

  @Test
  fun String_substring___Regex_Int___3() {
    val actual = "abcdefghijk".substring("xyz".toRegex())
    assertNull(actual)
  }

  @Test
  fun String_substring___Regex_Int___4() {
    val input = "The timestamp is 2019-02-09T12:01:00."
    val actual = input.substring("([0-9]{4})-([0-1][0-9])-([0-3][0-9])".toRegex(), 3)
    assertNotNull<Any>(actual)
    assertEquals("09", actual)
  }

  @Test
  fun String_prefix___1() {
    val actual = "abc/".prefix("/")
    assertEquals("/abc/", actual)
  }

  @Test
  fun String_prefix___2() {
    val actual = "/abc/".prefix("/")
    assertEquals("/abc/", actual)
  }

  @Test
  fun String_prefix___3() {
    val actual = "abc".prefix("")
    assertEquals("abc", actual)
  }

  @Test
  fun String_suffix___1() {
    val actual = "/abc".suffix("/")
    assertEquals("/abc/", actual)
  }

  @Test
  fun String_suffix___2() {
    val actual = "/abc/".suffix("/")
    assertEquals("/abc/", actual)
  }

  @Test
  fun String_suffix___3() {
    val actual = "abc".suffix("")
    assertEquals("abc", actual)
  }

  private val phrase = """
    The more that you read
    the more things you will know
    the more that you learn
    the more places you’ll go
  """

  fun expectedResult(delim: String) = "The more that you read${delim}the more things you will know${delim}the more that you learn${delim}the more places you’ll go"

  @Test
  fun String_lineUp___1() {
    val actual = phrase.lineUp("")
    assertEquals(expectedResult(""), actual)
  }

  @Test
  fun String_lineUp___2() {
    val actual = phrase.lineUp(",")
    assertFalse(actual.matches("^\\s.*".toRegex()))
    assertFalse(actual.matches(".*\\s$".toRegex()))
  }

  @Test
  fun String_lineUp___3() {
    val actual = phrase.lineUp(",")
    val containsNewLines = actual.contains("\n")
    assertFalse(containsNewLines)
  }

  @Test
  fun String_lineUp___4() {
    val actual = phrase.lineUp(",")
    val containsCommas = actual.contains(",")
    assertTrue(containsCommas)
  }

  @Test
  fun String_lineUp___5() {
    val expected = expectedResult(",")
    val actual = phrase.lineUp(",")
    assertEquals(expected, actual)
  }

  @Test
  fun String_lineUp___6() {
    val expected = expectedResult("\t")
    val actual = phrase.lineUp("\t")
    assertEquals(expected, actual)
  }

  @Test
  fun String_lineUp___7() {
    val actual = "".lineUp(",")
    assertEquals("", actual)
  }

  @Test
  fun String_lineUp___8() {
    val actual = "  \t\n   \t \t\n   ".lineUp(",")
    assertEquals("", actual)
  }

  @Test
  fun List_String_stripBlanks___1() {
    val input = listOf("abc", "", "efg")
    val expected = listOf("abc", "efg")
    val actual = input.stripBlanks()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_stripBlanks___2() {
    val input = listOf("", "abc", "\n", "efg", "\t")
    val expected = listOf("abc", "efg")
    val actual = input.stripBlanks()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_stripBlanks___3() {
    val input = listOf("abc", "efg")
    val expected = listOf("abc", "efg")
    val actual = input.stripBlanks()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimBlanks___1() {
    val input = listOf("", "abc", "efg", "")
    val expected = listOf("abc", "efg")
    val actual = input.trimBlanks()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimBlanks___2() {
    val input = listOf("abc", "", "efg")
    val expected = listOf("abc", "", "efg")
    val actual = input.trimBlanks()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimBlanks___3() {
    val input = listOf("   \t \t \r", "abc", "\n", "efg", "\r\n\r\n")
    val expected = listOf("abc", "\n", "efg")
    val actual = input.trimBlanks()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimStart___1() {
    val input = listOf("", "abc", "efg")
    val expected = listOf("abc", "efg")
    val actual = input.trimStart()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimStart___2() {
    val input = listOf("abc", "efg", "")
    val expected = listOf("abc", "efg", "")
    val actual = input.trimStart()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimStart___3() {
    val input = listOf("", "abc", "", "efg")
    val expected = listOf("abc", "", "efg")
    val actual = input.trimStart()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimEnd___1() {
    val input = listOf("abc", "efg", "")
    val expected = listOf("abc", "efg")
    val actual = input.trimEnd()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimEnd___2() {
    val input = listOf("", "abc", "efg")
    val expected = listOf("", "abc", "efg")
    val actual = input.trimEnd()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_trimEnd___3() {
    val input = listOf("abc", "", "efg", "")
    val expected = listOf("abc", "", "efg")
    val actual = input.trimEnd()
    assertEquals(expected, actual)
  }

  @Test
  fun List_String_indexOf___1() {
    val input = listOf("abc", "efg", "123", "456")
    val actual = input.indexOf("efg".toRegex())
    assertEquals(1, actual)
  }

  @Test
  fun List_String_indexOf___2() {
    val input = listOf("abc", "efg", "123", "456")
    val actual = input.indexOf("".toRegex())
    assertEquals(0, actual)
  }

  @Test
  fun List_String_indexOf___3() {
    val input = listOf<String>()
    val actual = input.indexOf("".toRegex())
    assertEquals(-1, actual)
  }

  @Test
  fun List_String_hexToByteArray___1() {
    val input = "0A6F43500F4A"
    val list = listOf(0x0A, 0x6F, 0x43, 0x50, 0x0F, 0x4A)
    val expected = ByteArray(list.size) { list[it].toByte() }
    val actual = input.hexToByteArray()

    assertEquals(expected.size, actual.size)
    for(i in 0 until expected.size) {
      assertEquals(expected[i], actual[i])
    }
  }

  @Test
  fun List_String_hexToByteArray___2() {
    assertFails {
      "0A6F43500F4A0".hexToByteArray()
    }
  }

  @Test
  fun List_String_hexToByteArray___3() {
    assertFails {
      "0X".hexToByteArray()
    }
  }

  @Test
  fun List_String_hexToByteArray___4() {
    assertFails {
      "X0".hexToByteArray()
    }
  }

  @Test
  fun List_String_hexToByteArray___5() {
    val input = ""
    val actual = input.hexToByteArray()
    assertTrue(actual.isEmpty())
  }

  @Test
  fun List_String_sha256___1() {
    val input = "Triumph Or Agony"
    val expected = """
      0A6F43500F4A50576945726B9ACF0339
      5A73A3F9A9B0FFD8A9D0D350A4207565
    """.lineUp("").hexToByteArray()
    val actual = input.sha256()

    assertEquals(expected.size, actual.size)
    for(i in 0 until expected.size) {
      assertEquals(expected[i], actual[i])
    }
  }

  @Test
  fun List_String_sha512___1() {
    val input = "Triumph Or Agony"
    val expected = """
      7BB5A2271B43FA0F72AE5011A0A682B2
      CA79AE9E10DFBE70FCFF67BB05A62DF0
      B4E94FA2E09A88F6D5B268699C00883B
      7462FAF7AA4A25D94E5C0C642C35DB02
    """.lineUp("").hexToByteArray()
    val actual = input.sha512()

    assertEquals(expected.size, actual.size)
    for(i in 0 until expected.size) {
      assertEquals(expected[i], actual[i])
    }
  }

  @Test
  fun Path_fileToSha256___1() {
    println("""
      ...Path.fileToSha256()
      When given a valid path to an existing file that contains some data
      The files content is hashed using SHA-256
      And the resultant hash is returned
    """.lineUp("\n\t"))

    Files.write(file_1, "Triumph Or Agony".toByteArray())

    val expected = """
      0A6F43500F4A50576945726B9ACF0339
      5A73A3F9A9B0FFD8A9D0D350A4207565
    """.lineUp("").hexToByteArray()

    val actual = file_1.fileToSha256()

    assertEquals(expected.size, actual.size)
    for(i in 0 until expected.size) {
      assertEquals(expected[i], actual[i])
    }
  }

  @Test
  fun Path_fileToSha512___1() {
    println("""
      ...Path.fileToSha512()
      When given a valid path to an existing file that contains some data
      The files content is hashed using SHA-512
      And the resultant hash is returned
    """.lineUp("\n\t"))

    Files.write(file_1, "Triumph Or Agony".toByteArray())

    val expected = """
      7BB5A2271B43FA0F72AE5011A0A682B2
      CA79AE9E10DFBE70FCFF67BB05A62DF0
      B4E94FA2E09A88F6D5B268699C00883B
      7462FAF7AA4A25D94E5C0C642C35DB02
    """.lineUp("").hexToByteArray()

    val actual = file_1.fileToSha512()

    assertEquals(expected.size, actual.size)
    for(i in 0 until expected.size) {
      assertEquals(expected[i], actual[i])
    }
  }
}