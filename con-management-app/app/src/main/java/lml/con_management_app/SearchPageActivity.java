package lml.con_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import utils.Convention;
import utils.DownloadConventionTask;
import utils.SearchConventionsTask;

//TODO: add scroll functionality for > 10 conventions returned
public class SearchPageActivity extends AppCompatActivity {

    Button submitButton;
    EditText searchInput;
    TextView feedbackText;
    List<Convention> conventionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //"up" button - links back to parent activity defined in manifest
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Submit input to the database, dynamically create buttons
        submitButton = (Button) findViewById(R.id.submitButton_id);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchInput = (EditText) findViewById(R.id.searchEditText_id);
                submit(searchInput.getText().toString());
            }
        });

    }

    //TODO:reset after search (might need to get button ids), or put them in a container
    //TODO:make search note case sensitive
    public void submit(String input){

        //<CLEAR BUTTONS//

        //Search database for input
        conventionList = null;
        try {
            conventionList = new SearchConventionsTask().execute(input).get();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Dynamically build buttons for search results
        if(conventionList != null) {

            LinearLayout thisDangLayout = (LinearLayout) findViewById(R.id.layout_id);
            for (int i = 0; i < conventionList.size(); i++) {

                    Button button1=new Button(this);
                    button1.setText(conventionList.get(i).getName());
                    button1.setId(i);
                    thisDangLayout.addView(button1);
                    button1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            gotoHome(conventionList.get(view.getId()));
                        }
                    });
            }
        }
        else{
            //....or tell user they FAILED
            feedbackText = (TextView)findViewById(R.id.feedbackText_id);
            feedbackText.setText("No such convention. Try another search term!");
        }

    }

    //Convention buttons parcel Convention data and send it back to homepage to build button
    public void gotoHome(Convention c) {
        Intent homeIntent = new Intent(SearchPageActivity.this, HomePageActivity.class);
        try {
            Convention d = new DownloadConventionTask().execute(c.getName()).get();
            if(d == null) {
                return;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        startActivity(homeIntent);
    }



}
