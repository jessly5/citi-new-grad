package com.citi.project.PortfolioProject.repos;

import com.citi.project.PortfolioProject.entities.Securities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityRepository extends JpaRepository<Securities, Integer> {



}
