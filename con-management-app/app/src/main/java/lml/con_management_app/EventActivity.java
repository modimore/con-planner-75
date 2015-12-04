package lml.con_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.List;
import utils.*;
import java.util.*;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create buttons for Events in the Convention schedule
        Bundle data = getIntent().getExtras();
        Convention c = data.getParcelable("convention");
        getSupportActionBar().setTitle(c.getName() + " Schedule");

        //Get & Sort Events List
        List<Event> eventList = c.getEventList();
        Collections.sort(eventList);


        for(final Event e : eventList) {
            populateButtons(e);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_return);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPrevious();
            }
        });
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Methods for Button behavior

    public void gotoPrevious() {
        Intent eventIntent = new Intent(EventActivity.this, ConventionPageActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }

    public void populateButtons(final Event e) {
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.eventLayout_id);
        Button button1 = new Button(this);
        thisDangLayout.addView(button1);
        String text = String.format("%s\n%s - %s", e.getName(), e.getStart(), e.getEnd());
        button1.setText(text);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gotoDetailPage(e);
            }
        });
    }

    public void gotoDetailPage(Event e){
        Intent eventIntent = new Intent(EventActivity.this, EventDescriptionActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        eventIntent.putExtra("event", (Parcelable) e);
        startActivity(eventIntent);
    }


}


