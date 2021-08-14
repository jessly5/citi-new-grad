package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;

public interface AccountsService {

    public Iterable<Accounts> getAccounts();
    public Accounts getAccountById(Integer id);
    public Accounts getAccountByName(String name);
    public Iterable<Accounts> getAccountByType(String type);
    public void addSecurity(Securities security, int invest_account_id, int cash_account_id);
    public void removeSecurity(Securities security, int invest_account_id, int cash_account_id);

    //sell all securities with given symbol
    public void removeAllSecurityBySymbol(String symbol, int invest_account_id, int cash_account_id);
    public void updateAccountCashAmount(int account_id, double changeInCash);






}
