package com.svalero.globalFeed.repository;

import com.svalero.globalFeed.domain.Post;
import com.svalero.globalFeed.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAll();
    List<Post> findByUserPost(User userPost);
    List<Post> findByMessageIgnoreCase(String message);
}
