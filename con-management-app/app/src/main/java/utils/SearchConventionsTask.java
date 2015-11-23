package utils;

import java.io.Serializable;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by samsok on 11/15/15.
 */
public class SearchConventionsTask extends AsyncTask<String, Void, List<Convention>> {

    @Override
    protected List<Convention> doInBackground(String... params) {
        //URL("localhost:3000/convention/search?query="  + query);
        InputStream is = null;
        ArrayList<Convention> results = new ArrayList<>();

        //Returns null on exception
        try {
            //Encode the query to not break when spaces are included
            String query = URLEncoder.encode(params[0], "UTF-8");
            URL url = new URL("http://127.0.0.1:3000/client_search?query="  + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Boilerplate for HTTP
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            if(response != HttpURLConnection.HTTP_OK)
            {
                Log.e("Search", "Non-200: " + response);
                return null;
            }

            // Convert the InputStream into a string
            String contentAsString = AppUtils.convertInputStreamToString(is);
            Log.d("Search",contentAsString);

            JSONObject json = new JSONObject(contentAsString);
            JSONArray conventions = json.getJSONArray("conventions");

            //construct a json object from information from http call
            // then use that to populate a list of Convention objects
            for(int i = 0; i < conventions.length(); i++)
            {
                JSONObject con_json = conventions.getJSONObject(i);
                Convention con = new Convention();

                con.setName(con_json.getString("name"));
                con.setDescription(con_json.getString("description"));
                con.setLocation(con_json.getString("location"));
                con.setStart(con_json.getString("start"));
                con.setEnd(con_json.getString("end"));

                results.add(con);
            }

        } catch (Exception e) {
            Log.e("Search", e.getMessage(), e);
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex){
                Log.e("Search", ex.getMessage(), ex);
            }
        }
        return results;
    }

}
