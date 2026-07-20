package com.NextStep.nextstep.Service;
import java.util.Scanner;

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
public UserAccount registerUser( String firstname,String lastname,String email,String password) {
	System.out.println("New User start here: Enter Information to begin" );
	UserAccount user= new UserAccount();
	user.setFirstName(firstname);
	user.setLastname(lastname);
	user.setEmail(email);
	user.setpassword(password);
	
	
	
	return userAccountRepository.save(user);
}
	
}

