package net.uraganov.rubric.movie.detail.review;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.uraganov.rubric.CursorRecyclerViewAdapter;
import net.uraganov.rubric.R;
import net.uraganov.rubric.model.MovieReview;

public class MovieReviewCollectionAdapter extends CursorRecyclerViewAdapter<MovieReviewCollectionItemViewHolder> {
    private static final String LOG_TAG = MovieReviewCollectionItemViewHolder.class.getSimpleName();


    public MovieReviewCollectionAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        Log.e(LOG_TAG, "In MovieReviewCollectionAdapter");
    }

    @Override
    public MovieReviewCollectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(LOG_TAG, "In onCreateViewHolder");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.review_item_card_view, parent, false);
        return new MovieReviewCollectionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewCollectionItemViewHolder viewHolder, Cursor cursor) {
        Log.e(LOG_TAG, "In onBindViewHolder");
        MovieReview movieReview = new MovieReview(cursor);
        viewHolder.bindReview(movieReview, cursor.getPosition());
    }
}
