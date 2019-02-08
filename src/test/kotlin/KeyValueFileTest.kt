import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.OS
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val resources = Paths.get("src/test/resources").toAbsolutePath()
private val file_1 = resources.resolve("store_1.kvs")

class KeyValueFileTest {

  companion object {

    @BeforeAll
    @JvmStatic
    fun beforeAllTests() {
      Files.createDirectories(resources)
    }
  }

  @AfterEach
  fun afterEachTest() {
    Files.deleteIfExists(file_1)
  }

  private val expected_abc = "abc" to "Rincewind"
  private val expected_efg = "efg" to "Weatherwax"
  private val expected_hij = "   hij   " to "Mort"
  private val expected_klm = "klm" to ""

  private val valid_data = mapOf(
    expected_abc,
    expected_efg,
    expected_hij,
    expected_klm
  )

  fun obtainOSSpecificDeadPath() = when {
    OS.WINDOWS.isCurrentOs -> Paths.get(
      "I:\\asdasdasda\\"
    )
    OS.LINUX.isCurrentOs -> Paths.get(
      "$resources/asdasdasda/\\0"
    )
    else -> throw Exception("Operating system unknown to test case")
  }

  @Test
  fun write_badPath_returnsBadResult() {
    val kvFile = KeyValueFile(obtainOSSpecificDeadPath())
    val data = mapOf(
      "abcd" to "Ogg"
    )
    val result = kvFile.write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write_aKeyContainsEqualsSymbol_returnsBadResult() {
    val kvFile = KeyValueFile(file_1)
    val data = mapOf(
      "ab=cd" to "Ogg"
    )
    val result = kvFile.write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write_aKeyContainsNewlineSymbol_returnsBadResult() {
    val kvFile = KeyValueFile(file_1)
    val data = mapOf(
      "ab\ncd" to "Ogg"
    )
    val result = kvFile.write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write_noKeysContainEqualsOrNewlineSymbols_returnsGoodResult() {
    val kvFile = KeyValueFile(file_1)
    val data = mapOf(
      "abcd" to "Ogg"
    )
    val result = kvFile.write(data)
    assertTrue(result.isGood)
  }

  @Test
  fun write_aValueContainsNewlineSymbol_returnsBadResult() {
    val kvFile = KeyValueFile(file_1)
    val data = mapOf(
      "abcd" to "  Og\ng  "
    )
    val result = kvFile.write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write_noValuesContainNewlineSymbols_returnsGoodResult() {
    val kvFile = KeyValueFile(file_1)
    val data = mapOf(
      "abcd" to "Ogg"
    )
    val result = kvFile.write(data)
    assertTrue(result.isGood)
  }

  @Test
  fun write_validData_returnsGoodResult() {
    val kvFile = KeyValueFile(file_1)
    val result = kvFile.write(valid_data)
    assertTrue(result.isGood)
  }

  @Test
  fun write_validData_createsFile() {
    val kvFile = KeyValueFile(file_1)
    kvFile.write(valid_data)
    assertTrue(Files.exists(file_1))
  }

  @Test
  fun read_badPath_returnsBadResult() {
    val kvFile = KeyValueFile(obtainOSSpecificDeadPath())
    val result = kvFile.read()
    assertTrue(result.isBad)
  }

  @Test
  fun read_validData_returnsGoodResult() {
    val content = "abc=Susan\n"
    Files.write(file_1, content.toByteArray(Charsets.UTF_8))

    val kvFile = KeyValueFile(file_1)
    val data = kvFile.read()
    assertTrue(data.isGood)
  }

  @Test
  fun read_aKeyValuePairContainsANewlineSymbol_returnsBadResult() {
    val content = "ab\nc=ogg"
    Files.write(file_1, content.toByteArray(Charsets.UTF_8))

    val kvFile = KeyValueFile(file_1)
    val data = kvFile.read()
    assertTrue(data.isBad)
  }

  @Test
  fun writeThenRead_validInput_createsFileAndReturnsGoodResultContainingWrittenData() {
    KeyValueFile(file_1).write(valid_data)
    val result = KeyValueFile(file_1).read()

    assertTrue(Files.exists(file_1))
    assertTrue(result.isGood)
    val data = result.unwrap()

    assertEquals(valid_data.size, data.size)

    assertTrue(data.containsKey(expected_abc.first))
    assertTrue(data.containsKey(expected_efg.first))
    assertTrue(data.containsKey(expected_hij.first))
    assertTrue(data.containsKey(expected_klm.first))

    assertEquals(expected_abc.second, data[expected_abc.first])
    assertEquals(expected_efg.second, data[expected_efg.first])
    assertEquals(expected_hij.second, data[expected_hij.first])
    assertEquals(expected_klm.second, data[expected_klm.first])
  }
}