/*
 * Registers a class as the target for a row mapper, only needs to be
 * done once for each Jdbi instance
 *
 * jdbi.registerRowMapper(
 *    ConstructorMapper.factory(SQLitePreProcessing::class.java)
 * )
 */

/*
 * Tries to get all rows from a specific database table
 *
 * val r = try {
 *   jdbi.withHandle<List<Thing>, Exception> { h ->
 *     h.createQuery("SELECT id, prop_one, prop_two FROM " <SCHEMA> "." <TABLE>)
 *       .mapTo(Thing::class.java)
 *       .toList()
 *   }.realResult()
 * } catch (ex: Exception) {
 *   AnyResult.bad(
 *     "Failed to retrieve " <thing name> " from database",
 *     ex
 *   )
 * }
 */

/*
 * Tries to get single row from the database
 *
 * val id = 123
 * val r = try {
 *   jdbi.withHandle<Thing, Exception> { h ->
 *     h.createQuery("SELECT id, prop_one, prop_two FROM " <SCHEMA> "." <TABLE> " WHERE id = :bind_id")
 *       .bind("bind_id", id)
 *       .mapTo(Thing::class.java)
 *       .firstOrNull()
 *   }.realResult()
 * } catch (ex: Exception) {
 *   AnyResult.bad(
 *     "Failed to retrieve " <thing name> " from database",
 *     ex
 *   )
 * }
 */