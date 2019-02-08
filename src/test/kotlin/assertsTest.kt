import org.junit.jupiter.api.Test
import kotlin.test.assertFails

class AssertsTest {

  @Test
  fun Collection_assertNoDuplicates__noDuplicates__notThrowException() {
    listOf(1, 2, 3, 4).assertNoDuplicates("Test failed!")
  }

  @Test
  fun Collection_assertNoDuplicates__withDuplicates__throwsException() {
    assertFails {
      listOf(1, 2, 3, 1).assertNoDuplicates("Test passed!")
    }
  }

  @Test
  fun Collection_assertNoDuplicates__withNullButNoDuplicates__notThrowException() {
    listOf(1, null, 3, 4).assertNoDuplicates("Test failed!")
  }

  @Test
  fun Collection_assertNoDuplicates__withNullDuplicates__throwsException() {
    assertFails {
      listOf(null, 2, 3, null).assertNoDuplicates("Test passed!")
    }
  }
}