package com.rawee.app.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MBaseModel {

    @Exclude
    public long getUtc_time() {
        return utc_time;
    }
    @Exclude
    public String getId() {
        return id;
    }

    public long utc_time; //timestamp
    public String id; //document ID

    public MBaseModel() {
        this.utc_time = System.currentTimeMillis();
    }
}
