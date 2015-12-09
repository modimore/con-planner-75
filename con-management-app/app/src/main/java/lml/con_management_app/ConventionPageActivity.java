package lml.con_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutionException;

import utils.*;

//Activity for the Convention Page - acts as a hub for Convention data (Schedule, Personal Schedule, Documents
public class ConventionPageActivity extends AppCompatActivity {

    //BUTTONS FOR THE CONVENTION PAGE:
    //---Schedule of events, Documents, PersonalSchedule
    Button eventsButton;
    Button docsButton;
    Button personalButton;
    Button detailButton;
    Button updateButton;
    Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convention_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set toolbar title to convention name
        Bundle data = getIntent().getExtras();
        Convention c = data.getParcelable("convention");
        getSupportActionBar().setTitle(c.getName());

        //Articulate Buttons:

        eventsButton = (Button) findViewById(R.id.eventsButton_id);
        eventsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoEvents();
            }
        });
        eventsButton.getBackground().setColorFilter(Color.parseColor("#5a97ec"), PorterDuff.Mode.MULTIPLY);
        eventsButton.setTextColor(Color.parseColor("#FFFFFF"));

        docsButton = (Button) findViewById(R.id.docsButton_id);
        docsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoDocs();
            }
        });
        docsButton.getBackground().setColorFilter(Color.parseColor("#5a97ec"), PorterDuff.Mode.MULTIPLY);
        docsButton.setTextColor(Color.parseColor("#FFFFFF"));

        personalButton = (Button) findViewById(R.id.personalButton_id);
        personalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoPersonal();
            }
        });
        personalButton.getBackground().setColorFilter(Color.parseColor("#5a97ec"), PorterDuff.Mode.MULTIPLY);
        personalButton.setTextColor(Color.parseColor("#FFFFFF"));

        detailButton = (Button) findViewById(R.id.detailButton_id);
        detailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoDetail();
            }
        });
        detailButton.getBackground().setColorFilter(Color.parseColor("#5a97ec"), PorterDuff.Mode.MULTIPLY);
        detailButton.setTextColor(Color.parseColor("#FFFFFF"));

        updateButton = (Button) findViewById(R.id.updateButton_id);
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateConvention();
            }
        });
        updateButton.getBackground().setColorFilter(Color.parseColor("#5a97ec"), PorterDuff.Mode.MULTIPLY);
        updateButton.setTextColor(Color.parseColor("#FFFFFF"));

        returnButton = (Button) findViewById(R.id.returnButton_id);
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoPrevious();
            }
        });
        returnButton.getBackground().setColorFilter(Color.parseColor("#51d43c"), PorterDuff.Mode.MULTIPLY);
        returnButton.setTextColor(Color.parseColor("#FFFFFF"));



        //"up" button - links back to parent activity defined in manifest
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //Methods for Button behavior

    public void gotoPrevious() {
        Intent eventIntent = new Intent(ConventionPageActivity.this, HomePageActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }

        //CONVENTION PAGE BUTTONS: lead to respective activities (docs, eventlist, personalschedule)
        //bundle convention data parcel to their respective activities

    public void gotoEvents() {
        Intent eventIntent = new Intent(ConventionPageActivity.this, EventActivity.class);
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
       Intent personalIntent = new Intent(ConventionPageActivity.this, PersonalEventActivity.class);
        Bundle data = getIntent().getExtras();
        personalIntent.putExtra("convention", data.getParcelable("convention"));
       startActivity(personalIntent);
    }

    public void gotoDetail() {
        Intent detailIntent = new Intent(ConventionPageActivity.this, ConventionDetailActivity.class);
        Bundle data = getIntent().getExtras();
        detailIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(detailIntent);
    }

    //Update downloaded data from database
    public void updateConvention() {
        Intent updateIntent = new Intent(ConventionPageActivity.this, ConventionPageActivity.class);
        Bundle data = getIntent().getExtras();
        Convention c = data.getParcelable("convention");
        String con_name = c.getName();
        AppUtils.deleteConvention(c);
        try {
            c = new DownloadConventionTask().execute(con_name).get();

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        updateIntent.putExtra("convention", c);
        startActivity(updateIntent);
        //exit the stack because it references an out of date convention object now.
        finish();
    }

}
