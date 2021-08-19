package com.citi.project.PortfolioProject.repo;

import com.citi.project.PortfolioProject.PortfolioProjectApplication;
import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.repos.SecurityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest // use an in memory database
@ContextConfiguration(classes= PortfolioProjectApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties") // this is only needed because swagger breaks tests

public class TestSecuritiesRepo {

    @Autowired
    private TestEntityManager manager;


    @Autowired
    private SecurityRepository repo;


    private int acc1Id;

    @BeforeEach
    public  void setupDatabaseEntryForReadOnlyTests() {
        Accounts acc1 = new Accounts(9000.00,"investment", "Wealth Simple");
        Securities securities = new Securities( "Stock", "APPL", 2, 30.5, 75.3, 74.2);
        acc1.addSecurity(securities);
        Accounts result1 = manager.persist(acc1);
        acc1Id = result1.getId();
        //Integer id, String type, String symbol, Integer holdings, Double purchaseCost, Double closingCost, Double currentCost, Integer accountId
        Accounts acc2 = new Accounts(5000.00,"cash", "RBC");
        Accounts result2 = manager.persist(acc2);

    }

    @Test
    public void canGetBySymbol(){
        Iterable<Securities> sec = repo.findBySymbol("APPL");
        Stream<Securities> stream = StreamSupport.stream(sec.spliterator(), false);
        for (Securities a1 :sec) {
            System.out.println("Security: " +a1.getSymbol());
        }
        //    System.out.println(accounts.toString());
        assertThat(stream.count(), equalTo(1L));
    }




//    @Test
//    public void canGetByAccountId(){
//        Iterable<Securities> sec = repo.findByAccountId((acc1Id));
//        Stream<Securities> stream = StreamSupport.stream(sec.spliterator(), false);
//        for (Securities a1 :sec) {
//            System.out.println("Security: " +a1.getSymbol());
//        }
//    //    System.out.println(accounts.toString());
//        assertThat(stream.count(), equalTo(1L));
//
//
//    }




}
