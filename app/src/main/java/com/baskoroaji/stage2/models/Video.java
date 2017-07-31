package com.baskoroaji.stage2.models;

/**
 * Created by Macpro on 7/25/17.
 */

public class Video {
    public String id;
    public String key;
    public String name;
    public String site;
    public Video(String id,String key, String name, String site){
        this.id = id;
        this.key = key;
        this.site = site;
        this.name = name;
    }
}
