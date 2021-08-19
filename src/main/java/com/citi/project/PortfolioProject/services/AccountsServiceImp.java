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
    public void addSecurity(Securities security, String invest_account_name, String purchase_account_name) {
        Accounts account=getAccountByName(invest_account_name);
        Accounts cashAccount=getAccountByName(invest_account_name);
        double money = security.getHoldings()*security.getCurrent_cost();
        if (cashAccount.getAmount() >= money){
            account.addSecurity(security);
            updateAccountCashAmount(purchase_account_name, -money);
        } else {
            //in case of being poor
        }

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
        History h1 = new History("cash", new Date(), changeInCash);
        account.addHistory(h1);
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


}
