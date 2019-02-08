
/* **************************************************
Gradle imports:
testImplementation 'org.junit.jupiter:junit-jupiter-api:5.3.2'
testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.3.2'
testImplementation group: 'org.jetbrains.kotlin', name: 'kotlin-test', version: '1.3.11'
************************************************** */

import org.junit.jupiter.api.Test
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.test.*

class AnyResultTest {

  private val pass = 9
  private val fail = -1
  private val passAlt = ":)"

  private val errMsg = "ERROR"
  private val exMsg = "EXCEPTION"
  private val errMsgAlt = ":("
  private val errEx = Exception(exMsg)

  private val goodPass = GoodResult(pass)
  private val goodFail = GoodResult(fail)
  private val bad = BadResult<Int>(errMsg, errEx)

  private val realGoodPass = GoodResult<Int?>(pass)
  private val realGoodFail = GoodResult<Int?>(fail)
  private val nullGood = GoodResult<Int?>(null)

  private fun <R> new(good: GoodResult<R>, bad: BadResult<R>) = AnyResult(good, bad)

  private fun <R> newGood(good: GoodResult<R>) = AnyResult(good, null)

  private fun <R> newBad(bad: BadResult<R>) = AnyResult(null, bad)

  @Test
  fun construct_goodNull_badNull_throws() {
    assertFailsWith(Exception::class) {
      AnyResult<Boolean>(null, null)
    }
  }

  @Test
  fun construct_goodNonNull_badNonNull_throws() {
    assertFailsWith(Exception::class) {
      AnyResult(goodPass, bad)
    }
  }

  @Test
  fun construct_goodNonNull_badNull_noException() {
    AnyResult(goodPass, null)
  }

  @Test
  fun construct_goodNull_badNonNull_noException() {
    AnyResult(null, bad)
  }

  @Test
  fun good_toGood_noException() {
    newGood(goodPass).toGood()
  }

  @Test
  fun good_toBad_throws() {
    assertFailsWith(Exception::class) {
      newGood(goodPass).toBad()
    }
  }

  @Test
  fun bad_toBad_noException() {
    newBad(bad).toBad()
  }

  @Test
  fun bad_toGood_throws() {
    assertFailsWith(Exception::class) {
      newBad(bad).toGood()
    }
  }

  @Test
  fun good_ifGood_runsFunction() {
    var actual = fail
    newGood(goodPass).ifGood { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_ifBad_runsFunction() {
    var actual = fail
    newBad(bad).ifBad { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_ifBad_doesNothing() {
    var actual = pass
    newGood(goodPass).ifBad { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_ifBad_doesNothing() {
    var actual = pass
    newBad(bad).ifGood { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun good_whenGood_returnsValue() {
    val actual = newGood(goodFail).whenGood(pass) { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_whenBad_returnsValue() {
    val actual = newBad(bad).whenBad(pass) { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun good_whenBad_runsFunction() {
    val actual = newGood(goodFail).whenBad(fail) { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_whenGood_runsFunction() {
    val actual = newBad(bad).whenGood(fail) { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_map_runsFunction() {
    var actual = fail
    newGood(goodPass).map { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_map_returnsGoodResult() {
    val actual = newGood(goodPass).map { passAlt }
    assertTrue(actual.isGood)
    actual.toGood()
    assertEquals(passAlt, actual.toGood().result)

  }

  @Test
  fun bad_map_returnsBadResult() {
    val actual = newBad(bad).map { passAlt }
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsg, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }


  @Test
  fun good_map_withMsg_runsFunction() {
    var actual = fail
    newGood(goodPass).map(errMsgAlt) { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_map_withMsg_returnsGoodResult() {
    val actual = newGood(goodPass).map(errMsgAlt) { passAlt }
    assertTrue(actual.isGood)
    actual.toGood()
    assertEquals(passAlt, actual.toGood().result)
  }

  @Test
  fun bad_map_withMsg_returnsBadResult() {
    val actual = newBad(bad).map(errMsgAlt) { passAlt }
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsgAlt, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun good_mapIfBad_returnsSelf() {
    val expected = newGood(goodPass)
    val actual = expected.mapIfBad { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun bad_mapIfBad_returnsMappedResult() {
    val expected = newBad(bad)
    val actual = newBad(bad).mapIfBad { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun good_throwIfBad_noException() {
    newGood(goodPass).throwIfBad()
  }

  @Test
  fun bad_throwIfBad_throws() {
    assertFailsWith(Exception::class) {
      newBad(bad).throwIfBad()
    }
  }

  @Test
  fun good_throwIfBad_byFun_noException() {
    newGood(goodPass).throwIfBad { Exception(errMsgAlt) }
  }

  @Test
  fun bad_throwIfBad_byFun_throws() {
    assertFailsWith(Exception::class) {
      newBad(bad).throwIfBad { Exception(errMsgAlt) }
    }
  }

  @Test
  fun good_or_returnsValue() {
    val actual = newGood(goodPass).or { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_or_runsFunction() {
    val actual = newBad(bad).or { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_unwrap_returnsValue() {
    val actual = newGood(goodPass).unwrap()
    assertEquals(pass, actual)
  }

  @Test
  fun bad_unwrap_throws() {
    assertFailsWith(Exception::class) {
      newBad(bad).unwrap()
    }
  }

  @Test
  fun realGood_ifReal_runsFunction() {
    var actual = fail
    realGoodPass.ifReal { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun realGood_ifNull_doesNothing() {
    var actual = pass
    realGoodPass.ifNull { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun nullGood_ifNull_runsFunction() {
    var actual = fail
    nullGood.ifNull { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun nullGood_ifReal_doesNothing() {
    var actual = pass
    nullGood.ifReal { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun realGood_mapIfNull_doesNothing() {
    val actual = realGoodPass.mapIfNull{ realGoodFail }
    assertEquals(realGoodPass, actual)
  }

  @Test
  fun nullGood_mapIfNull_runsFunction() {
    val actual = nullGood.mapIfNull{ realGoodPass }
    assertEquals(realGoodPass, actual)
  }

  @Test
  fun bad_escalate_newBadResult() {
    val actual = bad.escalate(errMsgAlt)
    assertTrue(actual.isBad)
    assertNotEquals(bad, actual.toBad())
    assertEquals(errMsgAlt, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun bad_mapEscalate_newBadResult() {
    val actual = bad.mapEscalate<String>()
    assertTrue(actual.isBad)
    assertEquals(errMsg, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun bad_escalate_withMsg_newBadResult() {
    val actual = bad.mapEscalate<String>(errMsgAlt)
    assertTrue(actual.isBad)
    assertEquals(errMsgAlt, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun bad_throwEx_throws() {
    assertFailsWith(Exception::class) {
      bad.throwEx()
    }
  }

  @Test
  fun bad_throwWrappedEx_throws() {
    assertFailsWith(Exception::class) {
      bad.throwWrappedEx()
    }
  }

  @Test
  fun bad_throwEx_withCustomEx_throws() {
    assertFailsWith(NullPointerException::class) {
      bad.throwEx { NullPointerException() }
    }
  }

  @Test
  fun real_optResult_badResult() {
    val r = pass.optResult(errMsgAlt)
    assertTrue(r.isGood)
    r.toGood()
    assertEquals(pass, r.toGood().result)
  }

  @Test
  fun null_optResult_badResult() {
    val v: Int? = null
    val r = v.optResult(errMsgAlt)
    assertTrue(r.isBad)
    r.toBad()
    assertEquals(errMsgAlt, r.toBad().message)
  }

  @Test
  fun real_ifReal_runsFunction() {
    var actual: Int? = fail
    newGood(realGoodPass).ifReal { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun real_ifNull_doesNothing() {
    var actual: Int? = pass
    newGood(realGoodPass).ifNull { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun null_ifNull_runsFunction() {
    var actual = fail
    newGood(nullGood).ifNull { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun null_ifReal_doesNothing() {
    var actual = pass
    newGood(nullGood).ifReal { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun real_mapIfNull_doesNothing() {
    val expected = newGood(realGoodPass)
    val actual = expected.mapIfNull{ newGood(realGoodFail) }
    assertEquals(expected, actual)
  }

  @Test
  fun null_mapIfNull_runsFunction() {
    val expected = newGood(realGoodPass)
    val actual = newGood(nullGood).mapIfNull{ expected }
    assertEquals(expected, actual)
  }

  @Test
  fun real_mapIfBadOrNull_doesNothing() {
    val expected = newGood(realGoodPass)
    val actual = expected.mapIfBadOrNull { newGood(realGoodFail) }
    assertEquals(expected, actual)
  }

  @Test
  fun null_mapIfBadOrNull_runsFunction() {
    val expected = newGood(realGoodPass)
    val actual = newGood(nullGood).mapIfBadOrNull { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun bad_mapIfBadOrNull_runsFunction() {
    val expected = newGood(realGoodPass)
    val actual = AnyResult.bad<Int?>(errMsg).mapIfBadOrNull { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun real_orReal_returnsValue() {
    val actual = newGood(realGoodPass).orReal { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun null_orReal_runsFunction() {
    val actual = newGood(nullGood).orReal { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_orReal_runsFunction() {
    val actual = AnyResult.bad<Int?>(errMsg).orReal { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun real_toReal_returnsGoodResult() {
    val actual = newGood(realGoodPass).toReal(errMsgAlt)
    assertTrue(actual.isGood)
  }

  @Test
  fun null_toReal_returnsBadResult() {
    val actual = newGood(nullGood).toReal(errMsgAlt)
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsgAlt, actual.toBad().message)
  }

  @Test
  fun bad_toReal_returnsBadResult() {
    val actual = AnyResult.bad<Int?>(errMsg).toReal(errMsgAlt)
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsg, actual.toBad().message)
    assertNull(actual.toBad().exception)
  }

  @Test
  fun good_of_returnsGoodResult() {
    val expected = pass
    val actual = AnyResult.of { pass }
    assertTrue(actual.isGood)
    assertEquals(expected, actual.unwrap())
  }

  @Test
  fun bad_of_returnsBadResult() {
    val expectedMessage = errMsg
    val actual = AnyResult.of { throw Exception(errMsg) }
    assertTrue(actual.isBad)
    assertEquals(expectedMessage, actual.toBad().message)
    assertNotNull(actual.toBad().exception)
  }

  @Test
  fun bad_of_withErrMsg_returnsBadResult() {
    val expectedMessage = errMsgAlt
    val actual = AnyResult.of(errMsgAlt) { throw Exception(errMsg) }
    assertTrue(actual.isBad)
    assertEquals(expectedMessage, actual.toBad().message)
    assertNotNull(actual.toBad().exception)
  }

  @Test
  fun bad_of_noErrMsgOrExMsg_returnsBadResult() {
    val actual = AnyResult.of { throw Exception() }
    assertTrue(actual.isBad)
    assertNotNull(actual.toBad().message)
    assertNotNull(actual.toBad().exception)
  }
}
