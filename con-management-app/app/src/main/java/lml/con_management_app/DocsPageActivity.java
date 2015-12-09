package lml.con_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import utils.Convention;
import utils.Document;

//Activity for the Documents Page - allows user to view Convention Documents
public class DocsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle data = getIntent().getExtras();
        Convention con = data.getParcelable("convention");
        //con.setName("TEST!!!!!!!!!!!!!!!!!!");


        //Set toolbar title
        getSupportActionBar().setTitle(con.getName() + " Documents");

        for(Document d : con.getDocuments()) {
            Log.d("DocsAct", "Adding document: " + d.getDisplayName());
            addDocumentButton(d);
        }

        addReturnButton();

        //"up" button - links back to parent activity defined in manifest
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Methods for Button behavior

    public void addReturnButton(){
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.docLayout_id);
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

    //Go to previous Activity, bundles data back
    public void gotoPrevious() {
        Intent eventIntent = new Intent(DocsPageActivity.this, ConventionPageActivity.class);
        Bundle data = getIntent().getExtras();
        eventIntent.putExtra("convention", data.getParcelable("convention"));
        startActivity(eventIntent);
    }

    //Add buttons based on Documents in Convention
    public void addDocumentButton(final Document doc){
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.docLayout_id);
        Button button1=new Button(this);
        thisDangLayout.addView(button1);
        button1.setText(doc.getDisplayName());
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewDocument(doc);
            }
        });

        button1.getBackground().setColorFilter(Color.parseColor("#5a97ec"), PorterDuff.Mode.MULTIPLY);
        button1.setTextColor(Color.parseColor("#FFFFFF"));

    }

    //View documents - launch phone's default browser to view documents
    public void viewDocument(Document doc)
    {
        String uri = "http://127.0.0.1:3000/" + doc.getLocation();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }
}
