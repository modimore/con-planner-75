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

        try {
            URL url = new URL("http://127.0.0.1:3000/convention/" + params[0] + "/download");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();

            if(response == 500) {
                return result;
            }

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = AppUtils.convertInputStreamToString(is);
            JSONObject con_json = new JSONObject(contentAsString);
            Log.d("Download", contentAsString);

            File dir = new File(Environment.getExternalStorageDirectory().toString() +"/Conventions/" +con_json.getString("name"));

            if (!dir.mkdirs()) {
                Log.d("Download", "File exists");
            }
            Log.d("Download", dir.getAbsolutePath());

            FileOutputStream fos ;

            try {
                File file = new File(dir, con_json.getString("name")+".txt");
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

        //download all documents
        for(int i = 0; i < result.getDocuments().size(); i++) {
            Document doc = result.getDocuments().get(i);
            try {
                File dir = new File(Environment.getExternalStorageDirectory().toString() +"/Conventions/" +result.getName());
                File file = new File(dir, doc.getLocation().substring(doc.getLocation().lastIndexOf('/')+1));
                URL url = new URL("http://127.0.0.1:3000/" + doc.getLocation());

                Log.d("Download", url.toString());
                Log.d("Download", dir.toString());
                Log.d("Download", file.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();

               /*
                * Define InputStreams to read from the URLConnection.
                */
                is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

               /*
                * Read bytes to the Buffer until there is nothing more to read(-1).
                */
                ByteArrayBuffer baf = new ByteArrayBuffer(5000);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }


                /* Convert the Bytes read to a String. */
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.flush();
                fos.close();

            } catch (Exception e) {
                Log.e("Download", e.getMessage(), e);
                return null;
            }
        }

        return result;
    }

}

