package com.citi.project.PortfolioProject.repos;

import com.citi.project.PortfolioProject.entities.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Accounts, Integer> {



}
