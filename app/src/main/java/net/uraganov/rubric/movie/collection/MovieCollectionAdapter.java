package net.uraganov.rubric.movie.collection;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.uraganov.rubric.CursorRecyclerViewAdapter;
import net.uraganov.rubric.R;
import net.uraganov.rubric.model.MovieDetails;

public class MovieCollectionAdapter extends CursorRecyclerViewAdapter<MovieCollectionItemViewHolder> {


    public MovieCollectionAdapter(Context context,Cursor cursor) {
        super(context,cursor);
    }

    @Override
    public MovieCollectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item_card_view, parent, false);
        return new MovieCollectionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieCollectionItemViewHolder viewHolder, Cursor cursor) {
        MovieDetails movie = new MovieDetails(cursor);
        viewHolder.bindMovie(movie, cursor.getPosition());
    }
}
