package net.uraganov.rubric.movie.detail.trailer;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.uraganov.rubric.CursorRecyclerViewAdapter;
import net.uraganov.rubric.R;
import net.uraganov.rubric.model.Youtube;

public class MovieTrailerCollectionAdapter extends CursorRecyclerViewAdapter<MovieTrailerCollectionItemViewHolder> {
    private static final String LOG_TAG = MovieTrailerCollectionItemViewHolder.class.getSimpleName();


    public MovieTrailerCollectionAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        Log.e(LOG_TAG, "In MovieTrailerCollectionAdapter");

    }

    @Override
    public MovieTrailerCollectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(LOG_TAG, "In onCreateViewHolder");

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.trailer_item_card_view, parent, false);
        return new MovieTrailerCollectionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerCollectionItemViewHolder viewHolder, Cursor cursor) {


        Log.e(LOG_TAG, "In onBindViewHolder");

        Youtube youtube = new Youtube(cursor);
        viewHolder.bindTrailer(youtube, cursor.getPosition());
    }
}
