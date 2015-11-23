package lml.con_management_app;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lml.con_management_app.dummy.DummyContent;
import utils.Event;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */

//test
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    private Event event;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            // If intent arguments have a event object, get it
            if (getArguments().containsKey("event")) {
                event = getArguments().getParcelable("event");

                //also set app bar title to event name
                if (appBarLayout != null) {
                    appBarLayout.setTitle(event.getName());
                }
            }
        }
    }

    //Display Event data passed from EventListActivity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (event != null) {
            ((TextView) rootView.findViewById(R.id.event_detail)).setText(event.getDescription());
        }
        else{
            ((TextView) rootView.findViewById(R.id.event_detail)).setText("test");
        }

        return rootView;
    }
}
