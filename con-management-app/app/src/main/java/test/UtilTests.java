package test;

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
        List<Convention> results = new SearchConventionsTask().execute("A").get();
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
        results = new SearchConventionsTask().execute("AB").get();
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
        results = new SearchConventionsTask().execute("C").get();
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
    public void testPersonalScheduleAdd() throws Exception {
        AppUtils.deleteDownloadedConventions();
        Convention c = new DownloadConventionTask().execute("Test Convention 1").get();

        AppUtils.addToPersonalSchedule("Event 1", c.getName());
        List<Event> e = AppUtils.getPersonalSchedule(c);

        assertEquals("Event 1", e.get(0).getName());

        AppUtils.removeFromPersonalSchedule("Event 1", c.getName());
        e = AppUtils.getPersonalSchedule(c);

        assertEquals(0, e.size());

    }
}