package net.uraganov.rubric.movie.detail.trailer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.uraganov.rubric.R;
import net.uraganov.rubric.model.Youtube;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieTrailerCollectionItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private static final String LOG_TAG = MovieTrailerCollectionItemViewHolder.class.getSimpleName();

    private Youtube mTrailer;
    private Context mContext;
    private int mPosition;

    @Bind(R.id.trailer_imageview)
    ImageView mTrailerView;

    @Bind(R.id.trailer_name_textview)
    TextView mTrailerNameText;


    public MovieTrailerCollectionItemViewHolder(View itemView) {
        super(itemView);
        Log.e(LOG_TAG, "In MovieTrailerCollectionItemViewHolder");
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void bindTrailer(Youtube youtube, int position) {

        Log.e(LOG_TAG, "In bindTrailer");

        mTrailer = youtube;
        mPosition = position;
        mTrailerNameText.setText(youtube.getName());
    }

    @Override
    public void onClick(View v) {
        String source = mTrailer.getSource();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + source));
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + source));
            mContext.startActivity(intent);
        }
    }
}
