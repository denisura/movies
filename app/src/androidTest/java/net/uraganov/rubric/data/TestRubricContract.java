package net.uraganov.rubric.data;

import android.net.Uri;
import android.test.AndroidTestCase;

public class TestRubricContract extends AndroidTestCase {
    private static final long TEST_MOVIE = 123;


    public void testBuildMovieUri() {
        Uri movieUri = RubricContract.MovieEntry.buildMovieUri(TEST_MOVIE);
        assertNotNull("Error: Null Uri returned.  buildMovieUri must be implemented in " +
                        "RubricContract.",
                movieUri);
        assertEquals("Error: Movie Id not properly appended to the end of the Uri",
                TEST_MOVIE, Long.parseLong(movieUri.getLastPathSegment()));
        assertEquals("Error: Movie Uri doesn't match our expected result",
                movieUri.toString(),
                "content://net.uraganov.rubric/movie/123");
    }
}
