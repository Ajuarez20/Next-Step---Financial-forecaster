package com.NextStep.nextstep.repository;

import com.NextStep.nextstep.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

// Adding <UserAccount, Long> fixes the "incompatible types" error
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByEmail(String email);
}
