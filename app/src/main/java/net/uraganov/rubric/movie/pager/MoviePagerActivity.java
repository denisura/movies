package net.uraganov.rubric.movie.pager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import net.uraganov.rubric.SingleFragmentActivity;

public class MoviePagerActivity extends SingleFragmentActivity  {

    static final String LOG_TAG = MoviePagerActivity.class.getCanonicalName();

    private static final String EXTRA_MOVIE_ID =
            "net.uraganov.rubric.movie.detail.movie_id";

    public static Intent newIntent(Context packageContext, int positionId) {
        Intent intent = new Intent(packageContext, MoviePagerActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, positionId);
        Log.e(LOG_TAG, "createFragment EXTRA_MOVIE_ID " + positionId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int positionId = (int) getIntent().getSerializableExtra(EXTRA_MOVIE_ID);
        Log.e(LOG_TAG,"createFragment EXTRA_MOVIE_ID "+ positionId);
        return MoviePagerFragment.newInstance(positionId);
    }
}
