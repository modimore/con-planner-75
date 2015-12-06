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

import java.util.Collections;
import java.util.List;

import utils.AppUtils;
import utils.Convention;
import utils.Event;

public class PersonalEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle data = getIntent().getExtras();
        Convention c = data.getParcelable("convention");
        getSupportActionBar().setTitle(c.getName() + " Schedule");

        //Get & Sort Events List
        List<Event> eventList = AppUtils.getPersonalSchedule(c);
        Collections.sort(eventList);

        //If event has been added to the personal list previously
        for(final Event e : eventList) {
            if(!e.isPersonal()) {
                populateButtons(e);
            }
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


    public void populateButtons(final Event e) {
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.eventLayout_id);
        Button button1 = new Button(this);
        thisDangLayout.addView(button1);
        String text = String.format("%s\n%s\n%s - %s", e.getName(), e.getRoom(), e.getStart(), e.getEnd());
        button1.setText(text);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gotoDetailPage(e);
            }
        });
    }


    public void gotoDetailPage(Event e){
        Intent eventIntent = new Intent(PersonalEventActivity.this, PersonalEventDetailActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        eventIntent.putExtra("event", (Parcelable) e);
        startActivity(eventIntent);
    }


    public void gotoPrevious() {
        Intent eventIntent = new Intent(PersonalEventActivity.this, ConventionPageActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }

}
