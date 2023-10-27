package com.manasvi.Project.Security;

import com.manasvi.Project.Entiry.Status;
import com.manasvi.Project.Entiry.User;
import com.manasvi.Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for the email : "+ email));
        if(existingUser.getStatus()== Status.INACTIVE)
        {
            return null;

        }
        else {
            return new org.springframework.security.core.userdetails.User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
        }

    }

}

