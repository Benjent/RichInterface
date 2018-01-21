package fr.enssat.morvan.richinterface;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by benjent on 11/01/18.
 */

/*
    Retrieve data from the videoMetadataPerser and streamline it java-style.
 */
public class VideoMetadata implements Parcelable {

    private String name, url, pageUrl;
    private ArrayList<Tag> tags;
    private ArrayList<Waypoint> waypoints;

    public VideoMetadata(JSONObject videoMetadata) {
        this.name   = null;
        this.url    = null;
        this.tags       = new ArrayList<>();
        this.waypoints  = new ArrayList<>();

        try {
            setName(videoMetadata.getString("name"));
            setUrl(videoMetadata.getString("url"));

            JSONArray videoTags = videoMetadata.getJSONArray("tags");
            JSONArray waypoints = videoMetadata.getJSONArray("waypoints");

            for (int i = 0; i < videoTags.length(); i++) {
                JSONObject videoTag = (JSONObject) videoTags.get(i);
                this.tags.add(new Tag(videoTag.getString("label"), videoTag.getInt("timestamp"), videoTag.getString("url")));
            }

            for (int i = 0; i < waypoints.length(); i++) {
                JSONObject waypoint = (JSONObject) waypoints.get(i);
                this.waypoints.add(new Waypoint(waypoint.getString("label"), waypoint.getInt("timestamp"), waypoint.getLong("lat"), waypoint.getLong("lon")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Parcels for data transition

    protected VideoMetadata(Parcel in) {
        name = in.readString();
        url = in.readString();
        pageUrl = in.readString();
        tags = in.createTypedArrayList(Tag.CREATOR);
        waypoints = in.createTypedArrayList(Waypoint.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getName());
        parcel.writeString(this.getUrl());
        parcel.writeTypedList(this.getTags());
        parcel.writeTypedList(this.getWaypoints());
    }

    public static final Creator<VideoMetadata> CREATOR = new Creator<VideoMetadata>() {
        @Override
        public VideoMetadata createFromParcel(Parcel in) {
            return new VideoMetadata(in);
        }

        @Override
        public VideoMetadata[] newArray(int size) {
            return new VideoMetadata[size];
        }
    };

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() { return this.url; }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
    public String getPageUrl() { return this.pageUrl; }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }
    public void addWaypoint(Waypoint waypoint) {
        this.waypoints.add(waypoint);
    }
}
