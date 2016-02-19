package net.uraganov.rubric.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.uraganov.rubric.database.TrailerColumns;


public class Youtube {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("type")
    @Expose
    private String type;


    static final String LOG_TAG = Youtube.class.getCanonicalName();
    static final String PLAYER = "youtube";

    /**
     * No args constructor for use in serialization
     *
     */
    public Youtube() {
    }


    public Youtube(Cursor cursor) {

        int idx_name = cursor.getColumnIndex(TrailerColumns.COLUMN_NAME);
        int idx_source = cursor.getColumnIndex(TrailerColumns.COLUMN_SOURCE);
        int idx_type = cursor.getColumnIndex(TrailerColumns.COLUMN_TYPE);
        int idx_size = cursor.getColumnIndex(TrailerColumns.COLUMN_SIZE);

        setName(cursor.getString(idx_name));
        setSource(cursor.getString(idx_source));
        setSize(cursor.getString(idx_size));
        setType(cursor.getString(idx_type));
    }



    /**
     *
     * @param source
     * @param name
     * @param type
     * @param size
     */
    public Youtube(String name, String size, String source, String type) {
        this.name = name;
        this.size = size;
        this.source = source;
        this.type = type;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Youtube withName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     * The size
     */
    public String getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    public void setSize(String size) {
        this.size = size;
    }

    public Youtube withSize(String size) {
        this.size = size;
        return this;
    }

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    public Youtube withSource(String source) {
        this.source = source;
        return this;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    public Youtube withType(String type) {
        this.type = type;
        return this;
    }


    public ContentValues toContentValues(long movieId) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TrailerColumns.COLUMN_NAME, getName());
        contentValues.put(TrailerColumns.COLUMN_SIZE, getSize());
        contentValues.put(TrailerColumns.COLUMN_SOURCE, getSource());
        contentValues.put(TrailerColumns.COLUMN_TYPE, getType());
        contentValues.put(TrailerColumns.COLUMN_PLAYER, PLAYER);
        contentValues.put(TrailerColumns.MOVIE_ID, movieId);
        return contentValues;
    }


}