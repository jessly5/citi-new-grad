package com.citi.project.PortfolioProject.repo;

import com.citi.project.PortfolioProject.PortfolioProjectApplication;
import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.repos.SecurityRepository;
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

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
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

    @Autowired
    private SecurityRepository secRepo;

//    @Autowired
//    private AccountsService accountsService;

//
//    @Autowired
//    AccountController controller;

    private int acc1Id;

    @BeforeEach
    public  void setupDatabaseEntryForReadOnlyTests() {
        Accounts acc1 = new Accounts(9000.00,"investment", "Wealth Simple");
        Securities securities = new Securities( "Stock", "APPL", 2, 30.5, 75.3, 74.2);
        acc1.addSecurity(securities);
        Accounts result1 = manager.persist(acc1);
        acc1Id = result1.getId();
        Accounts acc2 = new Accounts(5000.00,"cash", "RBC");
        Accounts result2 = manager.persist(acc2);
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
    public void canGetSecurities(){
        Optional<Accounts> ac = repo.findById(acc1Id);
        Accounts a1 =  ac.get();
        List<Securities> sec = a1.getSecuritiesList();
        assertThat(sec.size(), equalTo(1));
    }

    @Test
    public void canAddSecurity(){
        Optional<Accounts> ac = repo.findById(acc1Id);
        Accounts a1 =  ac.get();
        Securities securities = new Securities( "Stock", "GOOGL", 2, 20.5, 35.3, 34.2);
        a1.addSecurity(securities);
        repo.save(a1);
        Optional<Accounts> ac2 = repo.findById(acc1Id);
        assertThat(ac2.get().getSecuritiesList().size(), equalTo(2));

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
        Optional<Accounts> ac = repo.findById(acc1Id);
        Accounts a1 =  ac.get();
        a1.setAmount(1000D);
        repo.save(a1);
        Optional<Accounts> ac2 = repo.findById(acc1Id);
        assertThat(ac2.get().getAmount(), equalTo(1000D));
    }

}
