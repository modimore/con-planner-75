package utils;
import java.io.Serializable;

/**
 * Created by samsok on 11/14/15.
 */
public class Room implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
