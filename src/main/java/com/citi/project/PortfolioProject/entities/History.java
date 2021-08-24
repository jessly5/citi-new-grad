package com.citi.project.PortfolioProject.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="history")
public class History implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") private Integer id;
    @Column(name="account_type") private String accountType;
    @Column(name="transaction_date") private Date transactionDate;
    @Column(name="amount") private Double amount;
    @Column(name="account_id") private Integer account_id;

    public History() {
    }

    public History(Integer id, String accountType, Date transactionDate, Double amount, Integer accountID) {
        this.id = id;
        this.accountType = accountType;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.account_id = accountID;
    }

    public History(String account_type, Date transaction_date, Double amount, Integer account_id) {
        this.accountType = account_type;
        this.transactionDate = transaction_date;
        this.amount = amount;
        this.account_id = account_id;
    }

    public History(String account_type, Date transaction_date, Double amount) {
        this.accountType = account_type;
        this.transactionDate = transaction_date;
        this.amount = amount;
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accounType) {
        this.accountType = accounType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
