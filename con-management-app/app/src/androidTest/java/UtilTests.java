import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.test.ActivityTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import utils.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UtilTests extends InstrumentationTestCase {

    @Test
    public void testSearchConvention() throws Exception{

        boolean contains_a = false;
        boolean contains_b = false;
        boolean contains_ab = false;
        boolean contains_o = false;

        //test single letter
        InputStream in = getInstrumentation().getContext().getAssets().open("search_test_a.json");
        String json = AppUtils.convertInputStreamToString(in);
        List<Convention> results = new SearchConventionsTask().execute("A",json).get();
        for(Convention c : results) {
            if(c.getName().equals("Test A")) { contains_a = true; }
            else if(c.getName().equals("Test AB")) { contains_ab = true; }
            else if(c.getName().equals("Test B")) { contains_b = true; }
            else if(c.getName().equals("Test O")) { contains_o = true; }
        }

        assertTrue(contains_a);
        assertTrue(contains_ab);
        assertTrue(!contains_b);
        assertTrue(!contains_o);

        contains_a = false;
        contains_b = false;
        contains_ab = false;
        contains_o = false;

        //test phrase
        in = getInstrumentation().getContext().getAssets().open("search_test_ab.json");
        json = AppUtils.convertInputStreamToString(in);
        results = new SearchConventionsTask().execute("AB",json).get();
        for(Convention c : results) {
            if(c.getName().equals("Test A")) { contains_a = true; }
            else if(c.getName().equals("Test AB")) { contains_ab = true; }
            else if(c.getName().equals("Test B")) { contains_b = true; }
            else if(c.getName().equals("Test O")) { contains_o = true; }
        }

        assertTrue(!contains_a);
        assertTrue(contains_ab);
        assertTrue(!contains_b);
        assertTrue(!contains_o);

        contains_a = false;
        contains_b = false;
        contains_ab = false;
        contains_o = false;

        //test no results
        in = getInstrumentation().getContext().getAssets().open("search_test_d.json");
        json = AppUtils.convertInputStreamToString(in);
        results = new SearchConventionsTask().execute("D",json).get();
        if(results == null) return;
        for(Convention c : results) {
            if(c.getName().equals("Test A")) { contains_a = true; }
            else if(c.getName().equals("Test AB")) { contains_ab = true; }
            else if(c.getName().equals("Test B")) { contains_b = true; }
            else if(c.getName().equals("Test O")) { contains_o = true; }
        }

        assertTrue(!contains_a);
        assertTrue(!contains_ab);
        assertTrue(!contains_b);
        assertTrue(!contains_o);

    }

    @Test
    public void testDownlaodAndPersonalSchedule() throws Exception {
        AppUtils.deleteDownloadedConventions();

        InputStream in = getInstrumentation().getContext().getAssets().open("personal_schedule_test.json");
        String json = AppUtils.convertInputStreamToString(in);
        Convention c = new DownloadConventionTask().execute("Test Convention 1", json).get();

        AppUtils.addToPersonalSchedule("Event 1", c.getName());
        List<Event> e = AppUtils.getPersonalSchedule(c);

        assertEquals("Event 1", e.get(0).getName());

        AppUtils.removeFromPersonalSchedule("Event 1", c.getName());
        e = AppUtils.getPersonalSchedule(c);

        assertEquals(0, e.size());

    }
}