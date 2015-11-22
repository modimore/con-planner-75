package lml.con_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.util.Log;
import utils.*;
import test.UtilTests;

public class ConventionPageActivity extends AppCompatActivity {

    //BUTTONS FOR THE CONVENTION PAGE:
    //---Schedule of events, Documents, PersonalSchedule
    Button eventsButton;
    Button docsButton;
    Button personalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convention_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Articulate Buttons:
        eventsButton = (Button) findViewById(R.id.eventsButton_id);
        eventsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoEvents();
            }
        });

        docsButton = (Button) findViewById(R.id.docsButton_id);
        docsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoDocs();
            }
        });

        personalButton = (Button) findViewById(R.id.personalButton_id);
        personalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoPersonal();
            }
        });

        //"up" button - links back to parent activity defined in manifest
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //Methods for Button behavior
    //CONVENTION PAGE BUTTONS: lead to respective activities (docs, eventlist, personalschedule)
    public void gotoEvents() {
        Intent eventIntent = new Intent(ConventionPageActivity.this, EventListActivity.class);
        startActivity(eventIntent);
    }

    public void gotoDocs() {
       Intent docIntent = new Intent(ConventionPageActivity.this, DocsPageActivity.class);
       startActivity(docIntent);
    }

    public void gotoPersonal() {
       Intent personalIntent = new Intent(ConventionPageActivity.this, EventListActivity.class);
       startActivity(personalIntent);
    }


}
