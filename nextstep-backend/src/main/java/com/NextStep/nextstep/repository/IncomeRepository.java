package com.NextStep.nextstep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.NextStep.nextstep.entity.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {
	List<Income> findByFinancialProfileId(Integer financialProfileId);
}