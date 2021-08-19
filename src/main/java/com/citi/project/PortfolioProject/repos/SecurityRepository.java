package com.citi.project.PortfolioProject.repos;

import com.citi.project.PortfolioProject.entities.Accounts;
import com.citi.project.PortfolioProject.entities.Securities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface SecurityRepository extends JpaRepository<Securities, Integer> {

  //  public Iterable<Securities> findByAccountID(Integer account_id);

//    @Query( nativeQuery = true, value = "select sec from securities sec where sec.account_id =:accountid")
//    List<Securities> findByAccountId(@Param("accountid") Integer account_id);

    public Iterable<Securities> findBySymbol(String symbol);

}
