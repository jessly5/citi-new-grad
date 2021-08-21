package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.History;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.repos.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountsServiceImp implements AccountsService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private HistoryRepository historyRepository;

    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Accounts> getAccounts(){
        return repository.findAll();
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public Accounts getAccountById(Integer id){
//        return repository.findById(id);
        Optional<Accounts> accountOptional =  repository.findById(id);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        else return null;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Accounts getAccountByName(String name) {
        System.out.println("Name: " + name);
       Accounts account =  repository.findByName(name);

       return account;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Accounts> getAccountByType(String type) {
        return repository.findByType(type);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Securities addSecurity(Securities security/*, String invest_account_name, String purchase_account_name*/) throws IOException {
        Stock stock = YahooFinance.get(security.getSymbol());
        security.setCurrent_cost(stock.getQuote().getPrice().doubleValue());
        security.setClosing_cost(stock.getQuote().getPreviousClose().doubleValue());

        Accounts account=getAccountById(security.getAccount_id());
        Accounts cashAccount=getAccountByName(security.getCash_account());

        double money = security.getHoldings()*security.getCurrent_cost();

        if (cashAccount.getAmount() >= money){
            account.addSecurity(security);      //will add to existing secuirty if same symbol
            updateAccountCashAmount(security.getCash_account(), -money);
        } else {
            //in case of being poor
            return null;
        }

        repository.save(account);
        return security;
        //TODO: change from void to boolean, in case of failure
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void removeSecurity(Securities security, String invest_account_name, String cash_account_name){
        Accounts account=getAccountByName(invest_account_name);
        account.removeSecurity(security);
        double money = security.getHoldings()*security.getCurrent_cost();
        updateAccountCashAmount(cash_account_name, money);

    }

    @Override
    //@Transactional(propagation= Propagation.REQUIRED)
    public void updateAccountCashAmount(String account_name, double changeInCash){
        Accounts account=repository.findByName(account_name);//getAccountByName(account_name);
        account.setAmount(account.getAmount()+changeInCash);
        History h1 = new History("cash", new Date(), changeInCash, account.getId());
        account.addHistory(h1);
        System.out.println(account.getId());
        repository.save(account);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void removeAllSecurityBySymbol(String symbol, String invest_account_name, String cash_account_name) {
        System.out.println("\n\n\n");
        System.out.println(symbol);
        System.out.println(invest_account_name);
        System.out.println(cash_account_name);
        Accounts account=getAccountByName(invest_account_name);
        Accounts account2=getAccountByName(cash_account_name);
        double totalValue= account.removeSecurityBySymbol(symbol);
        account.updateAmount(-totalValue);
    //    updateAccountCashAmount(cash_account_name, totalValue);
        account2.setAmount(account.getAmount()+totalValue);
        System.out.println("\n\n\n");
        System.out.println("here");
        History h1 = new History("cash", new Date(), totalValue, account.getId());
        account2.addHistory(h1);

        repository.save(account);
        repository.save(account2);

    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void removeSomeSecuritiesBySymbol(String symbol, String invest_account_name, String cash_account_name, int quantity) {
        Accounts account=getAccountByName(invest_account_name);
        double totalValue = account.removeSecurityQuantityBySymbol(symbol, quantity);
        if (totalValue > 0) {
            updateAccountCashAmount(invest_account_name, totalValue);
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void calculateInvestmentSummary() {
        Iterable<Accounts> accounts = getAccountByType("investment");
        for(Accounts acc: accounts){
            List<Securities> sec = acc.getSecuritiesList();
            double total = 0;
            for(Securities s: sec){
                total += s.getHoldings() * s.getCurrent_cost(); // to fix
            }
            acc.setAmount(total);
            repository.save(acc);
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Accounts> updateAllSecuirtyInfo() {
        Iterable<Accounts> accounts = getAccountByType("investment");
        for(Accounts acc: accounts){
            List<Securities> sec = acc.getSecuritiesList();
            for(Securities s: sec) {

                try {
                    System.out.println(s.getSymbol());
                    Stock stock = YahooFinance.get(s.getSymbol());
                    s.setCurrent_cost(stock.getQuote().getPrice().doubleValue()) ;
                    s.setClosing_cost(stock.getQuote().getPreviousClose().doubleValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }catch(NullPointerException e2){
                    e2.printStackTrace();
                }
            }
            repository.save(acc);

        }
        //update amount
        calculateInvestmentSummary();
        return accounts;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<String> summarizePortfolio() {
        Iterable<Accounts> accounts= getAccounts();
        double netWorth=0;
        double cashAmount =0;
        double investmentAmount=0;
        for (Accounts acc: accounts){
            if(acc.getType().equals("cash")){
                cashAmount+=acc.getAmount();
            }else if(acc.getType().equals("investment")){
                investmentAmount+=acc.getAmount();
            }
        }
        netWorth = cashAmount+investmentAmount;

        List<String> summaryData = new ArrayList<>();
        summaryData.add("Net Worth: " + netWorth);
        summaryData.add("Cash Value: " + cashAmount);
        summaryData.add("Investment Value: " + investmentAmount);

        return summaryData;
    }


}
