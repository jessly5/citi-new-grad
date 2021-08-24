package com.citi.project.PortfolioProject.repo;

import com.citi.project.PortfolioProject.PortfolioProjectApplication;
import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.History;
import com.citi.project.PortfolioProject.entities.Securities;
import com.citi.project.PortfolioProject.repos.AccountRepository;
import com.citi.project.PortfolioProject.repos.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ExtendWith(SpringExtension.class)
@DataJpaTest // use an in memory database
@ContextConfiguration(classes= PortfolioProjectApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties") // this is only needed because swagger breaks tests
public class TestHistoryRepo {

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private HistoryRepository repo;

    @Autowired
    private AccountRepository accountRepository;

    private int acc1Id;

    @BeforeEach
    public  void setupDatabaseEntryForReadOnlyTests() {
        Accounts acc1 = new Accounts(9000.00,"investment", "Wealth Simple");
        Securities securities = new Securities( "Stock", "APPL", 2,  75.3, 74.2);
        acc1.addSecurity(securities);
        History history = new History("investment", new Date(2021, 8, 2), 100D);
        acc1.addHistory(history);

        Accounts result1 = manager.persist(acc1);
        acc1Id = result1.getId();

        Accounts acc2 = new Accounts(5000.00,"cash", "RBC");
        Accounts result2 = manager.persist(acc2);
    }

    @Test
    public void canAddHistoryEntry(){
        Accounts acs = accountRepository.getById(acc1Id);

        History history = new History("investment", new Date(2021, 8, 2), 1000D);
        acs.addHistory(history);
        Iterable<History> h = repo.findAll();
        Stream<History> stream = StreamSupport.stream(h.spliterator(), false);
        assertThat(stream.count(), equalTo(1L));
//tring account_type, Date transaction_date, Double amount
    }

    @Test
    public void canGetAfterDate(){
        Iterable<History> hist = repo.findByTransactionDateIsAfter(new Date(2021,8,3));
        Stream<History> stream = StreamSupport.stream(hist.spliterator(), false);
        assertThat(stream.count(), equalTo(0L));

    }

}
