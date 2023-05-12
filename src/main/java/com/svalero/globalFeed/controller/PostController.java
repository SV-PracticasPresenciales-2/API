package com.svalero.globalFeed.controller;

import com.svalero.globalFeed.domain.Post;
import com.svalero.globalFeed.domain.dto.PostDTO;
import com.svalero.globalFeed.exception.ErrorException;
import com.svalero.globalFeed.exception.PostNotFoundException;
import com.svalero.globalFeed.exception.UserNotFoundException;
import com.svalero.globalFeed.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.svalero.globalFeed.util.ErrorExceptionUtil.getErrorExceptionResponseEntity;

@RestController
public class PostController {
    @Autowired
    PostService postService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(@RequestParam Map<String, String> data) {
        logger.info("GET Posts");
        if (data.isEmpty()) {
            return ResponseEntity.ok().body(postService.findAll());
        } else if (data.containsKey("username")) {
            String debug = "Data:" + "username " + data.get("username");
            logger.info(debug);
            List<Post> posts = postService.findByUsername(data.get("username"));
            logger.info("END GET Posts");
            return ResponseEntity.ok(posts);
        }
        logger.info("GET User: BAD REQUEST");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/post")
    public ResponseEntity<Post> addPost(@Valid @RequestBody PostDTO postDTO) throws UserNotFoundException {
        logger.info("POST Posts");
        Post post = postService.addPost(postDTO);
        logger.info("END POST Posts");
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") long id, @Valid @RequestBody PostDTO postDTO) throws PostNotFoundException {
        logger.info("PUT Posts");
        Post post = postService.modifyPost(id, postDTO);
        logger.info("END PUT Posts");
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<ErrorException> deletePost(@PathVariable("id") long id) throws PostNotFoundException {
        logger.info("DELETE Posts");
        postService.deletePost(id);
        logger.info("END DELETE Posts");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorException> handleUserNotFoundException(UserNotFoundException infe){
        logger.error("User not found exception");
        ErrorException errorException = new ErrorException(404, infe.getMessage());
        return new ResponseEntity<>(errorException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorException> handlePostNotFoundException(UserNotFoundException infe){
        logger.error("Post not found exception");
        ErrorException errorException = new ErrorException(404, infe.getMessage());
        return new ResponseEntity<>(errorException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorException> handleConstraintViolationException(ConstraintViolationException cve){
        logger.error("Constraint violation exception");
        return getErrorExceptionResponseEntity(cve);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorException> handleException(Exception e){
        logger.error("Internal Error ", e.getMessage());
        ErrorException error = new ErrorException(500, "Unexpected Error");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorException> handleMethodArgumentNotValidException(MethodArgumentNotValidException manve){
        logger.error("Datos introducidos erroneos");
        return getErrorExceptionResponseEntity(manve);
    }
}