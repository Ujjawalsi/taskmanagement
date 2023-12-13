package com.taskmanagement.taskmanagement.authentication;


import com.taskmanagement.taskmanagement.entities.Role;
import com.taskmanagement.taskmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JwtRequest jwtRequest;

    @Autowired
    public CustomUserDetailsService(JwtRequest jwtRequest) {
        this.jwtRequest = jwtRequest;
    }

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = getPasswordByUsername(username);

        Set<Role> roles = getRolesByUsername(username);
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());

        return User.builder()
                .username(username)
                .password(jwtConfig.passwordEncoder().encode(password))
                .authorities(authorities)
                .build();
    }

    private Set<Role> getRolesByUsername(String username) {
        com.taskmanagement.taskmanagement.entities.User userByUserName = userService.findUserByUserName(username);
        return userByUserName.getRoles();
    }

    private String getPasswordByUsername(String username) {
        com.taskmanagement.taskmanagement.entities.User userByUserName = this.userService.findUserByUserName(username);
        return userByUserName.getPassword();
    }



}
