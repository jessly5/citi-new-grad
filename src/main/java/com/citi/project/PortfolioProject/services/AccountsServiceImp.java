package com.citi.project.PortfolioProject.services;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.History;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.repos.HistoryRepository;
import com.citi.project.PortfolioProject.repos.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.*;

@Service
public class AccountsServiceImp implements AccountsService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private SecurityRepository securityRepository;

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
    public void addNewAccount(Accounts account) {
        if(account.getAmount()==null){
            account.setAmount(0.0);
        }
        repository.save(account);
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

//    @Override
//    @Transactional(propagation= Propagation.REQUIRED)
//    public void removeSecurity(Securities security, String invest_account_name, String cash_account_name){
//        Accounts account=getAccountByName(invest_account_name);
//        account.removeSecurity(security);
//        double money = security.getHoldings()*security.getCurrent_cost();
//        updateAccountCashAmount(cash_account_name, money);
//
//    }

    @Override
    //@Transactional(propagation= Propagation.REQUIRED)
    public void updateAccountCashAmount(String account_name, double changeInCash){
        Accounts account=repository.findByName(account_name);//getAccountByName(account_name);
        account.setAmount(account.getAmount()+changeInCash);
        History h1 = new History("cash", new Date(), changeInCash, account.getId());
        account.addHistory(h1);
        repository.save(account);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void removeAllSecurityBySymbol(String symbol, String invest_account_name, String cash_account_name) {
        Accounts account=getAccountByName(invest_account_name);

        // maybe try to combine the following 2 lines?
        Iterable<Securities> securitiesToDelete = account.findBySymbol(symbol);
        double totalValue= account.removeSecurityBySymbol(symbol);
        account.updateAmount(-totalValue);
        repository.save(account);
        for(Securities s: securitiesToDelete){
            securityRepository.delete(s);
        }

        updateAccountCashAmount(cash_account_name, totalValue);

    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void removeSomeSecuritiesBySymbol(String symbol, String invest_account_name, String cash_account_name, int quantity) {
        Accounts account=getAccountByName(invest_account_name);
//TODO handle case when quantity=holdings
        double totalValue = account.removeSecurityQuantityBySymbol(symbol, quantity);
        if(totalValue>0.0){
            account.updateAmount(-totalValue);
            repository.save(account);
            updateAccountCashAmount(invest_account_name, totalValue);
        }else if(totalValue==-1.0){     //chosen value is in fact all that is owned
            removeAllSecurityBySymbol(symbol, invest_account_name, cash_account_name);
        }else if(totalValue==0.0){
            // when trying to sell more than owned -- don't let person do this
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
                total += s.getHoldings() * s.getCurrent_cost(); // to fix ????
            }
            acc.setAmount(total);
            repository.save(acc);
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Iterable<Accounts> updateAllSecurityInfo() {
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
    public Iterable<Securities> getAllSecurities() {
        return securityRepository.findAll();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Double summarizeCash() {
        Iterable<Accounts> accounts= getAccountByType("cash");
        double cashAmount =0;
        for (Accounts acc: accounts){
            cashAmount+=acc.getAmount();
        }
        return cashAmount;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Double summarizeInvsetments(){
        Iterable<Accounts> accounts= getAccountByType("investment");
        double investmentAmount=0;
        for (Accounts acc: accounts){
            investmentAmount+=acc.getAmount();
        }
        return investmentAmount;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Double summarizeNetWorth(){
        return summarizeCash()+summarizeInvsetments();
    }

    @Override
    public Iterable<Securities> getSecuritiesInAccount(Integer id) {
        Accounts account = getAccountById(id);
        return account.getSecuritiesList();

    }

    @Override
    public Iterable<Securities> getTopDailyPerformers() {
        List<Securities> allSecurities = sortByPercentChange();
        Collections.reverse(allSecurities);
        if (allSecurities.size()<5){
            return allSecurities;
        }else{
            return allSecurities.subList(0,5);
        }

    }

    @Override
    public Iterable<Securities> getWorstDailyPerformers() {
        List<Securities> allSecurities = sortByPercentChange();
        if (allSecurities.size()<5){
            return allSecurities;
        }else{
            return allSecurities.subList(0,5);
        }
    }

    private List<Securities> sortByPercentChange(){
        Iterable<Securities> allSecurities = getAllSecurities();
        List<Securities> result = new ArrayList<>();
        allSecurities.forEach(result::add);
        Collections.sort(result, new Comparator<Securities>() {
            @Override
            public int compare(Securities c1, Securities c2) {
                return Double.compare((c1.getCurrent_cost()-c1.getClosing_cost())/c1.getClosing_cost(), (c2.getCurrent_cost()-c2.getClosing_cost())/c2.getClosing_cost());
            }
        });
        return result;
    }


}
