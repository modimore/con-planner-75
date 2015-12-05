package utils;

import java.io.Serializable;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import org.apache.http.util.ByteArrayBuffer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by samsok on 11/15/15.
 */
public class DownloadConventionTask extends AsyncTask<String, Void, Convention> {
    /*
     * @param name name of convention to download
     * @effects Will check site for existance of convention (site-side check)
     *          If a convention with that name exists, sends back a list of:
     *              events, hosts, rooms, and documents
     *          It will then go through the list of documents and download each.
     *          It will store everything in a file in the appropriate location on the machine
     */

    @Override
    protected Convention doInBackground(String... params) {
        //URL("localhost:3000/convention/search?query="  + query);
        InputStream is = null;
        Convention result = null;

        //Returns null on exception
        try {
            //Encode space into the uri
            String con = params[0].replaceAll(" ", "%20");
            URL url = new URL("http://127.0.0.1:3000/convention/" + con + "/download");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Boilerplate
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();

            if(response != HttpURLConnection.HTTP_OK) {
                Log.e("Search", "Non-200: " + response);
                return null;
            }

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = AppUtils.convertInputStreamToString(is);
            JSONObject con_json = new JSONObject(contentAsString);
            Log.d("Download", contentAsString);

            //get Convention's directory to place information
            File dir = new File(Environment.getExternalStorageDirectory().toString() +"/Conventions/" +con_json.getString("name"));

            if (!dir.mkdirs()) {
                Log.d("Download", "File exists");
            }
            Log.d("Download", dir.getAbsolutePath());

            FileOutputStream fos ;

            try {
                File file = new File(dir, con_json.getString("name")+".txt");
                File personal = new File(dir, "personal.txt");
                Log.d("Download", file.getAbsolutePath());
                Log.d("Download", file.exists() ? "Yes" : "No");
                fos = new FileOutputStream(file);

                FileWriter fWriter;

                try {
                    fWriter = new FileWriter(fos.getFD());
                    fWriter.write(con_json.toString());
                    fWriter.close();
                } catch (Exception e) {
                    Log.e("Download", e.getMessage(), e);
                } finally {
                    fos.getFD().sync();
                    fos.close();
                }
                Log.d("Download", file.exists() ? "Yes" : "No");

                if(personal.exists()) {
                    FileWriter pWriter;
                    FileOutputStream pos;
                    pos = new FileOutputStream(personal);

                    try {
                        pWriter = new FileWriter(pos.getFD());
                        pWriter.write("{\"personal\":[]}");
                        pWriter.close();
                    } catch (Exception e) {
                        Log.e("Download", e.getMessage(), e);
                    } finally {
                        fos.getFD().sync();
                        fos.close();
                    }
                }

            } catch (Exception e) {
                Log.e("Download", e.getMessage(), e);
            }

            result = AppUtils.parseConvention(con_json.getString("name"));

        } catch (Exception e) {
            Log.e("Download", e.getMessage(), e);
            return  null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex){
                Log.e("Download", ex.getMessage(), ex);
            }
        }

        return result;
    }

}

