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
    public void addSecurity(Securities security, String invest_account_name, String purchase_account_name) {
        security.setCurrent_cost(security.getPurchase_cost());
        security.setClosing_cost(security.getPurchase_cost());

        System.out.println("\n\n");
        System.out.println(security.getAccountId());
        System.out.println(purchase_account_name);

        Accounts account=getAccountById(security.getAccountId());
        //Accounts cashAccount=getAccountByName(invest_account_name);

        double money = security.getHoldings()*security.getCurrent_cost();

        //if (cashAccount.getAmount() >= money){
        account.addSecurity(security);
            //updateAccountCashAmount(purchase_account_name, -money);
//        } else {
//            //in case of being poor
//        }

        repository.save(account);

        //TODO: change from void to boolean, in case of failure
    }

    @Override

    public void removeSecurity(Securities security, String invest_account_name, String cash_account_name){
        Accounts account=getAccountByName(invest_account_name);
        account.removeSecurity(security);
        double money = security.getHoldings()*security.getCurrent_cost();
        updateAccountCashAmount(cash_account_name, money);

    }

    @Override
    public void updateAccountCashAmount(String account_name, double changeInCash){
        Accounts account=getAccountByName(account_name);
        account.setAmount(account.getAmount()+changeInCash);
        History h1 = new History("cash", new Date(), changeInCash, account.getId());
        account.addHistory(h1);
        System.out.println(account.getId());
        repository.save(account);
    }

    @Override
    public void removeAllSecurityBySymbol(String symbol, String invest_account_name, String cash_account_name) {
        Accounts account=getAccountByName(invest_account_name);
        double totalValue= account.removeSecurityBySymbol(symbol);
        updateAccountCashAmount(cash_account_name, totalValue);

    }

    @Override
    public void removeSomeSecuritiesBySymbol(String symbol, String invest_account_name, String cash_account_name, int quantity) {
        Accounts account=getAccountByName(invest_account_name);
        double totalValue = account.removeSecurityQuantityBySymbol(symbol, quantity);
        if (totalValue > 0) {
            updateAccountCashAmount(invest_account_name, totalValue);
        }
    }

    @Override
    public void calculateInvestmentSummary() {
        Iterable<Accounts> accounts = getAccountByType("investment");
        for(Accounts acc: accounts){
            List<Securities> sec = acc.getSecuritiesList();
            double total = 0;
            for(Securities s: sec){
                total += s.getHoldings() * s.getCurrent_cost(); // to fix
            }
        }
    }

    @Override
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
        return accounts;
    }


}
