package com.svalero.globalFeed.service;

import com.svalero.globalFeed.config.GlobalFeedConfigMapper;
import com.svalero.globalFeed.domain.Post;
import com.svalero.globalFeed.domain.User;
import com.svalero.globalFeed.domain.dto.PostDTO;
import com.svalero.globalFeed.exception.PostNotFoundException;
import com.svalero.globalFeed.exception.UserNotFoundException;
import com.svalero.globalFeed.repository.PostRepository;
import com.svalero.globalFeed.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GlobalFeedConfigMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public List<Post> findAll() {
        logger.info("findAll");
        Sort sort = Sort.by(Sort.Direction.DESC, "postDate");
        return postRepository.findAll(sort);
    }

    @Override
    public Post findById(long id) throws PostNotFoundException {
        logger.info("findById - id: " + id);
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    @Override
    public List<Post> findByUsername(String username) {
        logger.info("findByUsername - username: " + username);
        User user = userRepository.findByUsername(username);
        return postRepository.findByUserPost(user);
    }

    @Override
    public Post addPost(PostDTO postDTO) throws UserNotFoundException {
        logger.info("addPost - postDTO: " + postDTO);
        User user = userRepository.findById(postDTO.getUserPost()).orElseThrow(UserNotFoundException::new);
        logger.info("addPost - user: " + user);
        Post post = new Post();
        post.setPostDate(LocalDateTime.now());
        post.setMessage(postDTO.getMessage());
        post.setUserPost(user);
        post.setLikes(0);
        logger.info("addPost - post: " + post);
        return postRepository.save(post);
    }

    @Override
    public Post modifyPost(long id, PostDTO postDTO) throws PostNotFoundException {
        logger.info("modifyPost - postDTO: " + postDTO);
        Post existingPost = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        //Permitimos editar el mensaje, aunque no se deberia, pero nos obligan...
        //Aun asi, hemos conseguido lo que twitter ha tardado años, y gratis
        existingPost.setMessage(postDTO.getMessage());

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(long id) throws PostNotFoundException {
        logger.info("deletePost - id: " + id);
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
    }

    @Override
    public void deleteAllPostFromUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException();
        }
        List<Post> postList = postRepository.findByUserPost(user);
        postRepository.deleteAll(postList);
    }
}
