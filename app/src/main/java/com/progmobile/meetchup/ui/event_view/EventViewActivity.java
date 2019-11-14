package com.progmobile.meetchup.ui.event_view;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.progmobile.meetchup.R;
import com.progmobile.meetchup.ui.invitation.InvitationKeyActivity;
import com.progmobile.meetchup.utils.ChildActivity;
import com.progmobile.meetchup.utils.SnackbarFactory;


/**
 * [EventViewActivity] handles the event visualisation throw the feed, the album and the to-do list.
 * [EventViewActivity] requires an extra string in the intend to load the appropriate event.
 * <p>
 * ## INTENT COMMUNICATION
 * The intent extra string is the event ID and the extra name has to be [EXTRA_EVENT_ID].
 * <p>
 * <p>
 * ## ARCHITECTURE
 * This activity can contains three fragments :
 * <p>
 * - FeedFragment
 * - AlbumFragment
 * - ToDoFragment
 * <p>
 * The entire business logic for these fragments and this activity is handled by the
 * [EventViewViewModel]
 * <p>
 * <p>
 * ## PURPOSE
 * This class handle the event view. So the activity displays a BottomNavigationBar with the
 * following three tabs :
 * <p>
 * - Feed
 * - Album
 * - ToDoList
 * <p>
 * Each tab corresponds to one fragment. When the user click on one tab, the associated fragment
 * is inflate in the activity layout. The navigation system is handled with :
 * - The BottomNavigationView view which defines the menu corresponding to the 3 tab (see activity layout)
 * - The menu previously mentioned menu (see event_view_bottom_nav_menu.xml menu file)
 * - The fragment view which defines the navigation graph (see activity layout)
 * - The previously mentioned navigation graph (see event_view_navigation.xml navigation file)
 * - The navigation controller defined in this class
 * <p>
 * The last thing this activity does is to set the action bar title according to the event name.
 * The title of the event is provided by the ViewModel associated with this activity. See
 * [EventViewViewModel]
 * <p>
 * TODO 1: Add loading text until the title is available
 * TODO 2: Handle event loading error (wrong id for example)
 * TODO 3: Delegate ViewModel responsibilities to three sub-viewmodels for each fragment
 */
public class EventViewActivity extends ChildActivity {

    public static final String EXTRA_EVENT_ID = "com.example.appprojet.event_id";

    EventViewViewModel viewModel;
    String eventId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);


        Intent intent = getIntent();
        eventId = intent.getStringExtra(EXTRA_EVENT_ID);

        viewModel = ViewModelProviders.of(this).get(EventViewViewModel.class);
        viewModel.initEventMetaData(this, eventId);

        viewModel.eventTitleLive.observe(this, event -> {
            if (actionBar != null)
                actionBar.setTitle(event);
        });

        BottomNavigationView navView = findViewById(R.id.event_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.event_view_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_view_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_view_share_event_item:
                Intent invitIntent = new Intent(this, InvitationKeyActivity.class);
                invitIntent.putExtra(InvitationKeyActivity.EXTRA_EVENT_ID, eventId);
                startActivity(invitIntent);
                break;
            case R.id.event_view_quit_item:
                showQuitDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showQuitDialog() {
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("No longer participate ?")
                .setMessage("This event will no longer be available in your event list.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(getString(R.string.quit_event_btn), null) // we define the listener below to override completely the button behavior
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                viewModel.requestQuitEvent();
                viewModel.quitingRequestDone.observe(this, isQuitting -> {
                    if (isQuitting) {
                        dialogInterface.cancel();
                        finish();
                    } else {
                        SnackbarFactory.showSuccessSnackbar(findViewById(android.R.id.content), "An error occured");
                    }
                });
            });
            viewModel.requestQuitingIsLoading.observe(this, isLoading -> {
                button.setEnabled(!isLoading);
                button.setText(isLoading ? R.string.loading_btn : R.string.quit_event_btn);
            });
        });

        dialog.show();
    }
}