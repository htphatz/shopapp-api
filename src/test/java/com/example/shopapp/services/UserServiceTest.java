package com.example.shopapp.services;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
public class UserServiceTest {
    @Test
    public void testEmailDuplicated() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");
        Executable executable = () -> userService.createUser(userDTO);
        Assertions.assertThrowsExactly(DataIntegrityViolationException.class, executable);
    }

    @CsvFileSource(files = "src/test/resources/users.csv", numLinesToSkip = 1, delimiter = ',')
    @ParameterizedTest
    public void createUser(String firstName, String lastName, String email, String password, Long roleId) throws Exception {
        UserDTO userDTO = new UserDTO(firstName, lastName, email, password, password, roleId);
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        userService.createUser(userDTO);
    }
//    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    // Mock

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
//        this.userRepository = userService.getUserRepository();
//        userService.setUserRepository(userRepository);
    }

//    public UserServiceTest(@Autowired UserService userService, @Mock UserRepository userRepository) {
//        this.userService = userService;
//        this.userRepository = userRepository;
//    }
}
