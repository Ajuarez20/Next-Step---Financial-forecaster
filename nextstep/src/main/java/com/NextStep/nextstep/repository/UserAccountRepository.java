package com.NextStep.nextstep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.NextStep.nextstep.entity.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer>{

}

