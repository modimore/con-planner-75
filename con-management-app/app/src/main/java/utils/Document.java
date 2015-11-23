package utils;
import java.io.Serializable;

/**
 * Created by samsok on 11/15/15.
 */
public class Document implements Serializable{
    private String display_name;
    private String location;

    public String getDisplayName() {
        return display_name;
    }

    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
