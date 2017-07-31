package com.baskoroaji.stage2.models;

/**
 * Created by Macpro on 7/27/17.
 */

public class Review {
    public String reviewName;
    public String reviewContent;
    public String id;
    public String url;

    public Review(String id, String reviewName , String reviewContent, String url){
        this.reviewName = reviewName;
        this.reviewContent = reviewContent;
        this.id = id;
        this.url = url;
    }

}
