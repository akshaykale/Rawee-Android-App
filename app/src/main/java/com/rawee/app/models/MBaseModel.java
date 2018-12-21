package com.rawee.app.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class MBaseModel implements Serializable{

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

    Boolean _isValid() {
        if (id == null || id.equals("")){
            return false;
        } else if (utc_time == 0) {
            return false;
        }
        return true;
    }

    public void _build(){

    }
}