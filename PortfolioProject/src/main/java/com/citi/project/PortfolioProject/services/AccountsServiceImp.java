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

import java.util.Date;
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
    public Accounts getAccountByName(String name) {
       Accounts account =  repository.findByName(name);
       return account;
    }

    @Override
    public Iterable<Accounts> getAccountByType(String type) {
        return repository.findByType(type);
    }

    @Override
    public void addSecurity(Securities security, int invest_account_id, int purchase_account_id) {
        Optional<Accounts> accountOptional = repository.findById(invest_account_id);
        if (accountOptional.isPresent()) {
            accountOptional.get().addSecurity(security);
        }
//        historyRepositor
    }

    @Override
    public void removeSecurity(Securities security, int invest_account_id, int cash_account_id) {
        Optional<Accounts> accountOptional = repository.findById(invest_account_id);
        if (accountOptional.isPresent()) {
            accountOptional.get().removeSecurity(security);
        }

    }

    @Override
    public void removeAllSecurityBySymbol(String symbol, int invest_account_id, int cash_account_id) {
        Optional<Accounts> accountOptional = repository.findById(invest_account_id);
//        double totalValue=0;
        if (accountOptional.isPresent()) {
//          update amount in invest account
            double totalValue= accountOptional.get().removeSecurityBySymbol(symbol);
            updateAccountCashAmount(cash_account_id, totalValue);
//          update the history table
            History h1= new History("cash", new Date(), totalValue);
            History h2= new History("investment", new Date(), -totalValue);
            accountOptional.get().addHistory(h2);
            Accounts cashAccount= getAccountById(cash_account_id);
            cashAccount.addHistory(h1);
        }
    }

    @Override
    public void updateAccountCashAmount(int account_id, double changeInCash){
        Accounts account=getAccountById(account_id);
        account.setAmount(account.getAmount()+changeInCash);
    }







}
