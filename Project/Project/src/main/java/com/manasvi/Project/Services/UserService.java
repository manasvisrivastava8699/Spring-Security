package com.manasvi.Project.Services;

import com.manasvi.Project.Entiry.User;
import com.manasvi.Project.Entiry.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> getAllUsers(Pageable page);

    User createUser(UserModel user);

    User readUser(Long id);

    User updateUser(UserModel user,Long id);

    void deleteUser(Long id);

    User getLoggedInUser();

    Optional<User> getLoggedInUserDetails();

    void updatePassword(UserModel user) ;

    public boolean verify(String verificationCode);

    void sendVerificationEmail(User user);


}