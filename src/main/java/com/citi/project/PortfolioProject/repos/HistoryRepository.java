package com.citi.project.PortfolioProject.repos;

import com.citi.project.PortfolioProject.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    public Iterable<History> findByAccountType(String accountType);
//    public Iterable<History> findAl

    public Iterable<History> findByTransactionDateIsAfter(Date date);
    public Iterable<History> findByTransactionDateIsBetween(Date dateStart, Date end);

    public Iterable<History> findByTransactionDate(Date date);
    public Iterable<History> findByAccountTypeAndTransactionDateIsAfter(String type, Date date);


}
