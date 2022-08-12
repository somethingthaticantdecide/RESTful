package edu.school21.restful.models.dto;

import lombok.Data;

@Data
public class UserDto {
    private String firstname;
    private String lastname;
    private String roles;
    private String login;
    private String password;
}
