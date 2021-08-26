package com.citi.project.PortfolioProject.controller;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.rest.AccountController;
import com.citi.project.PortfolioProject.services.AccountsService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/* The beans need to be commented out otherwise other tests will fail as they pick up the
configured beans in this test!
 */

/* This test also requires a specific version of Mockito in the pom when using Java 11 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=AccountControllerUnitTest.Config.class)
public class AccountControllerUnitTest {

    // Define a configuration class used for our test
    // it is static so there is only one instance of it
    // NOTE breaks other tests as the scope of this configuration goes to all test classes
//    @TestConfiguration
    protected static class Config {

        // needed for the Spring repo dependency in the service layer
//        @Bean
////        @Primary
//        public AccountRepository repo() {
//            return mock(AccountRepository.class);
//        }
//
//        // create a mock service layer than when asked for all the CDs returns a single CD in a list
//        @Bean
////        @Primary
//        public AccountsService service() {
//            Accounts account = new Accounts();
//            Securities securities = new Securities();
//            account.addSecurity(securities);
//            List<Accounts> accounts = new ArrayList<>();
//            accounts.add(account);
//
//            AccountsService service = mock(AccountsService.class);
//            when(service.getAccounts()).thenReturn(accounts);
//            when(service.getAccountById(1)).thenReturn(account);
//
//
//            doReturn(account.getSecuritiesList()).when(service).getSecuritiesInAccount(1);
//            return service;
//        }
//
//
//
//        @Bean
////        @Primary
//        public AccountController controller() {
//            return new AccountController();
//        }
    }

//    @Autowired
//    private AccountController controller;
//
//    @Disabled
//    @Test
//    public void testFindAll() {
//        Iterable<Accounts> accounts = controller.findAll();
//        Stream<Accounts> stream = StreamSupport.stream(accounts.spliterator(), false);
//        assertThat(stream.count(), equalTo(1L));
//    }
//
//    @Disabled
//    @Test
//    public void testAccountById() {
//        Accounts cd = controller.getAccountById(1);
//        assertNotNull(cd);
//    }
//
//    @Disabled
//    @Test
//    public void testSecuritiesById(){
//        Iterable<Securities> secs = controller.getSecuritiesInAccount(1);
//        assertNotNull(secs);
//    }

}