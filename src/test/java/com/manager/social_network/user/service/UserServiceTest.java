package com.manager.social_network.user.service;

import com.manager.social_network.account.dto.SignUpRequest;
import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.mapper.SignUpRequestMapper;
import com.manager.social_network.user.mapper.UserRequestMapper;
import com.manager.social_network.user.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private SignUpRequestMapper signUpRequestMapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveUserExits() throws Throwable {
        String username = "testUser";
        UserRequest userRequest = new UserRequest();
        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setEmail("Email@example");
        existingUser.setId(27L);
        existingUser.setJob("DEV");
        existingUser.setCreateAt(Instant.now());
        existingUser.getEmail();
        existingUser.getId();
        existingUser.getCreateAt();
        existingUser.getJob();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        userService.saveUser(username, userRequest);

        assertEquals(userRequest.getBirthday(), existingUser.getBirthday());
        assertEquals(userRequest.getLiving(), existingUser.getLiving());
        assertEquals(userRequest.getFullName(), existingUser.getFullName());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testSaveUserDoNotExits() throws Throwable {
        String username = "testUser";
        UserRequest userRequest = new UserRequest();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(Throwable.class, () -> {
            userService.saveUser(username, userRequest);
        });
    }

    @Test
    void testCreateUser() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("newUser");
        request.setPassword("password123");
        request.setEmail("test@example.com");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());

        when(signUpRequestMapper.dtoToEntity(request)).thenReturn(user);

        userService.createUser(request);

        assertEquals(0, user.getDeleteFlag());
        assertEquals("user", user.getRole());
        verify(encoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword() {
        String email = "test@example.com";
        String pass = "Password!1";
        User user = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.updatePassword(email, pass);

        verify(encoder, times(1)).encode(pass);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindByEmail_UserExists_ReturnsUser() {
        String email = "test@example.com";
        User expectedUser = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    public void testFindByEmail_UserDoesNotExist_ReturnsEmpty() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByUsername_UserExists_ReturnsUser() {
        String username = "testUser";
        User expectedUser = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    public void testFindByUsername_UserDoesNotExist_ReturnsEmpty() {
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername(username);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindById_UserExists_ReturnsUser() {
        Long userId = 1L;
        User expectedUser = new User();
        when(userRepository.getReferenceById(userId)).thenReturn(expectedUser);

        User result = userService.findById(userId);

        assertEquals(expectedUser, result);
    }

    @Test
    public void testUserExistsByUsername_UserExists_ReturnsTrue() {
        String username = "existingUser";
        when(userRepository.findByUsernameAndDeleteFlag(username, 0)).thenReturn(Optional.of(new User()));

        boolean result = userService.userExits(username);

        assertTrue(result);
    }

    @Test
    public void testUserExistsByUsername_UserDoesNotExist_ReturnsFalse() {
        String username = "nonexistentUser";
        when(userRepository.findByUsernameAndDeleteFlag(username, 0)).thenReturn(Optional.empty());

        boolean result = userService.userExits(username);

        assertFalse(result);
    }

    @Test
    public void testUserExistsById_UserExists_ReturnsTrue() {
        Long userId = 1L;
        when(userRepository.findByIdAndDeleteFlag(userId, 0)).thenReturn(Optional.of(new User()));

        boolean result = userService.userExits(userId);

        assertTrue(result);
    }

    @Test
    public void testUserExistsById_UserDoesNotExist_ReturnsFalse() {
        Long userId = 2L;
        when(userRepository.findByIdAndDeleteFlag(userId, 0)).thenReturn(Optional.empty());

        boolean result = userService.userExits(userId);

        assertFalse(result);
    }
}
