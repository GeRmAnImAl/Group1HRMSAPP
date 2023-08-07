package com.example.group1hrmsapp.model;

/**
 * Represents a Subject in the Observer design pattern. A subject maintains
 * a list of observers and can notify them of changes.
 */
public interface Subject {

    /**
     * Registers a new observer to be notified of changes.
     *
     * @param observer The observer to register.
     */
    void registerObserver(Observer observer);

    /**
     * Removes a previously registered observer. The observer will no longer be
     * notified of changes.
     *
     * @param observer The observer to remove.
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all registered observers of a change.
     */
    void notifyObservers();
}