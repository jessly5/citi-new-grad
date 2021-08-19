package com.citi.project.PortfolioProject.rest;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.services.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    @RequestMapping(method = RequestMethod.POST)
//    public void addSecurity(@RequestBody String type, String symbol, Integer holdings, Double purchase_cost) {
//        Securities temp = new Securities(type, symbol, holdings, purchase_cost);
//        System.out.println(symbol);
//        accountsService.addSecurity(temp, "Wealth Simple", "RBC");
//
//    }
//    @RequestMapping(method = RequestMethod.POST)
//    public void addSecurity(@RequestBody Securities security/*, String investment_account, String cash_account*/) {
//        System.out.println(security.getCurrent_cost());
//        accountsService.addSecurity(security, "Wealth Simple", "RBC");
//
//    }





//    public void addSecurity(Securities security, String invest_account_name, String cash_account_name);
//    public void removeSecurity(Securities security, String invest_account_name, String cash_account_name);
//    public void updateAccountCashAmount(String account_name, double changeInCash);
//    public void removeAllSecurityBySymbol(String symbol, String invest_account_name, String cash_account_name);
//    public void removeSomeSecuritiesBySymbol(String symbol, String invest_account_name, String cash_account_name, int quantity);
}
