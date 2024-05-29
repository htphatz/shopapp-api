package com.example.shopapp.services;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.dtos.MailBody;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.entities.OTPReset;
import com.example.shopapp.exceptions.OTPExpiredException;
import com.example.shopapp.exceptions.PermissionDenyException;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.entities.Role;
import com.example.shopapp.entities.User;
import com.example.shopapp.repositories.OTPResetRepository;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Data
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final OTPResetRepository otpResetRepository;
    private final MailService mailService;

    @Override
    // Register
    public User createUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
        // Kiem tra email da dang ky chua
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exist");
        }
        Role existingRole = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (existingRole.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register an admin account");
        }
        // Convert userDTO -> user
        User newUser = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
        newUser.setRole(existingRole);
        newUser.setActive(true);
        // Kiem tra neu co accountID, khong yeu cau password
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Invalid Email or Password");
        }
        User existingUser = optionalUser.get();
        // Check tài khoản có bị khóa không
        if (!existingUser.isActive()) {
            throw new PermissionDenyException("Account has been blocked");
        }
        // Check password
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Wrong Email or Password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!"));
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

    @Override
    public void verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!"));

        int otp = generateOTP();

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Thank you for requesting to reset your account password." + "\n"
                        + "\nYour OTP code is " + otp + ". \nThis OTP code is valid for 2 minutes."
                        + "\nPlease enter the OTP code to reset the password for your account." + "\n"
                        + "\nNote: This OTP code is only for you and must not be shared with anyone else." + "\n"
                        + "\nBest regards,"
                        + "\nFrom Group 1 with love")
                .subject("Account confirmation - OTP")
                .build();

        OTPReset otpReset = OTPReset.builder()
                .otp(otp)
                .expirationDate(new Date(System.currentTimeMillis() + 2 * 60 * 1000)) // 2minutes
                .user(user)
                .build();

        mailService.sendSimpleMessage(mailBody);
        otpResetRepository.save(otpReset);
    }

    @Override
    public void verifyOtp(int otp, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!"));
        OTPReset otpReset = otpResetRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid OTP for email " + email));
        if (otpReset.getExpirationDate().before(Date.from(Instant.now()))) {
            otpResetRepository.deleteById(otpReset.getId());
            throw new OTPExpiredException("OTP has expired!");
        }
    }

    @Override
    @Transactional
    public void changePassword(String password, String newPassword) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResourceNotFoundException("Password not matches");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void disableUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void enableUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(true);
            userRepository.save(user);
        }
    }

    private Integer generateOTP() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

}
