package com.NextStep.nextstep.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;
    private String category;
    private String description;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_profile_id")
    @JsonIgnore
    private FinancialProfile financialProfile;

    public Expense() {
        this.date = LocalDate.now();
    }

    public Expense(Double amount, String category, String description) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = LocalDate.now();
    }

    // Getters and Setters
    public Integer getId() { 
        return id; 
    }
    public void setId(Integer id) { 
        this.id = id; 
    }

    public Double getAmount() { 
        return amount; 
    }
    public void setAmount(Double amount) { 
        this.amount = amount; 
    }

    public String getCategory() { 
        return category; 
    }
    public void setCategory(String category) { 
        this.category = category; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }

    public LocalDate getDate() { 
        return date; 
    }
    public void setDate(LocalDate date) { 
        this.date = date; 
    }

    public FinancialProfile getFinancialProfile() { 
        return financialProfile; 
    }
    public void setFinancialProfile(FinancialProfile financialProfile) { 
        this.financialProfile = financialProfile; 
    }
}
