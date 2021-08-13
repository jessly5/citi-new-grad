package com.citi.project.PortfolioProject.repo;

import com.citi.project.PortfolioProject.PortfolioProjectApplication;
import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.rest.AccountController;
import com.citi.project.PortfolioProject.services.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ListIterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest // use an in memory database
@ContextConfiguration(classes= PortfolioProjectApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties") // this is only needed because swagger breaks tests
public class TestAccountRepo {

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private AccountRepository repo;
//
//    @Autowired
//    private AccountsService accountsService;

//
//    @Autowired
//    AccountController controller;

    private int acc1Id;

    @BeforeEach
    public  void setupDatabaseEntryForReadOnlyTests() {
        Accounts acc1 = new Accounts(9000.00,"investment", "Wealth Simple");
        Accounts result1 = manager.persist(acc1);
        Accounts acc2 = new Accounts(5000.00,"cash", "RBC");
        Accounts result2 = manager.persist(acc2);
        acc1Id = result1.getId();
    }

    // unit test the repo using a mock database
    @Test
    public void canRetrieveAccountsByType() {
        Iterable<Accounts> accounts = repo.findByType("investment");
        Stream<Accounts> stream = StreamSupport.stream(accounts.spliterator(), false);

        for (Accounts a1 :accounts) {
            System.out.println("Account: " +a1.getName());
        }
        System.out.println(accounts.toString());
        assertThat(stream.count(), equalTo(1L));
    }

    @Test
    public void canRetrieveByAccountName() {
        Iterable<Accounts> accounts = repo.findByName("Wealth Simple");
        Stream<Accounts> stream = StreamSupport.stream(accounts.spliterator(), false);

        for (Accounts a1 :accounts) {
            System.out.println("Account: " +a1.getName());
        }
        System.out.println(accounts.toString());
        assertThat(stream.count(), equalTo(1L));
    }

    @Test
    public void canInsertAccount() {
    }

    @Test
    public void canDeleteAccount() {

    }

    @Test
    public void canUpdateAccount() {

    }

}
