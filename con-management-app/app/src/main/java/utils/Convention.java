package utils;

import java.util.List;

/**
 * Created by samsok on 11/11/15.
 */

public class Convention {
    private String name;
    private String description;
    private String location;
    private String start;
    private String end;

    private List<Event> event_list;
    private List<Host> host_list;
    private List<Room> room_list;

    private List<Document> documents;

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
}
