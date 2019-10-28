package com.example.appprojet.ui.event_view;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.appprojet.models.Event;
import com.example.appprojet.models.Location;
import com.example.appprojet.models.Post;
import com.example.appprojet.models.User;
import com.example.appprojet.repositories.FirestoreEventsDataRepository;
import com.example.appprojet.repositories.IEventsDataRepository;
import com.example.appprojet.utils.Callback;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class EventViewViewModel extends AndroidViewModel {

    private IEventsDataRepository eventsRepo;

    private Event event = null;

    protected MutableLiveData<String> eventTitleLive = new MutableLiveData<>();
    protected MutableLiveData<String> eventBeginDateLive = new MutableLiveData<>();
    protected MutableLiveData<String> eventDurationLive = new MutableLiveData<>();
    protected MutableLiveData<String> eventLocalisationLive = new MutableLiveData<>();

    protected  MutableLiveData<List<User>> eventParticipantsList = new MutableLiveData<>();
    protected MutableLiveData<List<Post>> eventPosts = new MutableLiveData<>();


    public EventViewViewModel(Application application) {
        super(application);
        eventsRepo = FirestoreEventsDataRepository.getInstance();
    }


    public void initEventMetaData(String eventId) {
        eventsRepo.getEvent(null, eventId, new Callback<Event>() {
            @Override
            public void onSucceed(Event result) {
                event = result;
                setEventMetaDataLive();
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void setEventMetaDataLive() {
        eventTitleLive.setValue(event.getTitle());
        Date dateBegin = event.getDateBegin();
        if (dateBegin != null)
            eventBeginDateLive.setValue(dateBegin.toString());

        Date dateEnd = event.getDateEnd();
        if (dateBegin != null && dateEnd != null)
            eventDurationLive.setValue(getDurationBetweenDate(dateBegin, dateEnd));

        Location localisation = event.getLocalisation();
        if (localisation != null)
            eventLocalisationLive.setValue(getAddressLocalisation(localisation));

        List<User> participants = event.getParticipants();
        if (participants != null && participants.size() > 0)
            eventParticipantsList.setValue(participants);
    }


    public void loadPosts() {
        Log.d(">>>>", "load posts");
        eventsRepo.loadEventPosts(null, event, new Callback<Event>() {
            @Override
            public void onSucceed(Event result) {
                event = result;
                setPostsLive();
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void setPostsLive() {
        List<Post> posts = event.getPosts();
        if (posts != null)
            eventPosts.setValue(posts);
    }


    private String getDurationBetweenDate(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime(); // milliseconds
        int days = (int) Math.round(diff / (1000.*60*60*24)); // to seconds -> to minutes -> to hours -> to days
        int weeks = (int) Math.round(days / 7.);
        int daysAfterLastWeek = days - Math.round(7 * weeks);

        String duration = "";
        if (weeks >= 1)
            duration += (weeks + " week" + (weeks > 1 ? "s" : ""));
        if (daysAfterLastWeek >= 1)
            duration += (" " + daysAfterLastWeek + "day" + (daysAfterLastWeek > 1 ? "s" : ""));

        return duration;
    }

    private String getAddressLocalisation(Location localisation) {
        Geocoder geocoder = new Geocoder(this.getApplication().getApplicationContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(localisation.getLatitude(), localisation.getLongitude(), 1);
            if (addresses.size() >= 1) {
                Address a =  addresses.get(0);
                String address= "";
                if (a.getLocality() != null) address += a.getLocality();
                if (a.getCountryName() != null) address += (", " + a.getCountryName());
                if (!address.isEmpty()) return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(">>>>>>>>", "Empty address");
        return null;
    }






}