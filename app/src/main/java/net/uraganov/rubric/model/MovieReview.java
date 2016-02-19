package net.uraganov.rubric.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.uraganov.rubric.database.ReviewColumns;

public class MovieReview {

    @SerializedName("id")
    @Expose
    private String review_id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * No args constructor for use in serialization
     */
    public MovieReview() {
    }


    public MovieReview(Cursor cursor) {

        int idx_review_id = cursor.getColumnIndex(ReviewColumns.COLUMN_REVIEW_ID);
        int idx_author = cursor.getColumnIndex(ReviewColumns.COLUMN_AUTHOR);
        int idx_content = cursor.getColumnIndex(ReviewColumns.COLUMN_CONTENT);

        setReview_id(cursor.getString(idx_review_id));
        setAuthor(cursor.getString(idx_author));
        setContent(cursor.getString(idx_content));
    }


    /**
     * @return The review_id
     */
    public String getReview_id() {
        return review_id;
    }

    /**
     * @param review_id The review_id
     */
    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    /**
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }


    public ContentValues toContentValues(long movieId) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ReviewColumns.COLUMN_AUTHOR, getAuthor());
        contentValues.put(ReviewColumns.COLUMN_CONTENT, getContent());
        contentValues.put(ReviewColumns.COLUMN_REVIEW_ID, getReview_id());
        contentValues.put(ReviewColumns.MOVIE_ID, movieId);
        return contentValues;
    }

}
