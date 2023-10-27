package com.manasvi.Project.Services;

import com.manasvi.Project.Entiry.Status;
import com.manasvi.Project.Entiry.User;
import com.manasvi.Project.Entiry.UserConstant;
import com.manasvi.Project.Entiry.UserModel;
import com.manasvi.Project.Exceptions.ItemExistsException;
import com.manasvi.Project.Exceptions.ResourceNotFoundException;
import com.manasvi.Project.Repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public Page<User> getAllUsers(Pageable page) {
        return userRepository.findAll(page);
    }

    @Override
    public User createUser(UserModel user) {
        if(userRepository.existsByEmail(user.getEmail()))
        {
            throw new ItemExistsException("User already registered with this email ");
        }

        User newUser=new User();
        BeanUtils.copyProperties(user, newUser);
        newUser.setRoles(UserConstant.DEFAULT_ROLE);
        newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
        newUser.setStatus(Status.INACTIVE);
        String randomCode = RandomString.make(64);
        newUser.setVerificationCode(randomCode);
        sendVerificationEmail(newUser);
        return userRepository.save(newUser);
    }


    @Override
    public User readUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with the id :"+id));
    }

    @Override
    public User updateUser(UserModel user, Long id) {
        User existingUser=readUser(id);
        existingUser.setUsername(user.getUsername() != null ? user.getUsername():existingUser.getUsername());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail():existingUser.getEmail());
        existingUser.setRoles(user.getRoles() != null ? user.getRoles():existingUser.getRoles());
        existingUser.setStatus(user.getStatus() != null ? user.getStatus() :existingUser.getStatus());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser=readUser(id);
        userRepository.delete(existingUser);
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found for the email"+email));
    }

    @Override
    public Optional<User> getLoggedInUserDetails() {
        User existingUser=getLoggedInUser();
        existingUser.getEmail();
        return userRepository.findByEmail(getLoggedInUser().getEmail());
    }

    @Override
    public void updatePassword(UserModel user) {

        String rs;
        User existingUser=getLoggedInUser();
        BeanUtils.copyProperties(user, existingUser);
        existingUser.setPassword(bcryptEncoder.encode(existingUser.getPassword()));
        rs=existingUser.getPassword();

//        userRepository.save(existingUser);

//        if(bcryptEncoder.matches(oldpassword,existingUser.getPassword()))
//        {
//            existingUser.setPassword(bcryptEncoder.encode(newpassword));
//        }
//        else {
//            return "Please enter correct old password";
//        }
//        return "Password updated successfully";

    }

    @Override
    public boolean verify(String verificationCode) {
        User existingUser=userRepository.findByVerificationCode(verificationCode);
        if (existingUser == null ) {
            return false;
        } else {
            existingUser.setVerificationCode(null);
            existingUser.setStatus(Status.ACTIVE);
            userRepository.save(existingUser);
            return true;
        }
    }

    @Override
    public void sendVerificationEmail(User user) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("sanidhya.official289@gmail.com");
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:7788/verify?code="+user.getVerificationCode());

        emailService.sendEmail(mailMessage);
    }

}
