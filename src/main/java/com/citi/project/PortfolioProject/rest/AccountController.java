package com.citi.project.PortfolioProject.rest;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.services.AccountsService;
import org.aspectj.lang.annotation.DeclareWarning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    //TODO USAMA ask if by name works (didnt for me) and merge if does.
    @RequestMapping(method=RequestMethod.GET,value = "/type/{type}")
    public Iterable<Accounts>  getAccountByType(@PathVariable("type") String type){
        return accountsService.getAccountByType(type);
    }

    //TODO add appropriate url
    @RequestMapping(method = RequestMethod.POST, value="/buySecurity")
    public void addSecurity(@RequestBody Securities security) {

        try {
            Securities addedSecurity = accountsService.addSecurity(security);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO add appropriate url
    @RequestMapping(method = RequestMethod.PUT, value="/depositMoney")
    public void updateAccountCashAmount(@RequestBody Map<String, String> input){
        accountsService.updateAccountCashAmount(input.get("account_name"), Double.parseDouble(input.get("changeInCash")));
    }

    @RequestMapping(method = RequestMethod.GET, value="/updateData")
    public Iterable<Accounts> retrieveLatestData(){
        return accountsService.updateAllSecurityInfo();
    }

    @RequestMapping(method = RequestMethod.GET, value="/cashAccountSummary")
    public Double getCashSummary(){
        return accountsService.summarizeCash();
    }

    @RequestMapping(method = RequestMethod.GET, value="/investmentAccountSummary")
    public Double getInvestmentSummary(){
        return accountsService.summarizeInvsetments();
    }
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value="/netWorth")
    public Double getNetWorth(){
        return accountsService.summarizeNetWorth();
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/sellSecurity")
    public void sellAllOfSecurity(@RequestBody Map<String, String> input){
        accountsService.removeAllSecurityBySymbol(input.get("symbol"), input.get("account_name"), input.get("cash_account"));
    }

    //TODO is PUT the right choice here?
    @RequestMapping(method = RequestMethod.PUT, value="/sellSecurity")
    public void sellQuantityOfSecurity(@RequestBody Map<String,String> input){
        accountsService.removeSomeSecuritiesBySymbol(input.get("symbol"), input.get("account_name"), input.get("cash_account"),
                Integer.parseInt(input.get("quantity")));
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addAccount(@RequestBody Accounts account){
        accountsService.addNewAccount(account);
    }

    @RequestMapping(method=RequestMethod.GET, value="/allSecurities")
    public Iterable<Securities> getAllSecurities(){
        return accountsService.getAllSecurities();
    }

    @RequestMapping(method=RequestMethod.GET, value="/{id}/securities")
    public Iterable<Securities> getSecuritiesInAccount(@PathVariable("id") Integer id){
        return accountsService.getSecuritiesInAccount(id);
    }

    @RequestMapping(method=RequestMethod.GET, value="/dailyGainers")
    public Iterable<Securities> getDailyGainers(){
        return accountsService.getTopDailyPerformers();
    }

    @RequestMapping(method=RequestMethod.GET, value="/dailyLosers")
    public Iterable<Securities> getDailyLosers(){
        return accountsService.getWorstDailyPerformers();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/testWeekly")
    public Double getWeeklyChange(){
        return  accountsService.getWeeklyChanges();
    }
    
}
