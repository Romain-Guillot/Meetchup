package com.progmobile.meetchup.ui.event_creation;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.progmobile.meetchup.utils.ChildActivity;


public class EventCreationActivity extends ChildActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle("Create a new new event");
    }

}