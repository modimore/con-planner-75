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

        addReturnButton();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addReturnButton(){
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.eventLayout_id);
        Button button1=new Button(this);
        thisDangLayout.addView(button1);
        button1.setText("Return to Convention");
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gotoPrevious();
            }
        });

        button1.getBackground().setColorFilter(Color.parseColor("#51d43c"), PorterDuff.Mode.MULTIPLY);
        button1.setTextColor(Color.parseColor("#FFFFFF"));
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
