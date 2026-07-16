package com.NextStep.nextstep.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    
    
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
    
    public void setId(int id) {
    	this.id=id;
    }
    
    public void setFirstName(String firstname) {
    	this.firstname=firstname;
    }
    public void setLastname(String lastname) {
    	this.lastname=lastname;
    }
    public void setEmail(String email) {
    	this.email=email;
    }
    public void setpassword(String password) {
    	this.password=password;
    }
    
}

