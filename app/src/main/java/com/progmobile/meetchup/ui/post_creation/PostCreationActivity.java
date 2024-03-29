package com.progmobile.meetchup.ui.post_creation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.progmobile.meetchup.R;
import com.progmobile.meetchup.utils.ChildActivity;


public class PostCreationActivity extends ChildActivity {
    public static final String EXTRA_EVENT_ID = "event_id";
    private PostCreationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);
        setActionBarTitle(getString(R.string.new_post));
        viewModel = ViewModelProviders.of(this).get(PostCreationViewModel.class);
        Intent intent = getIntent();
        viewModel.event_id = intent.getStringExtra(EXTRA_EVENT_ID);
    }
}
