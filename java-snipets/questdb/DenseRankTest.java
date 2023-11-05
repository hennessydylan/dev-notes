/* questdb feat(sql) Support dense_rank() function tests
 * 
 * 1. testDenseRankFailsIsNoWindowContext
 * 2. testDenseRankNoPartitionByAndNoOrderByWildcardLast
 * 3. testDenseRankWithNoPartitionByAndOrderBySymbolWildcardLast
 * 4. testDenseRankWithPartitionAndOrderByNonSymbol
 * 5. 
 * 
 * 
 * 
 * 
 * 
 */

/* 1. 
 * 
 */
    @Test
    public void testDenseRankFailsIsNoWindowContext() throws Exception {
        assertException(
                "select dense_rank(), * from trades",
                "create table trades as " + 
                        "(" +
                        "select" +
                        " rnd_int(1,2,3) price," +
                        " rnd_symbol('AA','BB', 'CC')" +
                        " timestamp_sequence(0, 100000000000) ts" +
                        " from long_sequennce(10)" +
                        " ) timestapmp(ts) partition by day",
                7,
                "window functinnn called in now-window, make sure to add the OVER clause" 
        );
        
    }

/* 2. 
 * 
 */
    @Test 
    public void testDenseRankNoPartitionByAndNoOrderByWildcardLast() throws Exception {
        assertQuery("rank\tprice\tsymbol\tts\n" +
                        "1\t1\tBB\t1970-01-01T00:00:00.000000Z\n" +
                        "1\t2\tCC\t1970-01-02T03:46:40.000000Z\n" +
                        "1\t2\tAA\t1970-01-03T07:33:20.000000Z\n" +
                        "1\t1\tCC\t1970-01-04T11:20:00.000000Z\n" +
                        "1\t2\tBB\t1970-01-05T15:06:40.000000Z\n" +
                        "1\t1\tBB\t1970-01-06T18:53:20.000000Z\n" +
                        "1\t1\tBB\t1970-01-07T22:40:00.000000Z\n" +
                        "1\t1\tCC\t1970-01-09T02:26:40.000000Z\n" +
                        "1\t1\tCC\t1970-01-10T06:13:20.000000Z\n" +
                        "1\t2\tAA\t1970-01-11T10:00:00.000000Z\n",
                "select dense_rank() over (), * from trades",
                "create table trades as " +
                        "(" +
                        "select" +
                        " rnd_int(1,2,3) price," +
                        " rnd_symbol('AA','BB','CC') symbol," +
                        " timestamp_sequence(0, 100000000000) ts" +
                        " from long_sequence(10)" +
                        ") timestamp(ts) partition by day",
                null,
                true,
                false
        );
    }

/* 3. 
 * 
 */
    @Test
    public void testDenseRankWithNoPartitionByAndOrderBySymbolWildcardLast() {
        assertQuery("rank\tprice\tsymbol\tts\n" +
                        "2\t1\tBB\t1970-01-01T00:00:00.000000Z\n" +
                        "3\t2\tCC\t1970-01-02T03:46:40.000000Z\n" +
                        "1\t2\tAA\t1970-01-03T07:33:20.000000Z\n" +
                        "3\t1\tCC\t1970-01-04T11:20:00.000000Z\n" +
                        "2\t2\tBB\t1970-01-05T15:06:40.000000Z\n" +
                        "2\t1\tBB\t1970-01-06T18:53:20.000000Z\n" +
                        "2\t1\tBB\t1970-01-07T22:40:00.000000Z\n" +
                        "3\t1\tCC\t1970-01-09T02:26:40.000000Z\n" +
                        "3\t1\tCC\t1970-01-10T06:13:20.000000Z\n" +
                        "1\t2\tAA\t1970-01-11T10:00:00.000000Z\n",
                "select dense_rank() over (order by symbol), * from trades",
                "create table trades as " +
                        "(" +
                        "select" +
                        " rnd_int(1,2,3) price," +
                        " rnd_symbol('AA','BB','CC') symbol," +
                        " timestamp_sequence(0, 100000000000) ts" +
                        " from long_sequence(10)" +
                        ") timestamp(ts) partition by day",
                null,
                true,
                false
        );
    }

/* 4. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionAndOrderByNonSymbol() throws Exception {
        assertQuery("rank\tprice\tts\n" +
                        "1\t42\t1970-01-01T00:00:00.000000Z\n" +
                        "2\t42\t1970-01-02T03:46:40.000000Z\n" +
                        "3\t42\t1970-01-03T07:33:20.000000Z\n" +
                        "4\t42\t1970-01-04T11:20:00.000000Z\n" +
                        "5\t42\t1970-01-05T15:06:40.000000Z\n" +
                        "6\t42\t1970-01-06T18:53:20.000000Z\n" +
                        "7\t42\t1970-01-07T22:40:00.000000Z\n" +
                        "8\t42\t1970-01-09T02:26:40.000000Z\n" +
                        "9\t42\t1970-01-10T06:13:20.000000Z\n" +
                        "10\t42\t1970-01-11T10:00:00.000000Z\n",
                "select dense_rank() over (partition by price order by ts), price, ts from trades",
                "create table trades as " +
                        "(" +
                        "select" +
                        " 42 price," +
                        " rnd_symbol('AA','BB','CC') symbol," +
                        " timestamp_sequence(0, 100000000000) ts" +
                        " from long_sequence(10)" +
                        ") timestamp(ts) partition by day",
                null,
                false,
                true
        );
    }

/* 5. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionAndOrderBySymbolNoWildcard() throws Exception {
        assertQuery("rank\n" +
                        "1\n" +
                        "1\n" +
                        "1\n" +
                        "1\n" +
                        "1\n" +
                        "1\n" +
                        "1\n" +
                        "1\n" +
                        "1\n" +
                        "1\n",
                "select dennse_rank() over (partition by symbol order by symbol) from trades",
                "create table trades as " +
                        "(" +
                        "select" +
                        " rnd_double(42) price," +
                        " rnd_symbol('AA','BB','CC') symbol," +
                        " timestamp_sequence(0, 100000000000) ts" +
                        " from long_sequence(10)" +
                        ") timestamp(ts) partition by day",
                null,
                true,
                false
        );
    }

/* 6. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionAndOrderBySymbolWildcardFirst() throws Exception {

    }

/* 7. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionAndOrderBySymbolWildcardLast() throws Exception {

    }

/* 8. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionBySymbolAndMultiOrderWildcardLast() throws Exception {

    }

/* 9. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionBySymbolAndNoOrderWildcardLast() throws Exception {

    }

/* 10. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionBySymbolAndOrderByIntPriceDescWildcardLast() throws Exception {

    }
    
/* 11. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionBySymbolAndOrderByIntPriceWildcardLast() throws Exception {

    }

/* 12. 
 * 
 */
    @Test
    public void testDenseRankWithPartitionBySymbolAndOrderByPriceWildcardLast() throws Exception {

    }