package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;

public interface AccountsService {

    public Iterable<Accounts> getAccounts();
    public Accounts getAccountById(Integer id);
    public Accounts getAccountByName(String name);
    public Iterable<Accounts> getAccountByType(String type);
    public void addSecurity(Securities security, String invest_account_name, String cash_account_name);
    public void removeSecurity(Securities security, String invest_account_name, String cash_account_name);
    public void updateAccountCashAmount(String account_name, double changeInCash);
    public void removeAllSecurityBySymbol(String symbol, String invest_account_name, String cash_account_name);
    public void removeSomeSecuritiesBySymbol(String symbol, String invest_account_name, String cash_account_name, int quantity);
    public void calculateInvestmentSummary();

}
