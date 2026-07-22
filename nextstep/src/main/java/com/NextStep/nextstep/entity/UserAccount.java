package com.NextStep.nextstep.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;
    private String lastname;
    private String email;

    @JsonIgnore
    private String password;

    // Security: Track failed login attempts
    @JsonIgnore
    private Integer failedLoginAttempts = 0;

    @JsonIgnore
    private Boolean accountLocked = false;

    // Connects UserAccount to FinancialProfile
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financial_profile_id")
    private FinancialProfile financialProfile;

    // Getters
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

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public FinancialProfile getFinancialProfile() {
        return financialProfile;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setFinancialProfile(FinancialProfile financialProfile) {
        this.financialProfile = financialProfile;
    }
}
