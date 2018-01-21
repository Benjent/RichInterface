package fr.enssat.morvan.richinterface;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benjent on 18/01/18.
 */

/*
    The waypoint is used in the map view.
    The timestamp is used so that a click on the waypoint changes the position in the video.
 */
public class Waypoint implements Parcelable {

    String label;
    Long lat, lon;
    Integer timeStamp;

    public Waypoint(String label, Integer timeStamp, Long lat, Long lon) {
        this.label = label;
        this.lat = lat;
        this.lon = lon;
        this.timeStamp = timeStamp;
    }

    private Waypoint(Parcel in) {
        this.label = in.readString();
        this.timeStamp = in.readInt();
        this.lat = lat;
        this.lon = lon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Long getLon() {
        return lon;
    }

    public void setLon(Long lon) {
        this.lon = lon;
    }

    public Integer getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    // Parcels for data transition

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getLabel());
        parcel.writeInt(this.getTimeStamp());
        parcel.writeLong(this.getLat());
        parcel.writeLong(this.getLon());
    }
    public static final Creator<Waypoint> CREATOR = new Creator<Waypoint>() {
        @Override
        public Waypoint createFromParcel(Parcel in) {
            return new Waypoint(in);
        }

        @Override
        public Waypoint[] newArray(int size) {
            return new Waypoint[size];
        }
    };
}
