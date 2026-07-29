package com.NextStep.nextstep.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_account")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;

    private Integer failedLoginAttempts = 0;
    private LocalDateTime lockoutUntil;
    private Boolean lockedAccount = false;
    private Boolean accountNonLocked = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financial_profile_id")
    private FinancialProfile financialProfile;

    public Integer getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Integer getFailedLoginAttempts() { return failedLoginAttempts; }
    public LocalDateTime getLockoutUntil() { return lockoutUntil; }
    public Boolean getLockedAccount() { return lockedAccount; }
    public Boolean getAccountNonLocked() { return accountNonLocked; }
    public FinancialProfile getFinancialProfile() { return financialProfile; }

    public void setId(Integer id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFailedLoginAttempts(Integer failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
    public void setLockoutUntil(LocalDateTime lockoutUntil) { this.lockoutUntil = lockoutUntil; }
    public void setLockedAccount(Boolean lockedAccount) { this.lockedAccount = lockedAccount; }
    public void setAccountNonLocked(Boolean accountNonLocked) { this.accountNonLocked = accountNonLocked; }
    public void setFinancialProfile(FinancialProfile financialProfile) { this.financialProfile = financialProfile; }
}
