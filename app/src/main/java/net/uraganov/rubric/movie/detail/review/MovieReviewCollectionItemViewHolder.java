package net.uraganov.rubric.movie.detail.review;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.uraganov.rubric.R;
import net.uraganov.rubric.model.MovieReview;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieReviewCollectionItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private static final String LOG_TAG = MovieReviewCollectionItemViewHolder.class.getSimpleName();

    private MovieReview mReview;
    private Context mContext;


    @Bind(R.id.review_author)
    TextView mAuthorTextView;

    @Bind(R.id.review_content_textview)
    TextView mContentTextView;


    public MovieReviewCollectionItemViewHolder(View itemView) {
        super(itemView);
        Log.e(LOG_TAG, "In MovieReviewCollectionItemViewHolder");
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void bindReview(MovieReview movieReview, int position) {

        Log.e(LOG_TAG, "In bindTrailer");

        mReview = movieReview;
        mAuthorTextView.setText(movieReview.getAuthor());
        mContentTextView.setText(movieReview.getContent());
    }

    @Override
    public void onClick(View v) {
    }
}
