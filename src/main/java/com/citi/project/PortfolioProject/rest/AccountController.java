package com.citi.project.PortfolioProject.rest;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.services.AccountsService;
import com.citi.project.PortfolioProject.services.HistoryData;
import org.aspectj.lang.annotation.DeclareWarning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("")
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

    @RequestMapping(method=RequestMethod.GET,value = "/type/{type}")
    public Iterable<Accounts>  getAccountByType(@PathVariable("type") String type){
        if(type.equals("cash")) {
            return accountsService.getAccountByType(type);
        }else{
            return accountsService.updateAllSecurityInfo();
        }
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

    @RequestMapping(method = RequestMethod.PUT, value="/depositMoney")
    public void updateAccountCashAmount(@RequestBody Map<String, String> input){
        accountsService.updateAccountCashAmount(Integer.parseInt(input.get("account_id")), Double.parseDouble(input.get("changeInCash")));
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
    public void sellAllOfSecurity(@RequestBody Securities security){
        accountsService.removeAllSecurityBySymbol(security);
    }

    @RequestMapping(method = RequestMethod.PUT, value="/sellSecurity")
    public void sellQuantityOfSecurity(@RequestBody Securities security){
        accountsService.removeSomeSecuritiesBySymbol(security);
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
    
    @RequestMapping(method=RequestMethod.GET, value="/cashAndInvestmentHistory")
    public Map<String, Double> getWeeklyChange(){
        return  accountsService.getAllAccountChanges();
    }

    @RequestMapping(method=RequestMethod.GET, value="/investmentYearHistory")
    public Iterable<HistoryData> getInvestmentYear(){
        return accountsService.getInvestmentYearHistory();
    }

    @RequestMapping(method=RequestMethod.GET, value="/securityInfo/{symbol}")
    public Securities getInfoOnNewSecurity(@PathVariable("symbol") String symbol){
        return accountsService.getInfoOnPotentialSecurity(symbol);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/{id}")
    public String deleteAccount(@PathVariable("id") int id){
        return accountsService.deletAccount(id);
    }

    
}
