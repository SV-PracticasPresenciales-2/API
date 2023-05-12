package com.svalero.globalFeed.service;

import com.svalero.globalFeed.domain.Post;
import com.svalero.globalFeed.domain.dto.PostDTO;
import com.svalero.globalFeed.exception.PostNotFoundException;
import com.svalero.globalFeed.exception.UserNotFoundException;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    Post findById(long id) throws PostNotFoundException;
    List<Post> findByUsername(String username);
    Post addPost(PostDTO postDTO) throws UserNotFoundException;
    Post modifyPost(long id, PostDTO postDTO) throws PostNotFoundException;
    void deletePost(long id) throws PostNotFoundException;
    void deleteAllPostFromUsername(String username) throws UserNotFoundException;



}
