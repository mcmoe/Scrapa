package h2.table;

import org.junit.Test;

import static h2.table.commons.ColumnsMeta.DATA;
import static org.junit.Assert.assertEquals;

/**
 * Unit test the consistency of the columns meta data
 * Created by mcmoe on 4/29/2014.
 */
public class ColumnsMetaTest {

    @Test
    public void test_columns_meta_data_indeces() {
        assertEquals(DATA.TABLE_CAT        .index(),  1);
        assertEquals(DATA.TABLE_SCHEM      .index(),  2);
        assertEquals(DATA.TABLE_NAME       .index(),  3);
        assertEquals(DATA.COLUMN_NAME      .index(),  4);
        assertEquals(DATA.DATA_TYPE        .index(),  5);
        assertEquals(DATA.TYPE_NAME        .index(),  6);
        assertEquals(DATA.COLUMN_SIZE      .index(),  7);
        assertEquals(DATA.BUFFER_LENGTH    .index(),  8);
        assertEquals(DATA.DECIMAL_DIGITS   .index(),  9);
        assertEquals(DATA.NUM_PREC_RADIX   .index(), 10);
        assertEquals(DATA.NULLABLE         .index(), 11);
        assertEquals(DATA.REMARKS          .index(), 12);
        assertEquals(DATA.COLUMN_DEF       .index(), 13);
        assertEquals(DATA.SQL_DATA_TYPE    .index(), 14);
        assertEquals(DATA.SQL_DATETIME_SUB .index(), 15);
        assertEquals(DATA.CHAR_OCTET_LENGTH.index(), 16);
        assertEquals(DATA.ORDINAL_POSITION .index(), 17);
        assertEquals(DATA.IS_NULLABLE      .index(), 18);
        assertEquals(DATA.SCOPE_CATALOG    .index(), 19);
        assertEquals(DATA.SCOPE_SCHEMA     .index(), 20);
        assertEquals(DATA.SCOPE_TABLE      .index(), 21);
        assertEquals(DATA.SOURCE_DATA_TYPE .index(), 22);
        assertEquals(DATA.IS_AUTOINCREMENT .index(), 23);
        assertEquals(DATA.SCOPE_CATLOG     .index(), 24);
    }
}
