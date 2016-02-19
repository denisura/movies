package net.uraganov.rubric.movie.collection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.uraganov.rubric.R;
import net.uraganov.rubric.database.RubricProvider;
import net.uraganov.rubric.settings.SettingsActivity;
import net.uraganov.rubric.sync.RubricSyncAdapter;
import net.uraganov.rubric.utils.Utilities;

import butterknife.ButterKnife;

public class MovieCollectionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String LOG_TAG = MovieCollectionFragment.class.getCanonicalName();

    private static final int MOVIE_COLLECTION_LOADER = 0;

    private MovieCollectionAdapter mAdapter;
    private RecyclerView mMovieCollectionRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;

    public static MovieCollectionFragment newInstance() {
        return new MovieCollectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(LOG_TAG, "onStart");
        RubricSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_collection, container, false);
        mMovieCollectionRecyclerView = (RecyclerView) view
                .findViewById(R.id.fragment_movie_collection_recycler_view);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        }
        mMovieCollectionRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieCollectionAdapter(getContext(), null);
        mMovieCollectionRecyclerView.setAdapter(mAdapter);
        mMovieCollectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onActivityCreated");
        getLoaderManager().initLoader(MOVIE_COLLECTION_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "onCreateLoader");
        return new CursorLoader(getActivity(),
                Utilities.getMovieCollectionUri(getActivity()),
                RubricProvider.Movies.PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

//        if (mAdapter == null) {
//            mAdapter = new MovieCollectionAdapter(getActivity(), null);
//            mMovieCollectionRecyclerView.setAdapter(mAdapter);
//            mMovieCollectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        } else {
//            mAdapter.changeCursor(data);
//        }

        Log.e(LOG_TAG, "onLoadFinished");

        mAdapter.swapCursor(data);
        SharedPreferences collectionStatePref = getActivity().getSharedPreferences(getActivity().getString(R.string.pref_collection_state), Context.MODE_PRIVATE);
        int collectionPosition = collectionStatePref.getInt(getActivity().getString(R.string.pref_collection_state_position), 0);
        if ((collectionPosition & 1) != 0) {
            collectionPosition--;
        }
        mMovieCollectionRecyclerView.getLayoutManager().scrollToPosition(collectionPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(LOG_TAG, "onLoaderReset");
        if (mAdapter != null) {
            mAdapter.changeCursor(null);
        }
    }
}