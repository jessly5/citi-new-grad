package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;

public interface AccountsService {

    public Iterable<Accounts> getAccounts();
    public Accounts getAccountById(int id);

}
