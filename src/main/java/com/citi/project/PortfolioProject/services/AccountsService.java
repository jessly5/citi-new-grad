package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;

import java.io.IOException;
import java.util.Map;

public interface AccountsService {

    public Iterable<Accounts> getAccounts();
    public Accounts getAccountById(Integer id);
    public Accounts getAccountByName(String name);

    public Accounts addNewAccount(Accounts account);

    public Iterable<Accounts> getAccountByType(String type);
    public Securities addSecurity(Securities security) throws IOException;

    public void updateAccountCashAmount(Integer account_id, double changeInCash);
    public void removeAllSecurityBySymbol(String symbol, Integer invest_account_id, Integer cash_account_id);
    public void removeSomeSecuritiesBySymbol(String symbol, Integer invest_account_id, Integer cash_account_id, int quantity);
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
    public Iterable<HistoryData> getInvestmentYearHistory();

    public String deletAccount(Integer id);

    public Securities getInfoOnPotentialSecurity(String symbol);


}
