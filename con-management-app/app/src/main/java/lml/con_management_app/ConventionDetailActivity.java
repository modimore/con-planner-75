package lml.con_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import utils.Convention;

public class ConventionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convention_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle data = getIntent().getExtras();
        Convention c = data.getParcelable("convention");
        getSupportActionBar().setTitle(c.getName() + " Details");

        //set detail text
        writeDetails(c);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_return);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPrevious();
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void writeDetails(Convention c){
        TextView textView = (TextView) findViewById(R.id.textView);
        String text = String.format("Start Time: %s\n\nEnd Time: %s\n\nLocation: %s\n\nDescription: %s", c.getStart(),
                c.getEnd(), c.getLocation(), c.getDescription());
        textView.setText(text);
    }

    public void gotoPrevious() {
        Intent eventIntent = new Intent(ConventionDetailActivity.this, ConventionPageActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }
}
