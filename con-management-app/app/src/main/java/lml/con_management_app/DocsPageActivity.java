package lml.con_management_app;

import android.content.Intent;
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

public class DocsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle data = getIntent().getExtras();
        Convention con = data.getParcelable("convention");

        for(Document d : con.getDocuments()) {
            Log.d("DocsAct", "Adding document: " + d.getDisplayName());
            addDocumentButton(d);
        }


        //"up" button - links back to parent activity defined in manifest
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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

    }

    public void viewDocument(Document doc)
    {
        String uri = "http://127.0.0.1:3000/" + doc.getLocation();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }
}
