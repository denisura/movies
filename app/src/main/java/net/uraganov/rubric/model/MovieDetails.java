package net.uraganov.rubric.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.uraganov.rubric.database.MovieColumns;
import net.uraganov.rubric.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieDetails {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("adult")
    @Expose
    private boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("vote_count")
    @Expose
    private int voteCount;
    @SerializedName("video")
    @Expose
    private boolean video;
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    private boolean favorite;

    public MovieDetails() {

    }

    public MovieDetails(Cursor cursor) {
        int idx_movie_id = cursor.getColumnIndex(MovieColumns.MOVIE_ID);
        int idx_original_title = cursor.getColumnIndex(MovieColumns.COLUMN_ORIGINAL_TITLE);
        int idx_poster_path = cursor.getColumnIndex(MovieColumns.COLUMN_POSTER_PATH);
        int idx_overview = cursor.getColumnIndex(MovieColumns.COLUMN_OVERVIEW);
        int idx_vote_average = cursor.getColumnIndex(MovieColumns.COLUMN_VOTE_AVERAGE);
        int idx_release_date = cursor.getColumnIndex(MovieColumns.COLUMN_RELEASE_DATE);
        int idx_favorite = cursor.getColumnIndex(MovieColumns.COLUMN_IS_FAVORITE);

        setId(cursor.getInt(idx_movie_id));
        setOriginalTitle(cursor.getString(idx_original_title));
        setPosterPath(cursor.getString(idx_poster_path));
        setOverview(cursor.getString(idx_overview));
        setVoteAverage(cursor.getDouble(idx_vote_average));
        setReleaseDate(cursor.getString(idx_release_date));
        setFavorite(cursor.getInt(idx_favorite) != 0);
        Log.e("denisura", "MovieDetails from cursor" + Long.toString(cursor.getLong(idx_release_date)));
    }

    /**
     *
     * @return
     * The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     *
     * @param posterPath
     * The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     *
     * @return
     * The adult
     */
    public boolean isAdult() {
        return adult;
    }


    public boolean isFavorite() {
        return favorite;
    }

    /**
     *
     * @param adult
     * The adult
     */
    public void setAdult(boolean adult) {
        this.adult = adult;
    }


    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     *
     * @return
     * The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     *
     * @param overview
     * The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     *
     * @return
     * The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }


    public String getReleaseDate(String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        if (releaseDate == null){
            return "";
        }
        Date releaseDate = Utilities.parseDate(this.releaseDate);
        if (releaseDate == null){
            return "";
        }
        return formatter.format(releaseDate);
    }
    /**
     *
     * @param releaseDate
     * The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    /**
     *
     * @return
     * The genreIds
     */
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    /**
     *
     * @param genreIds
     * The genre_ids
     */
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     *
     * @param originalTitle
     * The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     *
     * @return
     * The originalLanguage
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     *
     * @param originalLanguage
     * The original_language
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The backdropPath
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     *
     * @param backdropPath
     * The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    /**
     *
     * @return
     * The popularity
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     *
     * @param popularity
     * The popularity
     */
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    /**
     *
     * @return
     * The voteCount
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     *
     * @param voteCount
     * The vote_count
     */
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    /**
     *
     * @return
     * The video
     */
    public boolean isVideo() {
        return video;
    }

    /**
     *
     * @param video
     * The video
     */
    public void setVideo(boolean video) {
        this.video = video;
    }

    /**
     *
     * @return
     * The voteAverage
     */
    public double getVoteAverage() {
        return voteAverage;
    }

    /**
     *
     * @param voteAverage
     * The vote_average
     */
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public  ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieColumns.COLUMN_ADULT, isAdult());
        contentValues.put(MovieColumns.COLUMN_BACKDROP_PATH, getBackdropPath());
        contentValues.put(MovieColumns.MOVIE_ID, getId());
        contentValues.put(MovieColumns.COLUMN_ORIGINAL_LANGUAGE, getOriginalLanguage());
        contentValues.put(MovieColumns.COLUMN_ORIGINAL_TITLE,  getOriginalTitle());
        contentValues.put(MovieColumns.COLUMN_OVERVIEW,  getOverview());
        contentValues.put(MovieColumns.COLUMN_RELEASE_DATE, getReleaseDate());
        contentValues.put(MovieColumns.COLUMN_POSTER_PATH, getPosterPath());
        contentValues.put(MovieColumns.COLUMN_POPULARITY, getPopularity());
        contentValues.put(MovieColumns.COLUMN_TITLE, getTitle());
        contentValues.put(MovieColumns.COLUMN_VIDEO, isVideo());
        contentValues.put(MovieColumns.COLUMN_VOTE_AVERAGE, getVoteAverage());
        contentValues.put(MovieColumns.COLUMN_VOTE_COUNT, getVoteCount());
        return contentValues;
    }


}