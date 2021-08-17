package com.tsingle.gkude_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    @Override
    public String toString()
    {
        return "id:"+id+"\n"+"username"+username+"\npassword"+password+"\n";
    }
}
