package com.svalero.globalFeed.service;

import com.svalero.globalFeed.domain.User;
import com.svalero.globalFeed.domain.dto.UserDTO;
import com.svalero.globalFeed.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(long id) throws UserNotFoundException;
    User findByUsername(String username);
    User addUser(UserDTO userDTO);
    User modifyUser(long id, UserDTO userDTO) throws UserNotFoundException;
    boolean deleteUser(long id) throws UserNotFoundException;

}
