package test;

import android.os.Environment;
import android.test.ActivityTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import utils.*;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UtilTests extends InstrumentationTestCase {
    @Test
    public void testConventionExists() throws Exception {

        File convention = new File(Environment.getExternalStorageDirectory().toString() +"/Conventions/holds");
        assertTrue(convention.exists());

        /*
        try {
            List<Convention> conventionList = new SearchConventionsTask().execute("o").get();
            Log.d("SearchResults", conventionList.toString());

            Convention c = new DownloadConventionTask().execute("holds").get();
            if(c != null) {
                Log.d("DownloadResults", c.toString());
            } else {
                Log.d("DownloadResults", "null");
            }


        } catch (Exception e) {
            Log.e("Results", e.getMessage(), e);
        }
        */
    }
}