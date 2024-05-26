package com.example.shopapp.services;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.entities.User;

public interface IUserService {
    User createUser(UserDTO userDTO);

    String login(String phone, String password);
}
