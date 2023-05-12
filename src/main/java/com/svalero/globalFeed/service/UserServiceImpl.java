package com.svalero.globalFeed.service;

import com.svalero.globalFeed.domain.Post;
import com.svalero.globalFeed.domain.User;
import com.svalero.globalFeed.domain.dto.UserDTO;
import com.svalero.globalFeed.exception.UserNotFoundException;
import com.svalero.globalFeed.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<User> findAll() {
        logger.info("findAll");
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        logger.info("findById - id:" + id);
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByUsername(String username) {
        logger.info("findByUsername - username: " + username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User addUser(UserDTO userDTO) {
        logger.info("addUser - User added: " + userDTO);
        User user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        modelMapper.map(userDTO, user);
        user.setPostList(new ArrayList<>());
        user.setRegisterDate(LocalDate.now());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User modifyUser(long id, UserDTO userDTO) throws UserNotFoundException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        logger.info("modifyUser - User modified: " + userDTO);
        logger.info("modifyUser - User id: " + id);

        User existingUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        logger.info("modifyUser - Actual user: " + existingUser);
        List<Post> postList = existingUser.getPostList();

        modelMapper.map(userDTO, existingUser);
        existingUser.setPostList(postList);
        existingUser.setRegisterDate(LocalDate.now());
        existingUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(existingUser);
    }

    @Override
    public boolean deleteUser(long id) throws UserNotFoundException {
        logger.info("deleteUser - User id: " + id);

        User existingUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        //TODO: Delete on cascade in POST foreingKey
        if(!existingUser.getPostList().isEmpty()){
            logger.error("deleteUser - This user has references, cannot be deleted");
            return false;
        }
        logger.info("deleteUser - User deleted: " + existingUser);
        userRepository.delete(existingUser);
        return true;
    }
}
