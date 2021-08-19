package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecuritiesServiceImp implements SecuritiesService {

    @Autowired
    private SecurityRepository repository;

    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Securities> getSecurities(){
        return repository.findAll();
    }
}
