package com.rawee.app.models;

public class MUser extends MBaseModel{
    public String display_name;
    public String email;
    public String photo_url;

    public long first_login;
    public long last_login;

    public MUser() {
        last_login = System.currentTimeMillis();
    }
}
