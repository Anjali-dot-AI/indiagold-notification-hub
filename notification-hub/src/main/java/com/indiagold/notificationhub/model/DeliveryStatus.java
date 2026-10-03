package com.indiagold.notificationhub.model;

/**
 * SUCCESS   -> channel sender ran without error
 * FAILED    -> channel sender threw an error (e.g. missing contact info)
 * SKIPPED   -> user had NOT opted into this channel, so we deliberately did not attempt it
 */
public enum DeliveryStatus {
    SUCCESS,
    FAILED,
    SKIPPED
}
