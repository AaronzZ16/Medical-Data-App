package com.medicaldataapp.service;

import com.medicaldataapp.entity.User;
import com.medicaldataapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User save(User user) {
        // 记录用户注册时的信息
        System.out.println("Registering user: " + user.getUsername());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        System.out.println("Encoded password: " + encodedPassword);

        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // 记录用户信息加载时的信息
        System.out.println("Found user: " + user.getUsername());
        System.out.println("Stored password: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getRole().equals("ADMIN") ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    // 新增的方法，用于在Controller中使用
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
