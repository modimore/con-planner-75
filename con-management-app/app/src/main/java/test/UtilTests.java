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
    public void testRemoveConventions() {
        AppUtils.deleteDownloadedConventions();
    }

    @Test
    public void testPersonalSchedule() throws Exception {
        AppUtils.deleteDownloadedConventions();
        Convention c = new DownloadConventionTask().execute("Test Convention 1").get();

        AppUtils.addToPersonalSchedule("Event 1", c.getName());
        List<Event> e = AppUtils.getPersonalSchedule(c);

        assertEquals("Event 1", e.get(0).getName());

        AppUtils.removeFromPersonalSchedule("Event 1", c.getName());
        e = AppUtils.getPersonalSchedule(c);

        assertEquals(0, e.size());

    }

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

            AppUtils.deleteDownloadedConventions();

        } catch (Exception e) {
            Log.e("Results", e.getMessage(), e);
        }
    }
}