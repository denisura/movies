package net.uraganov.rubric.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {

    // content://net.uraganov.rubric/movie"
    private static final Uri TEST_MOVIE_DIR = RubricContract.MovieEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = RubricProvider.buildUriMatcher();

        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), RubricProvider.MOVIE);
    }
}
