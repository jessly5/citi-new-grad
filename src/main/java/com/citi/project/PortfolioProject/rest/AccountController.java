package com.citi.project.PortfolioProject.rest;

import com.citi.project.PortfolioProject.entities.Accounts;
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



}
