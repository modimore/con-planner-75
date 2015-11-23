package utils;
import java.io.Serializable;

import android.os.Parcelable;
import android.os.Parcel;
/**
 * Created by samsok on 11/11/15.
 */


//Event Class; data unit for input from website
//has get/set functions to access data
//implements Serializable & Parcelable classes for transition among Activites
public class Event implements Serializable, Parcelable {
    private String name;
    private String description;
    private int length;

    public Event(){}

    //toString function for listadapter implementation
    public String toString () {
        return name;
    }

    private String host_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostName() {
        return host_name;
    }

    public void setHostName(String host_name) {
        this.host_name = host_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    protected Event(Parcel in) {
        name = in.readString();
        description = in.readString();
        length = in.readInt();
        host_name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(length);
        dest.writeString(host_name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}