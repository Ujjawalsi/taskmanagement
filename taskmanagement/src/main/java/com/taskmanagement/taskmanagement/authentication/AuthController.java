package com.taskmanagement.taskmanagement.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private JwtRequest jwtRequest;


    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
        jwtRequest.setUsername(jwtRequest.getUsername());
        jwtRequest.setPassword(jwtRequest.getPassword());
         this.doAuthenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtHelper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userName(userDetails.getUsername())
                .build();
        System.out.println(response.getJwtToken() + " Response");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String userName , String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        try{
            manager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw  new BadCredentialsException("Invalid UserName or Password !! ");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){

        return  "Credentials Invalid !! ";
    }
}
