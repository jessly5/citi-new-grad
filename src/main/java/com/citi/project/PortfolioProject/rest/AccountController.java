package com.citi.project.PortfolioProject.rest;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.services.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountsService accountsService;

    @RequestMapping(method=RequestMethod.GET)
    public Iterable<Accounts> findAll(){
        return accountsService.getAccounts();
    }

    @RequestMapping(method=RequestMethod.GET, value="/{id}")
    public Accounts getAccountById(@PathVariable("id") Integer id){
        return accountsService.getAccountById(id);
    }

    @RequestMapping(method=RequestMethod.GET,value = "/name/{name}")
    public Accounts getAccountByName(@PathVariable("name") String name){
        return accountsService.getAccountByName(name);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addSecurity(@RequestBody Securities security, @RequestParam("cashAccount") String cashAccount) {
        accountsService.addSecurity(security, "Wealth Simple", cashAccount);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateAccountCashAmount(@RequestBody Map<String, String> input){
        System.out.println(input.get("account_name").toString());
        System.out.println(input.get("changeInCash"));
        accountsService.updateAccountCashAmount(input.get("account_name"), Double.parseDouble(input.get("changeInCash")));
    }

    @RequestMapping(method = RequestMethod.GET, value="/update/Data")
    public Iterable<Accounts> retrieveLatestData(){
        return accountsService.updateAllSecuirtyInfo();
    }
    //from the document
    //GET: valuation summary (3 types), changes in everything, market movers (top 5 whatever)

//
//    public void removeSecurity(Securities security, String invest_account_name, String cash_account_name);
//    public void updateAccountCashAmount(String account_name, double changeInCash);
//    public void removeAllSecurityBySymbol(String symbol, String invest_account_name, String cash_account_name);
//    public void removeSomeSecuritiesBySymbol(String symbol, String invest_account_name, String cash_account_name, int quantity);
}
