package com.svalero.globalFeed.exception;

public class PostNotFoundException extends Exception{
    public PostNotFoundException(){
        super("Post not found");
    }
}
