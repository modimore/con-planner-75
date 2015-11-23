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


//Homepage Activity - Allows user to search for conventions (links to SearchPageActivity)
//  Acts as a hub for conventions downloaded via Search
public class HomePageActivity extends AppCompatActivity {

    //Button conventionButton;
    Button searchButton;
    Button clearButton;

    //Exists so that this view is forced to refresh its contents on returning.
    //This handles the case where a convention is updated and this view in stack
    // contains an out of date convention as its reference.
    @Override
    protected void onResume() {
        super.onResume();
        setupView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
    }

    //for onCreate; links to SearchPage, builds buttons based on downloaded conventions
    protected void setupView() {
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
        if (conventions != null) {
            for (Convention c : conventions) {
                populateButtons(c);
            }
        }

        //clearButton= (Button) findViewById(R.id.clearButton_id);
        /*clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clear();
            }
        });*/
    }

    //Create a button from a given Convention, links to ConventionPageActivity
    // Parcels Convention data over to that activity
    public void populateButtons(final Convention result) {
        LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.homeLayout_id);

        Button button1 = new Button(this);
        thisDangLayout.addView(button1);
        button1.setText(result.getName());
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gotoConventionPage(result);
            }
        });

    }

    //Function for convention buttons
    public void gotoConventionPage(Convention c) {
        //create an Intent
        Intent conventionIntent = new Intent(HomePageActivity.this, ConventionPageActivity.class);
        //Bundle Convention Parcel into the Intent
        conventionIntent.putExtra("convention", c);
        //jump to the next Activity with Parcel in hand
        startActivity(conventionIntent);
        onRestart();
    }

    //Clear downloaded conventions
    public void clear() {
        AppUtils.deleteDownloadedConventions();
        //TODO: refresh the layout
    }



    //Function for the search button, leads to search page
    public void gotoSearch() {
        Intent searchIntent = new Intent(HomePageActivity.this, SearchPageActivity.class);
        startActivity(searchIntent);
    }
}