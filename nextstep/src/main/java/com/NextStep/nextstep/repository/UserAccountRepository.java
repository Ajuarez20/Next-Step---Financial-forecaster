package com.NextStep.nextstep.repository;

import com.NextStep.nextstep.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    
    // Required by UserAccountService for user login lookup
    UserAccount findByEmail(String email);
    
}