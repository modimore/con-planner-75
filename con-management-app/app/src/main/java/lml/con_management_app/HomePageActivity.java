package lml.con_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import utils.*;

//TODO: persistent conventions
public class HomePageActivity extends AppCompatActivity {

    //Button conventionButton;
    Button searchButton;

    //Convention searchResult; //this is bad code practice; todo: fix IT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        searchButton = (Button) findViewById(R.id.searchButton_id);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoSearch();
            }
        });

        // Get list of downloaded convention
        // SearchActivity should download the selected convention, showing it here.
        List<Convention> conventions = AppUtils.getDownloadedConventions();
        if(conventions != null) {
            for(Convention c : conventions) {
                populateButton(c);
            }
        }

        /*
        searchResult = null;
        Intent homePageIntent = new Intent(HomePageActivity.this, EventListActivity.class);
        Bundle data = getIntent().getExtras();

        if(data != null) {
            searchResult = data.getParcelable("convention");
            if(searchResult != null) {
                populateButton(searchResult);
            }
        }

        /*****GARBAGE FOR TESTING
        //find convention using searchconvention
        List<Convention> conventions = AppUtils.getDownloadedConventions();

        final Convention c = getConvention("DOGS");

        //per conventions found that fit that search term, create buttons
        //within each button:
        //  click to goto convention page, send parceled convention data
        if (conventions != null) {
            //add loop for returning multiple via SearchConventionsTask
            conventionButton = (Button) findViewById(R.id.conventionButton_id);
            conventionButton.setText(c.getName());
            conventionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final Convention c = getConvention("DOGS");

                    if (c != null) {
                        gotoConventionPage(c);
                    }
                    else {
                        conventionButton.setText("nope :(");
                    }
                }
            });
        }
        */
    }

    public void populateButton(final Convention result){
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.homeLayout_id);

            Button button1=new Button(this);
            thisDangLayout.addView(button1);
            button1.setText(result.getName());
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    gotoConventionPage(result);
                }
            });

    }

    //function for convention buttons

    public void gotoConventionPage(Convention c) {
        //create an Intent
        Intent conventionIntent = new Intent(HomePageActivity.this, ConventionPageActivity.class);
        //Bundle Convention Parcel into the Intent
        conventionIntent.putExtra("convention", c);
        //jump to the next Activity with Parcel in hand
        startActivity(conventionIntent);
    }

    //function for the search button, leads to search page
    public void gotoSearch() {
        Intent searchIntent = new Intent(HomePageActivity.this, SearchPageActivity.class);
        startActivity(searchIntent);
    }

    //*****GARBAGE FOR TESTING

    //Download convention for given search term (move to search page after, hard coded for test)
    public Convention getConvention(String searchTerm) {
        Convention c;
        try {
            c = new DownloadConventionTask().execute(searchTerm).get();
        } catch (Exception ex) {
            Log.e("Results", ex.getMessage(), ex);
            c = null;
        }

        return c;
    }
    //********



}