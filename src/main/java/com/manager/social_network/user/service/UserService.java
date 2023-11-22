package com.manager.social_network.user.service;

import com.manager.social_network.account.dto.SignUpRequest;
import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.mapper.SignUpRequestMapper;
import com.manager.social_network.user.mapper.UserRequestMapper;
import com.manager.social_network.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    UserRepository userRepository;
    UserRequestMapper userRequestMapper;
    SignUpRequestMapper signUpRequestMapper;
    PasswordEncoder encoder;

    public void saveUser(String username, UserRequest userRequest) throws Throwable {
        try {
            User user = userRepository.findByUsername(username).get();
            user.setJob(userRequest.getJob());
            user.setBirthday(userRequest.getBirthday());
            user.setLiving(userRequest.getLiving());
            user.setFullName(userRequest.getFullName());
            userRepository.save(user);
        } catch (Exception e) {
            throw new Throwable("Đã có lỗi xảy ra");
        }
    }

    public void createUser(SignUpRequest request) {
        User user = signUpRequestMapper.dtoToEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCreateAt(Instant.now());
        user.setDeleteFlag(0);
        user.setRole("user");
        userRepository.save(user);
    }

    public void updatePassword(String email, String newPass) {
        User user = userRepository.findByEmail(email).get();
        user.setPassword(encoder.encode(newPass));
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String usm) {
        return userRepository.findByUsername(usm);
    }

    public User findById(Long id) {
        return userRepository.getReferenceById(id);
    }

    public boolean userExits(String userName) {
        return userRepository.findByUsernameAndDeleteFlag(userName, 0).isPresent();
    }

    public boolean userExits(Long id) {
        return userRepository.findByIdAndDeleteFlag(id, 0).isPresent();
    }
}
