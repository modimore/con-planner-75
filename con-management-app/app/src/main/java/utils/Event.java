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
public class Event implements Serializable, Parcelable, Comparable<Event> {
    private String name;
    private String description;
    private int length;

    private String start;
    private String end;
    private String room;

    private String host_name;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Event(){}

    //toString function for listadapter implementation
    public String toString () {
        return name;
    }

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
        start = in.readString();
        end = in.readString();
        room = in.readString();
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
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(room);
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

    @Override
    public int compareTo(Event another) {
        if (another == null)
        {
            return -1;
        }
        if(this.getStart().equals(""))
        {
            if(another.getStart().equals(""))
            {
                return this.getName().compareTo(another.getName());
            }
            else
            {
                return 1;
            }
        }
        if(another.getStart().equals(""))
        {
            return -1;
        }

        if(this.getStart().compareTo(another.getStart()) == 0){
            return this.getName().compareTo(another.getName());
        }
        else {
            return this.getStart().compareTo(another.getStart());
        }
    }
}