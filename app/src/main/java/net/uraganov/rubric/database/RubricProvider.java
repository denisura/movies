package net.uraganov.rubric.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

@ContentProvider(authority = RubricProvider.AUTHORITY,
        database = RubricDatabase.class,
        packageName = "net.uraganov.rubric.provider")
public class RubricProvider {

    private RubricProvider() {
    }

    public static final String AUTHORITY = "net.uraganov.rubric";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";
        String POPULAR = "movies/popular";
        String HIGHRATED = "movies/highrated";
        String FAVORITE = "movies/favorite";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }

        Log.e("MovieCollectionFragment", builder.build().toString());
        return builder.build();
    }

    @TableEndpoint(table = RubricDatabase.Tables.MOVIES)
    public static class Movies {

        //projection for ContentResolver.query
        public static String[] PROJECTION = new String[]{
                RubricDatabase.Tables.MOVIES + "." + MovieColumns.MOVIE_ID,
                MovieColumns.COLUMN_ADULT,
                MovieColumns.COLUMN_BACKDROP_PATH,
                MovieColumns.COLUMN_ORIGINAL_LANGUAGE,
                MovieColumns.COLUMN_ORIGINAL_TITLE,
                MovieColumns.COLUMN_OVERVIEW,
                MovieColumns.COLUMN_RELEASE_DATE,
                MovieColumns.COLUMN_POSTER_PATH,
                MovieColumns.COLUMN_POPULARITY,
                MovieColumns.COLUMN_TITLE,
                MovieColumns.COLUMN_VIDEO,
                MovieColumns.COLUMN_VOTE_AVERAGE,
                MovieColumns.COLUMN_VOTE_COUNT,
                MovieColumns.COLUMN_IS_FAVORITE};


        @MapColumns
        public static Map<String, String> mapColumns() {
            Map<String, String> map = new HashMap<>(1);
            map.put(MovieColumns.COLUMN_IS_FAVORITE, "ifnull(" + RubricDatabase.Tables.FAVORITE + "." + FavoriteColumns.MOVIE_ID + ",0)");
            return map;
        }

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movies",
                defaultSort = MovieColumns.MOVIE_ID + " DESC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @ContentUri(
                path = Path.POPULAR,
                type = "vnd.android.cursor.dir/movies",
                join = "LEFT JOIN " + RubricDatabase.Tables.FAVORITE +
                        " ON " + RubricDatabase.Tables.MOVIES + "." + MovieColumns.MOVIE_ID
                        + " = " + RubricDatabase.Tables.FAVORITE + "." + FavoriteColumns.MOVIE_ID,
                defaultSort = MovieColumns.COLUMN_POPULARITY + " DESC")
        public static final Uri POPULAR_CONTENT_URI = buildUri(Path.MOVIES, "popular");

        @ContentUri(
                path = Path.HIGHRATED,
                type = "vnd.android.cursor.dir/movies",
                join = "LEFT JOIN " + RubricDatabase.Tables.FAVORITE +
                        " ON " + RubricDatabase.Tables.MOVIES + "." + MovieColumns.MOVIE_ID
                        + " = " + RubricDatabase.Tables.FAVORITE + "." + FavoriteColumns.MOVIE_ID,
                defaultSort = MovieColumns.COLUMN_VOTE_AVERAGE + " DESC")
        public static final Uri HIGHRATED_CONTENT_URI = buildUri(Path.MOVIES, "highrated");

        @ContentUri(
                path = Path.FAVORITE,
                type = "vnd.android.cursor.dir/movies",
                join = "JOIN " + RubricDatabase.Tables.FAVORITE +
                        " ON " + RubricDatabase.Tables.MOVIES + "." + MovieColumns.MOVIE_ID
                        + " = " + RubricDatabase.Tables.FAVORITE + "." + FavoriteColumns.MOVIE_ID,

                defaultSort = MovieColumns.COLUMN_POPULARITY + " DESC")
        public static final Uri FAVORITE_CONTENT_URI = buildUri(Path.MOVIES, "favorite");


        @InexactContentUri(
                path = Path.MOVIES + "/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movies",
                join = "LEFT JOIN " + RubricDatabase.Tables.FAVORITE +
                        " ON " + RubricDatabase.Tables.MOVIES + "." + MovieColumns.MOVIE_ID
                        + " = " + RubricDatabase.Tables.FAVORITE + "." + FavoriteColumns.MOVIE_ID,
                whereColumn = RubricDatabase.Tables.MOVIES + "." + MovieColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }

        @NotifyBulkInsert(paths = Path.MOVIES)
        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
            return new Uri[]{
                    uri,
            };
        }
    }

    @TableEndpoint(table = RubricDatabase.Tables.FAVORITE)
    public static class Favorite {

        @InexactContentUri(
                path = Path.MOVIES + "/#/favorite",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movies",
                whereColumn = FavoriteColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id), "favorite");
        }
    }

    @TableEndpoint(table = RubricDatabase.Tables.TRAILERS)
    public static class Trailers {

        @InexactContentUri(
                path = Path.MOVIES + "/#/trailers",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.dir/trailers",
                whereColumn = FavoriteColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id), "trailers");
        }
    }

    @TableEndpoint(table = RubricDatabase.Tables.REVIEW)
    public static class Reviews {

        @InexactContentUri(
                path = Path.MOVIES + "/#/reviews",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.dir/reviews",
                whereColumn = FavoriteColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id), "reviews");
        }
    }

}
