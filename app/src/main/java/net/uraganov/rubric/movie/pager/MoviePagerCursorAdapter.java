package net.uraganov.rubric.movie.pager;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.uraganov.rubric.database.MovieColumns;
import net.uraganov.rubric.movie.detail.MovieDetailFragment;

/**
 * A simple pager adapter.
 */
public class MoviePagerCursorAdapter extends FragmentStatePagerAdapter {

    private Cursor mCursor;

    public MoviePagerCursorAdapter(FragmentManager fm, Cursor c) {
        super(fm);
        mCursor = c;
    }

    @Override
    public Fragment getItem(int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            long movieId = mCursor.getLong(mCursor.getColumnIndex(MovieColumns.MOVIE_ID));
            return MovieDetailFragment.newInstance(movieId);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}