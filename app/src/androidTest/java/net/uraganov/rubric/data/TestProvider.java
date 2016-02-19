package net.uraganov.rubric.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import static net.uraganov.rubric.data.RubricContract.MovieEntry;

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
   This helper function deletes all records from both database tables using the ContentProvider.
   It also queries the ContentProvider to make sure that the database has been successfully
   deleted, so it cannot be used until the Query and Delete functions have been written
   in the ContentProvider.

   Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
   the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
This helper function deletes all records from both database tables using the database
functions only.  This is designed to be used to reset the state of the database until the
delete functionality is available in the ContentProvider.
*/
    public void deleteAllRecordsFromDB() {
        RubricDbHelper dbHelper = new RubricDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
you have implemented delete functionality there.
*/
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertMovieValues() {

        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues entryValues = new ContentValues();

            entryValues.put(MovieEntry.COLUMN_ADULT, "false");
            entryValues.put(MovieEntry.COLUMN_BACKDROP_PATH, "blahBlaHbLAh" + i + ".jpg");
            entryValues.put(MovieEntry.COLUMN_MOVIE_ID, 1000 + i);
            entryValues.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
            entryValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, "Star Wars: " + (i + 1));
            entryValues.put(MovieEntry.COLUMN_OVERVIEW, "Star Wars: Episode " + (i + 1) + " is awesome");
            entryValues.put(MovieEntry.COLUMN_RELEASE_DATE, "2015-05-2" + i);
            entryValues.put(MovieEntry.COLUMN_POSTER_PATH, "poster" + i);
            entryValues.put(MovieEntry.COLUMN_POPULARITY, 100 + i);
            entryValues.put(MovieEntry.COLUMN_TITLE, "Star Wars: Episode " + (i + 1));
            entryValues.put(MovieEntry.COLUMN_VIDEO, i);
            entryValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, "7." + i + 1);
            entryValues.put(MovieEntry.COLUMN_VOTE_COUNT, i);
            returnContentValues[i] = entryValues;
        }
        return returnContentValues;
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // RubricProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                RubricProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: RubricProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + RubricContract.CONTENT_AUTHORITY,
                    providerInfo.authority, RubricContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: RubricProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    public void testGetType() {

        // content://com.example.android.sunshine.app/movie/
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        Long testMovieId = 94074L;
        // content://net.uraganov.rubric/movie/94074
        type = mContext.getContentResolver().getType(MovieEntry.buildMovieUri(testMovieId));
        assertEquals("Error: the MovieEntry CONTENT_URI with movie Id" +
                        " should return MovieEntry.CONTENT_ITEM_TYPE",
                MovieEntry.CONTENT_ITEM_TYPE, type);

        // content://net.uraganov.rubric/movie/popular
        type = mContext.getContentResolver().getType(
                MovieEntry.buildPopularMoviesUri());
        assertEquals("Error: the MovieEntry CONTENT_URI with popular should return WeatherEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        // content://net.uraganov.rubric/movie/hirated
        type = mContext.getContentResolver().getType(
                MovieEntry.buildHiratedMoviesUri());
        assertEquals("Error: the MovieEntry CONTENT_URI with hirated return WeatherEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);
    }


    public void testBasicMovieQuery() {
        // insert our test records into the database
        RubricDbHelper dbHelper = new RubricDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = TestUtilities.createMovieValues();

        long movieRowId = db.insert(MovieEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to Insert MovieEntry into the Database", movieRowId != -1);

        db.close();

        Uri content_Uri = MovieEntry.buildMovieUri(movieValues.getAsLong(MovieEntry.COLUMN_MOVIE_ID));

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                content_Uri,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, movieValues);
    }

    public void testInsertReadProvider() {
        ContentValues movieValues = TestUtilities.createMovieValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, movieValues);

        // Did our content observer get called?   If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.buildMovieUri(movieValues.getAsLong(MovieEntry.COLUMN_MOVIE_ID)),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.",
                cursor, movieValues);


    }


    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, movieObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        movieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);
    }


    public void testUpdateLocation() {

        ContentValues movieValues = TestUtilities.createMovieValues();

        Uri movieUri = mContext.getContentResolver().
                insert(MovieEntry.CONTENT_URI, movieValues);
        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New row id: " + movieRowId);

        ContentValues updatedValues = new ContentValues(movieValues);
        updatedValues.put(MovieEntry.COLUMN_MOVIE_ID, movieRowId);
        updatedValues.put(MovieEntry.COLUMN_TITLE, "Matrix");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor movieCursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieEntry.CONTENT_URI, updatedValues, MovieEntry.COLUMN_MOVIE_ID + "= ?",
                new String[]{Long.toString(movieRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,   // projection
                MovieEntry.COLUMN_MOVIE_ID + " = " + movieRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateMovie.  Error validating movie entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    public void testBulkInsert() {

        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, movieObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        movieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // values for "order by" clause
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for (int i = BULK_INSERT_RECORDS_TO_INSERT - 1; i > 0; i--, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating MovieEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
