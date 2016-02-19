package net.uraganov.rubric.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import net.uraganov.rubric.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + valueCursor.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default movie values for your database tests.
     */
    static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(RubricContract.MovieEntry.COLUMN_ADULT, 1);
        movieValues.put(RubricContract.MovieEntry.COLUMN_BACKDROP_PATH, 1.1);
        movieValues.put(RubricContract.MovieEntry.COLUMN_MOVIE_ID, 1);
        movieValues.put(RubricContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, 1.3);
        movieValues.put(RubricContract.MovieEntry.COLUMN_ORIGINAL_TITLE, 75);
        movieValues.put(RubricContract.MovieEntry.COLUMN_OVERVIEW, 65);
        movieValues.put(RubricContract.MovieEntry.COLUMN_RELEASE_DATE, 1419033600L);
        movieValues.put(RubricContract.MovieEntry.COLUMN_POSTER_PATH, 5.5);
        movieValues.put(RubricContract.MovieEntry.COLUMN_POPULARITY, 321);
        movieValues.put(RubricContract.MovieEntry.COLUMN_TITLE, 321);
        movieValues.put(RubricContract.MovieEntry.COLUMN_VIDEO, 321);
        movieValues.put(RubricContract.MovieEntry.COLUMN_VOTE_AVERAGE, 4.8);
        movieValues.put(RubricContract.MovieEntry.COLUMN_VOTE_COUNT, 5.5);
        return movieValues;
    }


    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.
        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}