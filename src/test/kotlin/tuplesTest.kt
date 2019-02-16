import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class tuplesTest {

  @Test
  fun pair_swap___1() {
    val before = 1 to 2
    val after = before.swap()
    assertEquals(1, after.second)
    assertEquals(2, after.first)
  }

  @Test
  fun pair_shuffle___1() {
    val before = 1 to 2
    for (i in 0..100) {
      val after = before.shuffle()
      if(after.first == 2) return
    }
    fail("Streak of 100 non-swaps is so tiny the function must be bugged")
  }

  @Test
  fun pair_contains___1() {
    val actual = (1 to 2).contains(1)
    assertTrue(actual)
  }

  @Test
  fun pair_contains___2() {
    val actual = (1 to 2).contains(2)
    assertTrue(actual)
  }

  @Test
  fun pair_contains___3() {
    val actual = (1 to 2).contains(3)
    assertFalse(actual)
  }

  @Test
  fun pair_map___1() {
    val expected = 3.0 to 4.0
    val actual = (1 to 2).map { it.first + 2.0 to it.second + 2.0 }
    assertEquals(expected, actual)
  }

  @Test
  fun pair_plus___1() {
    val actual = (1 to 2) + 3
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_plus___2() {
    val actual = (1 to 2) + { 3 }
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_midsert___1() {
    val actual = (1 to 3).midsert(2)
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_midsert___2() {
    val actual = (1 to 3).midsert { 2 }
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }
}