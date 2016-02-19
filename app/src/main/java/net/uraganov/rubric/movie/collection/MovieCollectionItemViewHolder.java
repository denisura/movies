package net.uraganov.rubric.movie.collection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.uraganov.rubric.R;
import net.uraganov.rubric.model.MovieDetails;
import net.uraganov.rubric.movie.pager.MoviePagerActivity;
import net.uraganov.rubric.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieCollectionItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private MovieDetails mMovie;
    private Context mContext;
    private int mPosition;

    @Bind(R.id.poster_imageview)
    ImageView mPosterView;

    public MovieCollectionItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void bindMovie(MovieDetails movie, int position ) {
        mMovie = movie;
        mPosition = position;
        Picasso.with(mContext) //
                .load(Utilities.getMoviePosterUrl(mMovie.getPosterPath(),"w185")) //
                .fit() //
                .into(mPosterView);
    }

    @Override
    public void onClick(View v) {
        Intent intent = MoviePagerActivity.newIntent(mContext, mPosition);
        mContext.startActivity(intent);
    }
}
