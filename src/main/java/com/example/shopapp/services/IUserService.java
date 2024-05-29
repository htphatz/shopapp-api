package com.example.shopapp.services;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.entities.User;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO);

    String login(String phone, String password);

    List<User> getAllUsers();
    void updatePassword(String email, String newPassword);

    void verifyEmail(String email);

    void verifyOtp(int otp, String email);
    void changePassword(String password, String newPassword);

    void disableUser(Long id);

    void enableUser(Long id);
}
