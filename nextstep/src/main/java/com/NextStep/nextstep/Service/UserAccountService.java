package com.NextStep.nextstep.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.repository.UserAccountRepository;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public UserAccount registerUser(UserAccount user) {
        System.out.println("New User start here: Enter Information to begin");
        return userAccountRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserAccount loginUser(String email, String password) {
        UserAccount user = userAccountRepository.findByEmail(email);

        // Fixed getpassword() -> getPassword()
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        throw new RuntimeException("Invalid email or password");
    }
}
