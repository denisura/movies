package net.uraganov.rubric.movie.pager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.uraganov.rubric.R;
import net.uraganov.rubric.database.RubricProvider;
import net.uraganov.rubric.utils.Utilities;

public class MoviePagerFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String LOG_TAG = MoviePagerFragment.class.getCanonicalName();

    private static final int PAGER_LOADER = 0;
    private static final String ARG_POSITION_ID = "position_id";
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private MoviePagerCursorAdapter adapter;

    public MoviePagerFragment() {
        // Required empty public constructor
    }

    public static MoviePagerFragment newInstance(int positionId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_POSITION_ID, positionId);
        MoviePagerFragment fragment = new MoviePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_item_pager, container, false);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        adapter = new MoviePagerCursorAdapter(getFragmentManager(), null);
        mPager.setAdapter(adapter);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e(LOG_TAG, "onPageSelected " + position);
                SharedPreferences collectionStatePref = getContext().getSharedPreferences(getString(R.string.pref_collection_state), Context.MODE_PRIVATE);
                collectionStatePref.edit().putInt(getString(R.string.pref_collection_state_position), position).apply();
            }
        };

        mPager.addOnPageChangeListener(onPageChangeListener);
        onPageChangeListener.onPageSelected(getArguments().getInt(ARG_POSITION_ID));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PAGER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                Utilities.getMovieCollectionUri(getActivity()),
                RubricProvider.Movies.PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        SharedPreferences collectionStatePref = getActivity().getSharedPreferences(getActivity().getString(R.string.pref_collection_state), Context.MODE_PRIVATE);
        int positionId = collectionStatePref.getInt(getActivity().getString(R.string.pref_collection_state_position), 0);
        mPager.setCurrentItem(positionId, false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}