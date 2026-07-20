package com.NextStep.nextstep.controller;

import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.Service.UserAccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    public UserAccount registerUser(@RequestBody UserAccount userAccount) {
        return userAccountService.registerUser(userAccount);
    }

    @PostMapping("/login")
    public UserAccount loginUser(@RequestBody UserAccount loginRequest) {
        return userAccountService.loginUser(
            loginRequest.getEmail(),
            loginRequest.getPassword() // Fixed getpassword() -> getPassword()
        );
    }
}
