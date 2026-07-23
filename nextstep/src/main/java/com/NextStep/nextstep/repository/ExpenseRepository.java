package com.NextStep.nextstep.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NextStep.nextstep.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Integer>{

	List<Expense> findByFinancialProfileId(Integer financialProfileId);
}
