package net.uraganov.rubric.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

import net.uraganov.rubric.R;
import net.uraganov.rubric.database.RubricProvider;
import net.uraganov.rubric.model.MovieCollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {

    private static final String LOG_TAG = Utilities.class.getSimpleName();

    public static String getPreferredMovieCollectionOrder(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sortby_key),
                context.getString(R.string.pref_sortby_popularity));
    }

    public static Uri getPreferredMovieCollectionUri(Context context) {

        if (getPreferredMovieCollectionOrder(context)
                .equals(context.getString(R.string.pref_sortby_hirated))) {
            return RubricProvider.Movies.HIGHRATED_CONTENT_URI;
        }
        return RubricProvider.Movies.POPULAR_CONTENT_URI;
    }

    public static Uri getMovieCollectionUri(Context context) {

        Log.e(LOG_TAG,"getMovieCollectionUri Started");
        SharedPreferences collectionStatePref = context.getSharedPreferences(context.getString(R.string.pref_collection_state), Context.MODE_PRIVATE);
        String collectionType = collectionStatePref.getString(context.getString(R.string.pref_collection_state_type), MovieCollection.Types.POPULAR.name());

        if (collectionType.equals(MovieCollection.Types.POPULAR.name())) {
            Log.e(LOG_TAG,"getMovieCollectionUri POPULAR Completed");
            return RubricProvider.Movies.POPULAR_CONTENT_URI;
        }

        if (collectionType.equals(MovieCollection.Types.HIGHRATED.name())) {
            Log.e(LOG_TAG,"getMovieCollectionUri HIGHRATED Completed");
            return RubricProvider.Movies.HIGHRATED_CONTENT_URI;
        }

        if (collectionType.equals(MovieCollection.Types.FAVORITE.name())) {
            Log.e(LOG_TAG,"getMovieCollectionUri FAVORITE Completed");
            return RubricProvider.Movies.FAVORITE_CONTENT_URI;
        }
        Log.e(LOG_TAG,"getMovieCollectionUri Completed");
        return null;

    }

    public static int getMovieCollectionDrawerPosition(Context context) {
        SharedPreferences collectionStatePref = context.getSharedPreferences(context.getString(R.string.pref_collection_state), Context.MODE_PRIVATE);
        String collectionType = collectionStatePref.getString(context.getString(R.string.pref_collection_state_type), MovieCollection.Types.POPULAR.name());

        if (collectionType.equals(MovieCollection.Types.POPULAR.name())) {
            return 0;
        }

        if (collectionType.equals(MovieCollection.Types.HIGHRATED.name())) {
            return 1;
        }

        if (collectionType.equals(MovieCollection.Types.FAVORITE.name())) {
            return 2;
        }
        return 0;
    }



    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long date) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(date);
        int julianDay = Time.getJulianDay(date, time.gmtoff);
        return time.setJulianDay(julianDay);
    }


    public static String getMoviePosterUrl(String path, String size) {
        //return "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        String url = "http://image.tmdb.org/t/p/" + size + "/" + path;
        Log.d(LOG_TAG, "getPosterPath :" + url);
        return url;
    }

    public static Date parseDate(String date) {
        String[] dateFormats = new String[]{"yyyy-MM-dd", "EEE MMM dd HH:mm:ss z yyyy"};
        for (String dateFormat : dateFormats) {
            try {
                SimpleDateFormat f = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
                return f.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
