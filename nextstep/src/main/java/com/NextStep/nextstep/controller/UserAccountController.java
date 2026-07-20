package com.NextStep.nextstep.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.NextStep.nextstep.Service.UserAccountService;
import com.NextStep.nextstep.entity.UserAccount;

@RestController
// Change this line to allow any port (the asterisk *)
@CrossOrigin(origins = "*") 
public class UserAccountController {
    
    private final UserAccountService userAccountService;
    
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    
    @PostMapping("/register")
    public UserAccount registerUser(@RequestBody UserAccount user) {
        return userAccountService.registerUser(
            user.getFirstname(),
            user.getLastname(), 
            user.getEmail(), 
            user.getPassword()
        );
    }
}

