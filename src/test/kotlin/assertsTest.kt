import org.junit.jupiter.api.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class AssertsTest {

  @Test
  fun T_assertNotNull___1() {
    "".assertNotNull("Should not throw")
  }

  @Test
  fun T_assertNotNull___2() {
    assertFailsWith<FailedAssert> {
      null.assertNotNull("Should throw")
    }
  }

  @Test
  fun String_assertNotEmpty___1() {
    "not empty".assertNotEmpty("Should not throw")
  }

  @Test
  fun String_assertNotEmpty___2() {
    assertFailsWith<FailedAssert> {
      "".assertNotEmpty("Should throw")
    }
  }

  @Test
  fun String_assertNotBlank___1() {
    "not blank".assertNotBlank("Should not throw")
  }

  @Test
  fun String_assertNotBlank___2() {
    assertFailsWith<FailedAssert> {
      "  \n  \r  \t  ".assertNotBlank("Should throw")
    }
  }

  @Test
  fun String_assertLength___1() {
    "1234".assertLength(4, "Should not throw")
  }

  @Test
  fun String_assertLength___2() {
    assertFailsWith<FailedAssert> {
      "1234".assertLength(5, "Should throw")
    }
  }

  @Test
  fun String_assertMatches___1() {
    "abc".assertMatches("[a-c]{3}".toRegex(), "Should not throw")
  }

  @Test
  fun String_assertMatches___2() {
    assertFailsWith<FailedAssert> {
      "abc".assertMatches("[x-z]{3}".toRegex(), "Should throw")
    }
  }

  @Test
  fun Collection_assertNotEmpty___1() {
    listOf(1,2,3).assertNotEmpty("Should not throw")
  }

  @Test
  fun Collection_assertNotEmpty___2() {
    assertFailsWith<FailedAssert> {
      listOf<Any>().assertNotEmpty("Should throw")
    }
  }

  @Test
  fun Collection_assertLength___1() {
    listOf(1,2,3).assertLength(3, "Should not throw")
  }

  @Test
  fun Collection_assertLength___2() {
    assertFailsWith<FailedAssert> {
      listOf(1,2,3).assertLength(4, "Should throw")
    }
  }

  @Test
  fun Collection_assertNoDuplicates__noDuplicates__notThrowException() {
    listOf(1, 2, 3, 4).assertNoDuplicates("Test failed!")
  }

  @Test
  fun Collection_assertNoDuplicates__withDuplicates__throwsException() {
    assertFailsWith<FailedAssert> {
      listOf(1, 2, 3, 1).assertNoDuplicates("Test passed!")
    }
  }

  @Test
  fun Collection_assertNoDuplicates__withNullButNoDuplicates__notThrowException() {
    listOf(1, null, 3, 4).assertNoDuplicates("Test failed!")
  }

  @Test
  fun Collection_assertNoDuplicates__withNullDuplicates__throwsException() {
    assertFailsWith<FailedAssert> {
      listOf(null, 2, 3, null).assertNoDuplicates("Test passed!")
    }
  }

  @Test
  fun Map_assertNotEmpty___1() {
    mapOf(1 to "1", 2 to "2").assertNotEmpty("Should not throw")
  }

  @Test
  fun Map_assertNotEmpty___2() {
    assertFailsWith<FailedAssert> {
      mapOf<Any, Any>().assertNotEmpty("Should throw")
    }
  }

  @Test
  fun Map_assertLength___1() {
    mapOf(1 to "1", 2 to "2").assertLength(2, "Should not throw")
  }

  @Test
  fun Map_assertLength___2() {
    assertFailsWith<FailedAssert> {
      mapOf(1 to "1", 2 to "2").assertLength(3, "Should throw")
    }
  }

  @Test
  fun Number_assertZero___1() {
    0.0.assertZero("Should not throw")
  }

  @Test
  fun Number_assertZero___2() {
    assertFailsWith<FailedAssert> {
      1.1.assertZero("Should throw")
    }
  }

  @Test
  fun Number_assertNotNegative___1() {
    1.assertNotNegative("Should not throw")
  }

  @Test
  fun Number_assertNotNegative___2() {
    assertFailsWith<FailedAssert> {
      (-1).assertNotNegative("Should throw")
    }
  }

  @Test
  fun Number_assertNotNegative___3() {
    0.assertNotNegative("Should not throw")
  }

  @Test
  fun Number_assertNotPositive___1() {
    (-1).assertNotPositive("Should not throw")
  }

  @Test
  fun Number_assertNotPositive___2() {
    assertFailsWith<FailedAssert> {
      1.assertNotPositive("Should throw")
    }
  }

  @Test
  fun Number_assertNotPositive___3() {
    0.assertNotPositive("Should not throw")
  }

  @Test
  fun Number_assertNegative___1() {
    (-1).assertNegative("Should not throw")
  }

  @Test
  fun Number_assertNegative___2() {
    assertFailsWith<FailedAssert> {
      1.assertNegative("Should throw")
    }
  }

  @Test
  fun Number_assertNegative___3() {
    assertFailsWith<FailedAssert> {
      0.assertNegative("Should throw")
    }
  }

  @Test
  fun Number_assertPositive___1() {
    1.assertPositive("Should not throw")
  }

  @Test
  fun Number_assertPositive___2() {
    assertFailsWith<FailedAssert> {
      (-1).assertPositive("Should throw")
    }
  }

  @Test
  fun Number_assertPositive___3() {
    assertFailsWith<FailedAssert> {
      0.assertPositive("Should throw")
    }
  }

  @Test
  fun Number_assertLessThan___1() {
    1.assertLessThan(2, "Should not throw")
  }

  @Test
  fun Number_assertLessThan___2() {
    assertFailsWith<FailedAssert> {
      2.assertLessThan(2, "Should throw")
    }
  }

  @Test
  fun Number_assertLessThan___3() {
    assertFailsWith<FailedAssert> {
      3.assertLessThan(2, "Should throw")
    }
  }

  @Test
  fun Number_assertLessThanOrEqual___1() {
    1.assertLessThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_assertLessThanOrEqual___2() {
    2.assertLessThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_assertLessThanOrEqual___3() {
    assertFailsWith<FailedAssert> {
      3.assertLessThanOrEqual(2, "Should throw")
    }
  }

  @Test
  fun Number_assertGreaterThan___1() {
    3.assertGreaterThan(2, "Should not throw")
  }

  @Test
  fun Number_assertGreaterThan___2() {
    assertFailsWith<FailedAssert> {
      2.assertGreaterThan(2, "Should throw")
    }
  }

  @Test
  fun Number_assertGreaterThan___3() {
    assertFailsWith<FailedAssert> {
      1.assertGreaterThan(2, "Should throw")
    }
  }

  @Test
  fun Number_assertGreaterThanOrEqual___1() {
    3.assertGreaterThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_assertGreaterThanOrEqual___2() {
    2.assertGreaterThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_assertGreaterThanOrEqual___3() {
    assertFailsWith<FailedAssert> {
      1.assertGreaterThanOrEqual(2, "Should throw")
    }
  }
}