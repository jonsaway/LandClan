package com.jonsaway.landclan.rest;

public enum ParcelStatus {
    NO_CHANGE, // used for update to indicate 'no change' - should not be stored
    SAVED,
    SHORT_LISTED,
    UNDER_CONSIDERATION,
    APPROVED
}
