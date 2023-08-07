package com.example.group1hrmsapp.model;

/**
 * Defines an Observer as part of the Observer design pattern.
 * Observers are updated by Subjects they are observing when
 * certain changes occur.
 */
public interface Observer {

    /**
     * Method called to update the Observer with a new TimeOffRequest.
     * @param timeOffRequest The new TimeOffRequest that the Observer is being updated with.
     */
    void update(TimeOffRequest timeOffRequest);
}
