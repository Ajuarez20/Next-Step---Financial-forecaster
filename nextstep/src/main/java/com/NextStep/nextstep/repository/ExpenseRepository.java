package com.NextStep.nextstep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.NextStep.nextstep.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
}