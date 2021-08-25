package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;

import java.io.IOException;
import java.util.Map;

public interface AccountsService {

    public Iterable<Accounts> getAccounts();
    public Accounts getAccountById(Integer id);
    public Accounts getAccountByName(String name);
    public void addNewAccount(Accounts account);
    public Iterable<Accounts> getAccountByType(String type);
    public Securities addSecurity(Securities security) throws IOException;
//    public void removeSecurity(Securities security, String invest_account_name, String cash_account_name);
    public void updateAccountCashAmount(String account_name, double changeInCash);
    public void removeAllSecurityBySymbol(String symbol, String invest_account_name, String cash_account_name);
    public void removeSomeSecuritiesBySymbol(String symbol, String invest_account_name, String cash_account_name, int quantity);
    public void calculateInvestmentSummary();
    public Iterable<Accounts> updateAllSecurityInfo();
    public Iterable<Securities> getAllSecurities();
    public Double summarizeCash();
    public Double summarizeInvsetments();
    public Double summarizeNetWorth();

    public Iterable<Securities> getTopDailyPerformers();
    public Iterable<Securities> getWorstDailyPerformers();
    public Iterable<Securities> getSecuritiesInAccount(Integer id);
    public Map<String, Double> getAllAccountChanges();
    public Iterable<Double> getInvestmentYearHistory();


}
