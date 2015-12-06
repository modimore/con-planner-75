package lml.con_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import utils.AppUtils;
import utils.Convention;
import utils.Event;

public class EventDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set page title
        Bundle data = getIntent().getExtras();
        Event e = data.getParcelable("event");
        Convention c = data.getParcelable("convention");
        getSupportActionBar().setTitle(e.getName());

        //Edit text
        addEventText(e);

        //Button to add event to personal schedule
        addPersonalButton(e, c);

        //Button to return to the schedule page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_return);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPrevious();
            }
        });


        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    public void addEventText(Event e) {
        TextView textView = (TextView) findViewById(R.id.textView);
        String text = String.format("Host: %s\n\nRoom: %s\n\nDescription: %s \n\n", e.getHostName(), e.getRoom(), e.getDescription());
        textView.setText(text);
    }

    public void addPersonalButton(final Event e, final Convention c) {
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.eventLayout_id);
        final Button button1 = new Button(this);
        thisDangLayout.addView(button1);
        button1.setText("ADD TO PERSONAL SCHEDULE");
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addToPersonalScheduleClick(e, c);
                button1.setText("ADDED TO PERSONAL SCHEDULE");
            }
        });
    }

    public void addToPersonalScheduleClick(Event e, Convention c) {
        AppUtils.addToPersonalSchedule(e.getName(),c.getName());

    }

    public void gotoPrevious() {
        Intent eventIntent = new Intent(EventDescriptionActivity.this, EventActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }


}
