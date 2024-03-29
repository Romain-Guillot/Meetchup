package com.progmobile.meetchup.repositories;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ListenerRegistration;
import com.progmobile.meetchup.models.Event;
import com.progmobile.meetchup.models.Post;
import com.progmobile.meetchup.utils.Callback;

import java.util.List;

/**
 * As operations are asynchronous with the database, the callback system is used to notify client
 * when the operation succeeds or fails : {@link Callback}
 * <p>
 * IMPORTANT : Sometimes the functions require an activity as a parameter.That's because some
 * function adds listeners on objects, so we have to delete these listeners when the client activity
 * is no longer active and so and therefore no longer needs to listen these objects.
 * These functions are generally READ operations (get..., all... in opposition to other types of
 * operations : update..., delete..., create...)
 */
public interface IEventsDataRepository {

    int INVITATION_KEY_MIN_LENGTH = 8;
    int INVITATION_KEY_MAX_LENGTH = 20;
    int EVENT_TITLE_MIN_LENGTH = 6;
    int EVENT_TITLE_MAX_LENGTH = 50;


    /**
     * Get all user events
     * If succeeds list of all events are returned (can be empty)
     * -> Every user event update will be notified through the callback
     */
    ListenerRegistration allEvents(@NonNull Callback<List<Event>> callback);

    /**
     * Get the event corresponding to the ID
     * The event is returned through the callback is it exists, else an exception is returned
     * -> Every event update will be notified through the callback
     */
    void getEvent(@NonNull Activity client, @NonNull String eventID, @NonNull Callback<Event> callback);

    /**
     * An event is created in the database
     * The created event ID is returned through the callback
     */
    void createEvent(@NonNull Event event, @NonNull Callback<String> callback);

    /**
     * Update the invitation key of the event
     * The callback returns the new key if success, an exception else
     */
    void updateEventInvitationKey(@NonNull String eventID, @NonNull String key, @NonNull Callback<String> callback);

    /**
     * Remove the current invitation key of the event
     * Nothing returned if success, else an exception is returned
     */
    void deleteEventInvitationKey(@NonNull String eventID, @NonNull Callback<Void> callback);

    /**
     * Add current user as participant of the event
     * Event ID returned if success, else an exception is returned
     */
    void joinEvent(@NonNull String invitationKey, @NonNull Callback<String> callback);

    /**
     * Remove the user from the event
     * Nothing returned if success, else an exception is returned
     */
    void quitEvent(@NonNull String eventID, @NonNull Callback<Void> callback);

    /**
     * Update event
     * The updated event ID is returned through the callback if success
     */
    void updateEvent(@NonNull String eventID, @NonNull Event event, Callback<String> callback);

    /**
     * Get list of posts
     * The list is returned through the callback if success
     */
    ListenerRegistration allPosts(@NonNull String eventID, Callback<List<Post>> callback);


    ListenerRegistration getPost(@NonNull String event_id, @NonNull String post_id, Callback<Post> callback);

    /**
     * Add a post to the database
     * The created post ID is returned through the callback
     */
    void addPost(@NonNull String eventID, @NonNull Post post, @NonNull Callback<String> callback);

    /**
     * Delete post
     * Nothing returned through the callback (just calling success() or fail() method)
     */
    void deletePost(String eventID ,String postID, Callback<Void> callback);

//    public void loadPostComments(Post post, Callback<Post> callback);

}
