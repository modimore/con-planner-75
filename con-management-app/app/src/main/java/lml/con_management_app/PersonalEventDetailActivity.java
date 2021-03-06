package lml.con_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

public class PersonalEventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set page title
        Bundle data = getIntent().getExtras();
        Event e = data.getParcelable("event");
        Convention c = data.getParcelable("convention");
        getSupportActionBar().setTitle(e.getName());

        //Button to remove Event from the Personal Schedule
        removePersonalButton(e, c);

        //Edit text
        addEventText(e);

        addReturnButton();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void addReturnButton(){
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.eventLayout_id);
        Button button1=new Button(this);
        thisDangLayout.addView(button1);
        button1.setText("Return to Personal Schedule");
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gotoPrevious();
            }
        });

        button1.getBackground().setColorFilter(Color.parseColor("#51d43c"), PorterDuff.Mode.MULTIPLY);
        button1.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void gotoPrevious() {
        Intent eventIntent = new Intent(PersonalEventDetailActivity.this, PersonalEventActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }

    public void removePersonalButton(final Event e, final Convention c) {
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.eventLayout_id);
        final Button button1 = new Button(this);
        thisDangLayout.addView(button1);
        button1.setText("REMOVE FROM PERSONAL SCHEDULE");
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeFromPersonalScheduleClick(e, c);
                button1.setText("REMOVED FROM PERSONAL SCHEDULE");
            }
        });


        button1.getBackground().setColorFilter(Color.parseColor("#5a97ec"), PorterDuff.Mode.MULTIPLY);
        button1.setTextColor(Color.parseColor("#FFFFFF"));

    }

    public void removeFromPersonalScheduleClick(Event e, Convention c){
        AppUtils.removeFromPersonalSchedule(e.getName(), c.getName());
    }


    public void addEventText(Event e) {
        TextView textView = (TextView) findViewById(R.id.textView);
        String text = String.format("Host: %s\n\nRoom: %s\n\nDescription: %s \n\n", e.getHostName(), e.getRoom(), e.getDescription());
        textView.setText(text);
    }


}
