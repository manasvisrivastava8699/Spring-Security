package com.manasvi.Project.Entiry;

public class JwtResponse {

    private final String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;


    }
}