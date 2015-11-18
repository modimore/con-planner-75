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

        try {

            File convention = new File(Environment.getExternalStorageDirectory().toString() +"/Conventions");
            assertTrue(convention.exists());

            AppUtils.deleteDownloadedConventions();

            List<Convention> conventionList = new SearchConventionsTask().execute("o").get(); //"o" is the search term
            Log.d("SearchResults", conventionList.toString());

            Convention c = new DownloadConventionTask().execute("holds").get(); //"holds" is the con name
            if(c != null) {
                Log.d("DownloadResults", c.toString());
            } else {
                Log.d("DownloadResults", "null");
            }
            c = new DownloadConventionTask().execute("bold").get(); //"bold is the con name
            if(c != null) {
                Log.d("DownloadResults", c.toString());
            } else {
                Log.d("DownloadResults", "null");
            }

            List<Convention> allDownloaded = AppUtils.getDownloadedConventions();
            Log.d("DownloadResults", allDownloaded.toString());

        } catch (Exception e) {
            Log.e("Results", e.getMessage(), e);
        }
    }
}