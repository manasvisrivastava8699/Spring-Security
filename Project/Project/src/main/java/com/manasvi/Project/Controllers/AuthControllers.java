package com.manasvi.Project.Controllers;

import com.manasvi.Project.Entiry.JwtResponse;
import com.manasvi.Project.Entiry.UserModel;
import com.manasvi.Project.Repository.UserRepository;
import com.manasvi.Project.Security.CustomUserDetailsService;
import com.manasvi.Project.Services.UserService;
import com.manasvi.Project.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
public class AuthControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @CrossOrigin
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserModel authModel) throws Exception {


        authenticate(authModel.getEmail(), authModel.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authModel.getEmail());
        if (userDetails ==null)
        {
            String str="bad";
            return new ResponseEntity<JwtResponse>(new JwtResponse(str), HttpStatus.BAD_REQUEST);
//            throw new Exception("Please activate your account");
        }
        else {
            final String token = jwtTokenUtil.generateToken(userDetails);
            return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
        }
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("User disabled");
        } catch (BadCredentialsException e) {
            throw new Exception("Either username or password is incorrect");
        }
    }

    @CrossOrigin
    @PostMapping("/register")
    public String save(@RequestBody UserModel user)
    {
        userService.createUser(user);
        return "Account created successfully, please confirm your account via the email sent to you.";
    }

}
