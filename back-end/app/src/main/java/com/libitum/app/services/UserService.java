package com.libitum.app.services;

import java.util.List;

import com.libitum.app.model.UserDTO;
import com.libitum.app.repository.IUserRepository;

public class UserService {
    
    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAll(){
        return userRepository.findAll();
    }
}
