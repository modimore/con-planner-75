package utils;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    public static void addToPersonalSchedule(String event, String con_name) {
        List<String> personals = parsePersonals(con_name);
        personals.add(event);
        writePersonalSchedule(personals, con_name);
    }

    public static void removeFromPersonalSchedule(String event, String con_name) {
        List<String> personals = parsePersonals(con_name);
        personals.remove(event);
        writePersonalSchedule(personals, con_name);
    }

    public static List<Event> getPersonalSchedule(Convention con) {
        Map<String, Event> con_event_map = new HashMap<>();

        for(Event e : con.getEventList()) {
            con_event_map.put(e.getName(), e);
        }

        List<Event> personal = new LinkedList<>();
        String con_name = con.getName();
        List<String> personal_names = parsePersonals(con_name);

        for(String name: personal_names) {
            Event e = con_event_map.get(name);

            if(e != null) {
                personal.add(e);
            }
        }
        Collections.sort(personal);
        return personal;

    }

    public static void writePersonalSchedule(List<String> events, String con_name) {
        //get Convention's directory to place information
        File dir = new File(Environment.getExternalStorageDirectory().toString() +"/Conventions/" +con_name);

        if (!dir.mkdirs()) {
            Log.d("WritePersonals", "File exists");
        }
        Log.d("WritePersonals", dir.getAbsolutePath());


        try {
            File personal = new File(dir, "personal.txt");
            FileWriter pWriter;
            FileOutputStream pos;
            pos = new FileOutputStream(personal);

            try {
                pWriter = new FileWriter(pos.getFD());
                JSONObject personal_json = new JSONObject();
                JSONArray personal_array = new JSONArray(events);
                personal_json.put("personal", personal_array);
                pWriter.write(personal_json.toString());
                pWriter.close();
            } catch (Exception e) {
                Log.e("Download", e.getMessage(), e);
            } finally {
                pos.getFD().sync();
                pos.close();
            }

        } catch (Exception e) {
            Log.e("Download", e.getMessage(), e);
        }
    }

    public static List<String> parsePersonals(String con_name) {
        List<String> personal = new LinkedList<>();

        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/Conventions/" + con_name);
        File file = new File(dir,"personal.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

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
            JSONArray events = new JSONObject(text.toString()).getJSONArray("personal");

            for(int i = 0; i < events.length(); i++)
            {
                String str = events.getString(i);
                personal.add(str);
            }

            return personal;
        }
        catch (JSONException e) {
            Log.e("Parse", e.getMessage(), e);
        }
        return null;
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
            //List<Event> events = new ArrayList<>();
            Map<String, Event> events = new HashMap<>();
            for(int i = 0; i < events_json.length(); i++)
            {
                JSONObject e_json = events_json.getJSONObject(i);
                Event e = new Event();

                e.setName(e_json.getString("name"));
                e.setHostName(e_json.getString("host_name"));
                e.setDescription(e_json.getString("description"));
                e.setLength(e_json.getInt("length"));

                events.put(e.getName(),e);
            }

            JSONArray schedule_json = con_json.getJSONArray("schedule");
            for(int i = 0; i < schedule_json.length(); i++)
            {
                JSONObject s_json = schedule_json.getJSONObject(i);
                Event e = events.get(s_json.getString("event"));
                if(e == null) continue;
                e.setStart(s_json.getString("start"));
                e.setEnd(s_json.getString("end"));
                e.setRoom(s_json.getString("room"));

                events.put(e.getName(), e);
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

            result.setEventList(new ArrayList<Event>(events.values()));
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
