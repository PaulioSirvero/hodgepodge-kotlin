
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private class VarSrc(
  val groupless: Map<String, String>,
  val grouped: Map<String, List<String>>
): VariableSource {

  override fun valueOf(name: String): Optional<String> = groupless[name].toOptional()

  override fun valueOf(group: String, index: Int): Optional<String> {
    val list = grouped[group] ?: return Optional.empty()
    if (index >= list.size) return Optional.empty()
    return list[index].toOptional()
  }
}

private val groupless = mapOf(
  "abc" to "123",
  "efg" to "456",
  "hij" to "789"
)

private val lean = listOf("delete", "simplify", "automate")
private val powerTriangle = listOf("power", "root", "log")
private val grouped = mapOf(
  "lean" to lean,
  "powerTriangle" to powerTriangle
)

private fun new() = Substitutor.bashStyle(
  VarSrc(groupless, grouped)
)

private fun assertGood(expected: String?, result: AnyResult<String>) {
  assertNotNull(result)
  val actual = result.unwrap()
  assertEquals(expected, actual)
}

class BashStyleSubstitutorTest {

  @Test
  fun constructor_constructs_withoutError() {
    new()
  }

  @Test
  fun noTemplates_returnsInputUnchanged() {
    val expected = "expected"
    val result = new().format(expected)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_soloTemplate_goodResult() {
    val stencil = "\${abc}"
    val expected = "123"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_withPrefix_goodResult() {
    val stencil = "tiger \${abc}"
    val expected = "tiger 123"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_withSuffix_goodResult() {
    val stencil = "\${abc} lion"
    val expected = "123 lion"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_withPrefixAndSuffix_goodResult() {
    val stencil = "tiger \${abc} lion"
    val expected = "tiger 123 lion"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_multiple_goodResult() {
    val stencil = "\${abc}\${efg}\${hij}"
    val expected = "123456789"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_multipleWithText_goodResult() {
    val stencil = "\${abc} tiger \${efg} lynx \${hij}"
    val expected = "123 tiger 456 lynx 789"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_noEndBracket_GoodResult() {
    val stencil = "\${abc"
    val expected = "\${abc"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_badFormat_GoodResult() {
    val stencil = "\${abc efg}"
    val expected = "\${abc efg}"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun groupLess_notFound_BadResult() {
    val stencil = "\${tiger}"
    val result = new().format(stencil)
    assert(result.isBad)
  }

  @Test
  fun grouped_soloTemplate_goodResult() {
    val stencil = "\${lean:0}"
    val expected = "delete"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_withPrefix_goodResult() {
    val stencil = "tiger \${lean:0}"
    val expected = "tiger delete"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_withSuffix_goodResult() {
    val stencil = "\${lean:0} lynx"
    val expected = "delete lynx"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_withPrefixAndSuffix_goodResult() {
    val stencil = "tiger \${lean:0} lynx"
    val expected = "tiger delete lynx"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_multiple_goodResult() {
    val stencil = "\${powerTriangle:0}\${powerTriangle:1}\${powerTriangle:2}"
    val expected = "powerrootlog"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_multipleWithText_goodResult() {
    val stencil = "\${lean:1} tiger \${powerTriangle:1} lynx \${powerTriangle:2}"
    val expected = "simplify tiger root lynx log"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_noEndBracket_GoodResult() {
    val stencil = "\${lean:0"
    val expected = "\${lean:0"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_badFormat_GoodResult() {
    val stencil = "\${lean:1bad}"
    val expected = "\${lean:1bad}"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_badFormat2_GoodResult() {
    val stencil = "\${lean:-1}"
    val expected = "\${lean:-1}"
    val result = new().format(stencil)
    assertGood(expected, result)
  }

  @Test
  fun grouped_groupNotFound_BadResult() {
    val stencil = "\${tiger:0}"
    val result = new().format(stencil)
    assert(result.isBad)
  }

  @Test
  fun grouped_indexTooBig_BadResult() {
    val stencil = "\${lean:999}"
    val result = new().format(stencil)
    assert(result.isBad)
  }
}