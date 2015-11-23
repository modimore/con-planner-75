package lml.con_management_app;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import utils.Convention;
import utils.DownloadConventionTask;
import utils.Event;
import utils.SearchConventionsTask;

//Event List activity - shows a series of listed Events from the convention, links each to the Detail activity
//   Sends data for respective Events to fragment
public class EventListActivity extends AppCompatActivity
        implements EventListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //Get Convention data from parcel
        Bundle data = getIntent().getExtras();
        Convention c = data.getParcelable("convention");
        //Get Event data from Convention
        List<Event> events = c.getEventList();
        //Turn eventlist into an ArrayList for parceling
        ArrayList<Event> AL_events = arrayListify(events);


        //Get FRAGMENT NONSENSE ALL UP IN HERE
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        setContentView(R.layout.activity_event_list);

        if (findViewById(R.id.event_detail_container) != null) {
            mTwoPane = true;
            // Its a tablet, so create a new detail fragment if one does not already exist
            EventDetailFragment df = (EventDetailFragment) fm.findFragmentByTag("Detail");
            if (df == null) {
                // Initialize new detail fragment
                df = new EventDetailFragment();
                Bundle args = new Bundle();
                args.putParcelable("event", new Event());
                df.setArguments(args);
                fm.beginTransaction().replace(R.id.event_detail_container, df, "Detail").commit();
            }
        }


        // Initialize a new  list fragment, if one does not already exist
        EventListFragment elf = (EventListFragment) fm.findFragmentByTag("List");
        if ( elf == null) {
            elf = new EventListFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList("events", AL_events);
            elf.setArguments(arguments);
            Log.v("con mgmt app", "List Activity: Create a new List Fragment " + elf);
            fm.beginTransaction().replace(R.id.event_list, elf, "List").commit();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_return);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPrevious();
            }
        });
        //"up" button - links back to parent activity defined in manifest
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Callback method from {@link EventListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
   @Override
    public void onItemSelected(Event e) {
       if (mTwoPane) {
           // It's a tablet, so update the detail fragment
           Bundle arguments = new Bundle();
           // Pass the selected event object to the DetailFragment
           arguments.putParcelable("event", e);
           EventDetailFragment fragment = new EventDetailFragment();
           fragment.setArguments(arguments);
           getSupportFragmentManager().beginTransaction()
                   .replace(R.id.event_detail_container, fragment, "Detail")
                   .commit();
       } else {
           // It's a phone, so launch a new detail activity
           Intent detailIntent = new Intent(this, EventDetailActivity.class);
           // Pass the selected event object to the DetailActivity
           Parcelable p = e;
           detailIntent.putExtra("event", p);
           startActivity(detailIntent);
       }

    }

    public ArrayList<Event> arrayListify(List<Event> e){
        ArrayList<Event> a = new ArrayList<Event>();
        for(int i = 0; i < e.size(); ++i){
            a.add(e.get(i));
        }
        return a;
    }



    public void gotoPrevious() {
        Intent eventIntent = new Intent(EventListActivity.this, ConventionPageActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }

}
