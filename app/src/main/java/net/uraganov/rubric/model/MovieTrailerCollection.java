package net.uraganov.rubric.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MovieTrailerCollection {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("quicktime")
    @Expose
    private List<Object> quicktime = new ArrayList<Object>();
    @SerializedName("youtube")
    @Expose
    private List<Youtube> youtube = new ArrayList<Youtube>();

    /**
     * No args constructor for use in serialization
     */
    public MovieTrailerCollection() {
    }

    /**
     * @param id
     * @param youtube
     * @param quicktime
     */
    public MovieTrailerCollection(int id, List<Object> quicktime, List<Youtube> youtube) {
        this.id = id;
        this.quicktime = quicktime;
        this.youtube = youtube;
    }

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    public MovieTrailerCollection withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * @return The quicktime
     */
    public List<Object> getQuicktime() {
        return quicktime;
    }

    /**
     * @param quicktime The quicktime
     */
    public void setQuicktime(List<Object> quicktime) {
        this.quicktime = quicktime;
    }

    public MovieTrailerCollection withQuicktime(List<Object> quicktime) {
        this.quicktime = quicktime;
        return this;
    }

    /**
     * @return The youtube
     */
    public List<Youtube> getYoutube() {
        return youtube;
    }

    /**
     * @param youtube The youtube
     */
    public void setYoutube(List<Youtube> youtube) {
        this.youtube = youtube;
    }

    public MovieTrailerCollection withYoutube(List<Youtube> youtube) {
        this.youtube = youtube;
        return this;
    }




}