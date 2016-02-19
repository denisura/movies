package net.uraganov.rubric.movie.detail;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.uraganov.rubric.R;
import net.uraganov.rubric.database.FavoriteColumns;
import net.uraganov.rubric.database.RubricProvider;
import net.uraganov.rubric.model.MovieDetails;
import net.uraganov.rubric.model.Youtube;
import net.uraganov.rubric.movie.detail.review.MovieReviewCollectionAdapter;
import net.uraganov.rubric.movie.detail.trailer.MovieTrailerCollectionAdapter;
import net.uraganov.rubric.settings.SettingsActivity;
import net.uraganov.rubric.sync.RubricSyncAdapter;
import net.uraganov.rubric.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private static final String ARG_MOVIE_ID = "movie_id";
    private static final int DETAIL_LOADER = 0;
    private static final int TRAILER_LOADER = 1;
    private static final int REVIEW_LOADER = 2;


    private MovieTrailerCollectionAdapter mAdapter;
    private MovieReviewCollectionAdapter mReviewAdapter;
    private String mShareTrailerStr;
    ShareActionProvider mShareActionProvider;

    @Bind(R.id.detail_poster_imageview)
    ImageView mPosterView;

    @Bind(R.id.detail_original_title)
    TextView mOriginalTitle;

    @Bind(R.id.detail_overview)
    TextView mDetailOverview;


    @Bind(R.id.detail_vote_average)
    TextView mDetailVoteAverage;

    @Bind(R.id.detail_favorite)
    ImageView mDetailFavorite;


    @Bind(R.id.release_date)
    TextView mReleaseDate;

    @Bind(R.id.fragment_movie_collection_recycler_view)
    RecyclerView mMovieTrailerCollectionRecyclerView;


    @Bind(R.id.fragment_movie_review_collection_recycler_view)
    RecyclerView mMovieReviewCollectionRecyclerView;


    MovieDetails mMovieDetails;

    public static MovieDetailFragment newInstance(long movieId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE_ID, movieId);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
    }

    @OnClick(R.id.detail_favorite)
    void onToggleFavorite() {
        Log.e(LOG_TAG, "onToggleFavorite ");
        final Context appContext = getActivity().getApplicationContext();

        if (mMovieDetails.isFavorite()) {
            Log.e(LOG_TAG, "onToggleFavorite to not favorite. Movie ID" + mMovieDetails.getId());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    appContext.getContentResolver()
                            .delete(
                                    RubricProvider.Favorite.withId(mMovieDetails.getId()),
                                    null,
                                    null);
                }
            }).start();
        } else {
            Log.e(LOG_TAG, "onToggleFavorite to favorite. Movie ID" + mMovieDetails.getId());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentValues cv = new ContentValues();
                    cv.put(FavoriteColumns.MOVIE_ID, mMovieDetails.getId());
                    appContext.getContentResolver()
                            .insert(RubricProvider.Favorite.withId(mMovieDetails.getId()),
                                    cv);
                }

            }).start();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        long movieId = getArguments().getLong(ARG_MOVIE_ID, 0);
        RubricSyncAdapter.syncNowMovieDetails(getActivity(), movieId);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mShareTrailerStr);
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        final LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMovieTrailerCollectionRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MovieTrailerCollectionAdapter(getContext(), null);
        mMovieTrailerCollectionRecyclerView.setAdapter(mAdapter);


        final LinearLayoutManager layoutReviewManager = new org.solovyev.android.views.llm.LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMovieReviewCollectionRecyclerView.setLayoutManager(layoutReviewManager);
        mReviewAdapter = new MovieReviewCollectionAdapter(getContext(), null);
        mMovieReviewCollectionRecyclerView.setAdapter(mReviewAdapter);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long movieId = getArguments().getLong(ARG_MOVIE_ID, 0);

        Log.v(LOG_TAG, "[movieId] " + movieId + " In onCreateLoader #" + id);
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        switch (id) {
            case DETAIL_LOADER:
                Uri movieCollectionUri = RubricProvider.Movies.withId(movieId);

                return new CursorLoader(getActivity(),
                        movieCollectionUri,
                        RubricProvider.Movies.PROJECTION,
                        null,
                        null,
                        null);
            case TRAILER_LOADER:
                Uri movieTrailerCollectionUri = RubricProvider.Trailers.withId(movieId);

                Log.v(LOG_TAG, "In onCreateLoader movieTrailerCollectionUri: " + movieTrailerCollectionUri);

                return new CursorLoader(getActivity(),
                        movieTrailerCollectionUri,
                        null,
                        null,
                        null,
                        null);
            case REVIEW_LOADER:
                Uri movieReviewCollectionUri = RubricProvider.Reviews.withId(movieId);

                Log.v(LOG_TAG, "In onCreateLoader movieReviewCollectionUri: " + movieReviewCollectionUri);

                return new CursorLoader(getActivity(),
                        movieReviewCollectionUri,
                        null,
                        null,
                        null,
                        null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        long movieId = getArguments().getLong(ARG_MOVIE_ID, 0);
        Log.v(LOG_TAG, "[movieId] " + movieId + " In onLoadFinished loader ID #" + loader.getId());
        switch (loader.getId()) {
            case DETAIL_LOADER:
                showDetails(data);
                break;
            case TRAILER_LOADER:
                showTrailers(data);
                break;
            case REVIEW_LOADER:
                showReviews(data);
                break;

            default:
                Log.v(LOG_TAG, "[movieId] " + movieId + " In onLoadFinished Unknown loader id loader ID #" + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        long movieId = getArguments().getLong(ARG_MOVIE_ID, 0);
        Log.v(LOG_TAG, "[movieId] " + movieId + " In onLoaderReset loader ID #" + loader.getId());

        switch (loader.getId()) {

            case TRAILER_LOADER:
                if (mAdapter != null) {
                    mAdapter.changeCursor(null);
                }
                break;
            case REVIEW_LOADER:
                if (mReviewAdapter != null) {
                    mReviewAdapter.changeCursor(null);
                }
                break;
        }
    }


    public void showDetails(Cursor data) {

        long movieId = getArguments().getLong(ARG_MOVIE_ID, 0);
        Log.v(LOG_TAG, "[movieId] " + movieId + " In showDetails");


        if (!data.moveToFirst()) {
            return;
        }

        mMovieDetails = new MovieDetails(data);

        // original title
        mOriginalTitle.setText(mMovieDetails.getOriginalTitle());


        // movie poster image thumbnail
        Picasso.with(getActivity()) //
                .load(Utilities.getMoviePosterUrl(mMovieDetails.getPosterPath(), "w154"))
                .fit() //
                .tag(getActivity()) //
                .into(mPosterView);

        // A plot synopsis (called overview in the api)
        mDetailOverview.setText(mMovieDetails.getOverview());

        // user rating (called vote_average in the api)
        mDetailVoteAverage.setText(getActivity().getString(R.string.movie_detail_format_rating, mMovieDetails.getVoteAverage()));

        // release date
        mReleaseDate.setText(mMovieDetails.getReleaseDate("yyyy"));

        if (mMovieDetails.isFavorite()) {
            mDetailFavorite.setImageResource(android.R.drawable.star_big_on);
            mDetailFavorite.setContentDescription(getString(R.string.favorite_off));
        } else {
            mDetailFavorite.setImageResource(android.R.drawable.star_big_off);
            mDetailFavorite.setContentDescription(getString(R.string.favorite_on));
        }
    }

    public void showTrailers(Cursor data) {
        if (!data.moveToFirst()) {
            //TODO put no trailers message
            return;
        }
        Youtube youtube = new Youtube(data);
        mShareTrailerStr = getActivity().getString(R.string.format_share_trailer, youtube.getSource());

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
        mAdapter.swapCursor(data);
    }


    public void showReviews(Cursor data) {
        if (!data.moveToFirst()) {
            //TODO put no review message
            return;
        }
        mReviewAdapter.swapCursor(data);
    }
}
