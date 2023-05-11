package com.svalero.globalFeed.controller;


import com.svalero.globalFeed.domain.User;
import com.svalero.globalFeed.domain.dto.UserDTO;
import com.svalero.globalFeed.exception.ErrorException;
import com.svalero.globalFeed.exception.UserNotFoundException;
import com.svalero.globalFeed.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.svalero.globalFeed.util.ErrorExceptionUtil.getErrorExceptionResponseEntity;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        logger.info("GET Users");
        List<User> users = userService.findAll();
        logger.info("END GET Users");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam Map<String, String> data) throws UserNotFoundException {
        logger.info("GET User");
        if(data.containsKey("username")){
            String debug = "Data:" + "username " + data.get("username") ;
            logger.info(debug);
            User user = userService.findByUsername(data.get("username"));
            logger.info("END GET User");
            return ResponseEntity.ok(user);
        } else if(data.containsKey("id")){
            String debug = "Data:" + "id " + data.get("id") ;
            logger.info(debug);
            User user = userService.findById(Long.parseLong(data.get("id")));
            logger.info("END GET User");
            return ResponseEntity.ok(user);
        }
        logger.info("GET User: BAD REQUEST");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserDTO userDTO){
        logger.info("POST User");
        User user = userService.addUser(userDTO);
        logger.info("END POST User");
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> modifyUser(@PathVariable long id, @Valid @RequestBody UserDTO userDTO) throws UserNotFoundException {
        logger.info("PUT User");
        User user = userService.modifyUser(id, userDTO);
        logger.info("END PUT User");
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ErrorException> deleteUser(@PathVariable long id) throws UserNotFoundException {
        logger.info("DELETE User");
        boolean result = userService.deleteUser(id);
        logger.info("END DELETE User");
        if(result){
            return ResponseEntity.noContent().build();
        } else {
            ErrorException error = new ErrorException(403, "Delete operation forbidden");
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorException> handleInventoryNotFoundException(UserNotFoundException infe){
        logger.error("User not found exception");
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