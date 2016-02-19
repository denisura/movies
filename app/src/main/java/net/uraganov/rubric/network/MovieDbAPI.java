package net.uraganov.rubric.network;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import net.uraganov.rubric.BuildConfig;
import net.uraganov.rubric.R;
import net.uraganov.rubric.database.RubricProvider;
import net.uraganov.rubric.model.MovieCollection;
import net.uraganov.rubric.model.MovieDetails;
import net.uraganov.rubric.model.MovieReview;
import net.uraganov.rubric.model.MovieReviewCollection;
import net.uraganov.rubric.model.MovieTrailerCollection;
import net.uraganov.rubric.model.Youtube;

import java.util.Iterator;
import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class MovieDbAPI {
    private final String LOG_TAG = MovieDbAPI.class.getSimpleName();

    public static final String BASE_URL = "http://api.themoviedb.org";
    private static final String SORT_BY_PARAM = "sort_by"; //popularity.desc
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;
    private final Context mContext;
    private final EndpointInterface mApiService;

    public MovieDbAPI(Context context) {
        mContext = context;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        // add logging as last interceptor
        httpClient.interceptors().add(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        mApiService = retrofit.create(EndpointInterface.class);
    }

    public void fetchPopularMovieCollection() {
        fetchMovieCollection(mContext.getString(R.string.pref_sortby_popularity));
    }

    public void fetchHighRatedMovieCollection() {
        fetchMovieCollection(mContext.getString(R.string.pref_sortby_hirated));
    }

    public Void fetchMovieCollection(String sortBy) {

        Call<MovieCollection> call = mApiService.getCollection(API_KEY, sortBy);
        call.enqueue(new Callback<MovieCollection>() {
            @Override
            public void onResponse(Response<MovieCollection> response, Retrofit retrofit) {
                int statusCode = response.code();
                Log.e(LOG_TAG, "statusCode #" + statusCode);

                MovieCollection movieCollection = response.body();

                Log.e(LOG_TAG, "Page #" + movieCollection.getPage());
                Log.e(LOG_TAG, "Total Pages " + movieCollection.getTotalPages());
                Log.e(LOG_TAG, "Total Results " + movieCollection.getTotalResults());

                Vector<ContentValues> cVVector = new Vector<ContentValues>(movieCollection.getResults().size());
                for (Iterator<MovieDetails> movieDetails = movieCollection.getResults().iterator(); movieDetails.hasNext(); ) {
                    MovieDetails movie = movieDetails.next();
                    Log.e(LOG_TAG, "Movie Id #" + movie.getId());
                    cVVector.add(movie.toContentValues());
                }
                int inserted = 0;
                // add to database
                if (cVVector.size() > 0) {
                    // call bulkInsert to add the movieEntries to the database here
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(RubricProvider.Movies.CONTENT_URI, cvArray);
                }

                Log.d(LOG_TAG, "fetchMovieItems Complete. " + inserted + " Inserted");
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                //TODO log unable fetch movies
                Log.e(LOG_TAG, "onFailure", t);
            }
        });
        return null;
    }


    public Void fetchMovieTrailers(long movieId) {

        Call<MovieTrailerCollection> call = mApiService.getMovieTrailers(movieId, API_KEY);
        call.enqueue(new Callback<MovieTrailerCollection>() {
            @Override
            public void onResponse(Response<MovieTrailerCollection> response, Retrofit retrofit) {
                int statusCode = response.code();
                Log.e(LOG_TAG, "fetchMovieTrailers statusCode #" + statusCode);

                MovieTrailerCollection movieTrailerCollection = response.body();
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movieTrailerCollection.getYoutube().size());
                for (Iterator<Youtube> youtubeTrailers = movieTrailerCollection.getYoutube().iterator(); youtubeTrailers.hasNext(); ) {
                    Youtube youtube = youtubeTrailers.next();
                    cVVector.add(youtube.toContentValues(movieTrailerCollection.getId()));
                }

                //clean prev set of trailers
                mContext.getContentResolver().delete(RubricProvider.Trailers.withId(movieTrailerCollection.getId()), null, null);

                //TODO add quicktime videos

                int inserted = 0;
                // add to database
                if (cVVector.size() > 0) {
                    // call bulkInsert to add the movieEntries to the database here
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(RubricProvider.Trailers.withId(movieTrailerCollection.getId()), cvArray);
                }
                Log.d(LOG_TAG, "fetchMovieTrailers Complete. Inserted " + inserted);
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                //TODO log unable fetch movie trailers
                Log.e(LOG_TAG, "onFailure", t);
            }
        });
        return null;
    }

    public Void fetchMovieReviews(long movieId) {

        Call<MovieReviewCollection> call = mApiService.getMovieReviews(movieId, API_KEY);
        call.enqueue(new Callback<MovieReviewCollection>() {
            @Override
            public void onResponse(Response<MovieReviewCollection> response, Retrofit retrofit) {
                int statusCode = response.code();
                Log.e(LOG_TAG, "fetchMovieTrailers statusCode #" + statusCode);

                MovieReviewCollection movieReviewCollection = response.body();
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movieReviewCollection.getReviews().size());
                for (Iterator<MovieReview> movieReviewIterator =movieReviewCollection.getReviews().iterator(); movieReviewIterator.hasNext(); ) {
                    MovieReview movieReview = movieReviewIterator.next();
                    cVVector.add(movieReview.toContentValues(movieReviewCollection.getId()));
                }


                int inserted = 0;
                // add to database
                if (cVVector.size() > 0) {
                    // call bulkInsert to add the movieEntries to the database here
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(RubricProvider.Reviews.withId(movieReviewCollection.getId()), cvArray);
                }
                Log.d(LOG_TAG, "fetchMovieReviews Complete. Inserted " + inserted);
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                //TODO log unable fetch movie trailers
                Log.e(LOG_TAG, "onFailure", t);
            }
        });
        return null;
    }

    /**
     * Retrofit Interface
     */
    public interface EndpointInterface {

        @GET("3/discover/movie")
        Call<MovieCollection> getCollection(@Query(API_KEY_PARAM) String apiKey, @Query(SORT_BY_PARAM) String sortBy);


        @GET("3/movie/{movieId}/trailers")
        Call<MovieTrailerCollection> getMovieTrailers(@Path("movieId") long movieId, @Query(API_KEY_PARAM) String apiKey);

        @GET("3/movie/{movieId}/reviews")
        Call<MovieReviewCollection> getMovieReviews(@Path("movieId") long movieId, @Query(API_KEY_PARAM) String apiKey);
    }

}
