package fr.enssat.morvan.richinterface;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benjent on 11/01/18.
 */

/*
  The tag is basically a chapter with a relevant URL for the web view,
  a timestamp for the video and a label.
 */
public class Tag implements Parcelable {

    String label, url;
    Integer timeStamp;

    public Tag(String label, Integer timeStamp, String url) {
        this.label = label;
        this.timeStamp = timeStamp;
        this.url = url;
    }

    private Tag(Parcel in) {
        this.label = in.readString();
        this.timeStamp = in.readInt();
        this.url = in.readString();
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
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
        parcel.writeString(this.getUrl());
    }
    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

}
