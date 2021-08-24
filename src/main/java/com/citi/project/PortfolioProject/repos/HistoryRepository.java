package com.citi.project.PortfolioProject.repos;

import com.citi.project.PortfolioProject.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    public Iterable<History> findByAccountType(String accountType);

    public Iterable<History> findByTransactionDateIsAfter(Date date);


}
