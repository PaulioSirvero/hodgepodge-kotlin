
/**
 * Thrown when a HTTP service encounters an error
 *
 * @property[status] HTTP status code
 * @property[msg] Error message
 * @param[cause] Cause of the error
 */
class HttpError(
  val status: Int,
  val msg: String,
  cause: Exception? = null
): Exception(msg, cause) {

  init {
    when {
      status < 400 -> throw HttpError.bug(
        "HTTP error status codes must be 400 or greater")
      status >= 600 -> throw HttpError.bug(
        "HTTP error status codes must be less than 600")
    }
  }

  /**
   * Represents a Lean reply for an erroneous call to an endpoint.
   * It is serialised into the target format directly
   *
   * @property[message] Human readable message describing the response
   * @property[lean] Version of the lean format
   */
  data class ErrorReply(
    val message: String
  ) {
    val lean: Int = 1
  }

  /**
   * Converts the exception into an object that can be sent
   * back to the client
   */
  fun toReply() = ErrorReply(
    when (status) {
      500 -> DEFAULT_ERROR_MESSAGE
      else -> msg
    }
  )

  companion object {

    /** Default service error message for when one is not provided */
    const val DEFAULT_ERROR_MESSAGE = "Internal service error"

    /**
     * Returns a client error with a 400 status when a
     * client supplied parameter is missing or invalid
     *
     * @param[msg] Response message
     */
    fun badRequest(msg: String) = HttpError(400, msg)

    /**
     * Returns a client error with a 401 status when a
     * client attempts to access a resource before they
     * have authenticated or there authentication details
     * were invalid
     *
     * @param[msg] Response message
     */
    fun badLogin(msg: String) = HttpError(401, msg)

    /**
     * Returns a client error with a 403 status when a
     * client attempts to access a resource they are not
     * authorised to access
     *
     * @param[msg] Response message
     */
    fun notAllowed(msg: String) = HttpError(403, msg)

    /**
     * Returns a client error with a 404 'not found' status
     *
     * @param[msg] Custom response message
     */
    fun notFound(msg: String = "No such resource") = HttpError(404, msg)

    /**
     * Returns a service error with a 500 status when a bug
     * is detected
     *
     * @param[msg] Response message
     * @param[cause] Cause of the error
     */
    fun bug(
      msg: String,
      cause: Exception? = null
    ) = HttpError(500, msg, cause)

    /**
     * Returns a service error with a 500 status when a bad
     * service configuration is detected
     *
     * @param[msg] Response message
     * @param[cause] Cause of the error
     */
    fun badConfig(
      msg: String,
      cause: Exception? = null
    ) = HttpError(500, msg, cause)

    /**
     * Returns a service error with a 501 status when a
     * service feature has been declared but not
     * implemented
     *
     * @param[msg] Response message
     * @param[cause] Cause of the error
     */
    fun notImplemented(
      msg: String,
      cause: Exception? = null
    ) = HttpError(501, msg, cause)

    /**
     * Returns a service error with a 503 status when a
     * service feature is unavailable due to a known
     * issue or maintenance
     *
     * @param[msg] Response message
     * @param[cause] Cause of the error
     */
    fun featureUnavailable(
      msg: String,
      cause: Exception? = null
    ) = HttpError(503, msg, cause)
  }
}