package h2.table.commons;

/**
 * A table's columns meta data can be acquired via a Result Set
 * This can be done using connection.getMetaData().getColumns()
 *
 * This class provides such a Result Set's "meta data"
 * Created by mcmoe on 4/29/2014.
 */
public class ColumnsMeta {
    /* Gets the list of columns. The result set is sorted by TABLE_SCHEM, TABLE_NAME, and ORDINAL_POSITION. */

    public enum DATA {
        TABLE_CAT         (1), /* (String) table catalog                                         */
        TABLE_SCHEM       (2), /* (String) table schema                                          */
        TABLE_NAME        (3), /* (String) table name                                            */
        COLUMN_NAME       (4), /* (String) column name                                           */
        DATA_TYPE         (5), /* (short) data type       (see java.sql.Types)                   */
        TYPE_NAME         (6), /* (String) data type name ("INTEGER", "VARCHAR",...)             */
        COLUMN_SIZE       (7), /* (int) precision (values larger than 2 GB are returned as 2 GB) */
        BUFFER_LENGTH     (8), /* (int) unused                                                   */
        DECIMAL_DIGITS    (9), /* (int) scale (0 for INTEGER and VARCHAR)                        */
        NUM_PREC_RADIX   (10), /* (int) radix (always 10)                                        */
        NULLABLE         (11), /* (int) columnNoNulls or columnNullable                          */
        REMARKS          (12), /* (String) comment (always empty)                                */
        COLUMN_DEF       (13), /* (String) default value                                         */
        SQL_DATA_TYPE    (14), /* (int) unused                                                   */
        SQL_DATETIME_SUB (15), /* (int) unused                                                   */
        CHAR_OCTET_LENGTH(16), /* (int) unused                                                   */
        ORDINAL_POSITION (17), /* (int) the column index (1,2,...)                               */
        IS_NULLABLE      (18), /* (String) "NO" or "YES"                                         */
        SCOPE_CATALOG    (19), /* (String) always null                                           */
        SCOPE_SCHEMA     (20), /* (String) always null                                           */
        SCOPE_TABLE      (21), /* (String) always null                                           */
        SOURCE_DATA_TYPE (22), /* (short) null                                                   */
        IS_AUTOINCREMENT (23), /* (String) "NO" or "YES"                                         */
        SCOPE_CATLOG     (24); /* (String) always null
            (the typo is on purpose, for compatibility with the JDBC specification prior to 4.1) */

        private final int index;
        DATA(int index) {
            this.index  = index;
        }

        public int index() {
            return index;
        }
    }
}
