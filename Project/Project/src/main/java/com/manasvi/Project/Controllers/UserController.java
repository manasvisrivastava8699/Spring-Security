package com.manasvi.Project.Controllers;

import com.manasvi.Project.Entiry.User;
import com.manasvi.Project.Entiry.UserModel;
import com.manasvi.Project.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @GetMapping("/user/{id}")
    public ResponseEntity<User> readUser(@PathVariable Long id) {
        return new ResponseEntity<User>(userService.readUser(id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/user/{id}")
    public String updateUser(@RequestBody UserModel user, @PathVariable Long id)
    {
        userService.updateUser(user, id);
        return "User updated successfully";
    }

    @CrossOrigin
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User has been deleted successfully";
    }

    @CrossOrigin
    @GetMapping("/user")
    public List<User> findAllUsers(Pageable page) {
        return userService.getAllUsers(page).toList();
    }

    @CrossOrigin
    @GetMapping("/userloggedin")
    public Optional<User> findLoggedinUser(Pageable page) {
        return userService.getLoggedInUserDetails();
    }

    @CrossOrigin
    @RequestMapping(value="/changepassword", method= {RequestMethod.PUT})
    public void updatePassword(@RequestBody UserModel user) {
        userService.updatePassword(user);
    }

    @RequestMapping(value="/verify", method= {RequestMethod.GET, RequestMethod.POST})
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "Congratulations your account has been activated successfully.Please go and sign-in.";
        } else {
            return "Please contact admin.";
        }
    }
}
