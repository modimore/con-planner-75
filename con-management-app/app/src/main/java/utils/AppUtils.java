package utils;
import java.io.Serializable;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import utils.Convention;

/**
 * Created by samsok on 11/15/15.
 */
public class AppUtils implements Serializable{

    /**
     * Deletes all of the downloaded conventions. Mainly used for testing.
     */
    public static void deleteDownloadedConventions() {
        File con_dir = new File(Environment.getExternalStorageDirectory().toString() + "/Conventions");
        deleteFolder(con_dir);
    }

    //Deletes the convention's directory. Used on update of a convnetion information
    public static void deleteConvention(Convention c) {
        File con_dir = new File(Environment.getExternalStorageDirectory().toString() + "/Conventions/" + c.getName());
        deleteFolder(con_dir);
        con_dir.delete();
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                }
                f.delete();
            }
        }
    }

    /**
     * @return a list of all conventions whose files are on the machine
     *          ps: checks for existance of JSON file.
     */
    public static List<Convention> getDownloadedConventions() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Conventions/");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        List<Convention> conventions = new LinkedList<>();
        for(String s : directories)
        {
            conventions.add(parseConvention(s));
        }
        return conventions;
    }

    //Takes the .txt file in the specified convention directory
    //Pareses the JSON into a full Convention object
    public static Convention parseConvention(String con_name) {
        //Find the directory for the SD Card using the API
        File sdcard = new File(Environment.getExternalStorageDirectory().toString() + "/Conventions/" + con_name);

        //Get the text file
        File file = new File(sdcard,con_name +".txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        Convention result = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.e("Parse", e.getMessage(), e);
        }

        try {
            result = new Convention();
            JSONObject con_json = new JSONObject(text.toString());

            result.setName(con_json.getString("name"));
            result.setDescription(con_json.getString("description"));
            result.setLocation(con_json.getString("location"));
            result.setStart(con_json.getString("start"));
            result.setEnd(con_json.getString("end"));

            JSONArray events_json = con_json.getJSONArray("events");
            List<Event> events = new ArrayList<>();
            for(int i = 0; i < events_json.length(); i++)
            {
                JSONObject e_json = events_json.getJSONObject(i);
                Event e = new Event();

                e.setName(e_json.getString("name"));
                e.setHostName(e_json.getString("host_name"));
                e.setDescription(e_json.getString("description"));
                e.setLength(e_json.getInt("length"));

                events.add(e);
            }

            JSONArray rooms_json = con_json.getJSONArray("rooms");
            List<Room> rooms = new ArrayList<>();
            for(int i = 0; i < rooms_json.length(); i++)
            {
                JSONObject r_json = rooms_json.getJSONObject(i);
                Room r = new Room();

                r.setName(r_json.getString("room_name"));

                rooms.add(r);
            }

            JSONArray hosts_json = con_json.getJSONArray("hosts");
            List<Host> hosts = new ArrayList<>();
            for(int i = 0; i < hosts_json.length(); i++)
            {
                JSONObject h_json = hosts_json.getJSONObject(i);
                Host h = new Host();

                h.setName(h_json.getString("name"));

                hosts.add(h);
            }

            JSONArray documents_json = con_json.getJSONArray("documents");
            List<Document> documents = new ArrayList<>();
            for(int i = 0; i < documents_json.length(); i++)
            {
                JSONObject d_json = documents_json.getJSONObject(i);
                Document d = new Document();

                d.setDisplayName(d_json.getString("display_name"));
                d.setLocation(d_json.getString("location"));

                documents.add(d);
            }

            result.setEventList(events);
            result.setHostList(hosts);
            result.setRoomList(rooms);
            result.setDocuments(documents);

            return result;
        }
        catch (JSONException e) {
            Log.e("Parse", e.getMessage(), e);
        }
        return null;
    }

    public static String convertInputStreamToString(InputStream is) throws IOException, UnsupportedEncodingException {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
