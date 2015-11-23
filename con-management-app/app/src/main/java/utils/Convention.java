package utils;

import android.os.Parcelable;
import android.os.Parcel;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by samsok on 11/11/15.
 */

public class Convention implements Parcelable {
    private String name;
    private String description;
    private String location;
    private String start;
    private String end;

    private List<Event> event_list;
    private List<Host> host_list;
    private List<Room> room_list;

    private List<Document> documents;

    public Convention() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public List<Event> getEventList() {
        return event_list;
    }

    public void setEventList(List<Event> event_list) {
        this.event_list = event_list;
    }

    public List<Host> getHostList() {
        return host_list;
    }

    public void setHostList(List<Host> host_list) {
        this.host_list = host_list;
    }

    public List<Room> getRoomList() {
        return room_list;
    }

    public void setRoomList(List<Room> room_list) {
        this.room_list = room_list;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String toString() {
        return name + ":\t" + description + "\t@" + location + "\tfrom: " + start + "\tto: " + end;
    }

    //---------------------------------------------------------------------------------------------
    //Parcelable-ization stuff -- Rachel

    //parcelable constructor
    protected Convention(Parcel in) {
        name = in.readString();
        description = in.readString();
        location = in.readString();
        start = in.readString();
        end = in.readString();
        if (in.readByte() == 0x01) {
            event_list = new ArrayList<Event>();
            in.readList(event_list, Event.class.getClassLoader());
        } else {
            event_list = null;
        }
        if (in.readByte() == 0x01) {
            host_list = new ArrayList<Host>();
            in.readList(host_list, Host.class.getClassLoader());
        } else {
            host_list = null;
        }
        if (in.readByte() == 0x01) {
            room_list = new ArrayList<Room>();
            in.readList(room_list, Room.class.getClassLoader());
        } else {
            room_list = null;
        }
        if (in.readByte() == 0x01) {
            documents = new ArrayList<Document>();
            in.readList(documents, Document.class.getClassLoader());
        } else {
            documents = null;
        }
    }

    //bit thing for parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    //write convention into a parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(start);
        dest.writeString(end);
        if (event_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(event_list);
        }
        if (host_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(host_list);
        }
        if (room_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(room_list);
        }
        if (documents == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(documents);
        }
    }

    //need to extend this for interface reasons
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Convention> CREATOR = new Parcelable.Creator<Convention>() {
        @Override
        public Convention createFromParcel(Parcel in) {
            return new Convention(in);
        }

        @Override
        public Convention[] newArray(int size) {
            return new Convention[size];
        }
    };
}
