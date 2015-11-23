package utils;
import java.io.Serializable;

/**
 * Created by samsok on 11/14/15.
 */


//Host Class; data unit for input from website
//has get/set functions to access data
//implements Serializable class for transition among Activites
public class Host implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
