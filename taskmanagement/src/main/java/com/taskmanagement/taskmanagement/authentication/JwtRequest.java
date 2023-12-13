package com.taskmanagement.taskmanagement.authentication;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {
    public  String username;
    public  String password;

}
