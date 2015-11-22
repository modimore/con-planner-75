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
import android.os.Environment;


public class ConventionPageActivity extends AppCompatActivity {

    //BUTTONS FOR THE CONVENTION PAGE:
    //---Schedule of events, Documents, PersonalSchedule
    Button eventsButton;
    Button docsButton;
    Button personalButton;
    Button dbTestButton;

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



        dbTestButton = (Button) findViewById(R.id.dbTestButton_id);
        dbTestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle data = getIntent().getExtras();

                Convention c = data.getParcelable("convention");

                if(c != null) {
                    dbTestButton.setText(c.getName());
                }
                else{
                    dbTestButton.setText("null");
                }

            }

    });
    }


    //Methods for Button behavior
    //CONVENTION PAGE BUTTONS: lead to respective activities (docs, eventlist, personalschedule)
    //bundle convention data parcel to their respective activities
    public void gotoEvents() {
        Intent eventIntent = new Intent(ConventionPageActivity.this, EventListActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }

    public void gotoDocs() {
       Intent docIntent = new Intent(ConventionPageActivity.this, DocsPageActivity.class);
        Bundle data = getIntent().getExtras();
        docIntent.putExtra("convention", data.getParcelable("convention"));
       startActivity(docIntent);
    }

    public void gotoPersonal() {
       Intent personalIntent = new Intent(ConventionPageActivity.this, EventListActivity.class);
        Bundle data = getIntent().getExtras();
        personalIntent.putExtra("convention", data.getParcelable("convention"));
       startActivity(personalIntent);
    }


}
