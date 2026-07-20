package com.NextStep.nextstep.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="financial_profile_id")
    private FinancialProfile financialProfile;
     
    
    
    public FinancialProfile getFinancialProfile() {
		return financialProfile;
	}
	public void setFinancialProfile(FinancialProfile financialProfile) {
		this.financialProfile = financialProfile;
	}
	public Integer getId() {
    	return id;
    }
    public String getFirstname() {
    	return firstname;
    }
    public String getLastname() {
    	return lastname;
    }
    public String getEmail() {
    	return email;
    }
    public String getPassword() {
    	return password;
    }
    
    public void setId(Integer id) {
    	this.id=id;
    }
    
    public void setFirstname(String firstname) {
    	this.firstname=firstname;
    }
    public void setLastname(String lastname) {
    	this.lastname=lastname;
    }
    public void setEmail(String email) {
    	this.email=email;
    }
    public void setPassword(String password) {
    	this.password=password;
    }
    
}

