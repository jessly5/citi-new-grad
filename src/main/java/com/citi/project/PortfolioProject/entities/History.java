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
    @Column(name="account_type") private String account_type;
    @Column(name="transaction_date") private Date transaction_date;
    @Column(name="amount") private Double amount;
    @Column(name="account_id") private Integer account_id;

    public History() {
    }

    public History(Integer id, String accountType, Date transactionDate, Double amount, Integer accountID) {
        this.id = id;
        this.account_type = accountType;
        this.transaction_date = transactionDate;
        this.amount = amount;
        this.account_id = accountID;
    }

    public History(String account_type, Date transaction_date, Double amount, Integer account_id) {
        this.account_type = account_type;
        this.transaction_date = transaction_date;
        this.amount = amount;
        this.account_id = account_id;
    }

    public History(String account_type, Date transaction_date, Double amount) {
        this.account_type = account_type;
        this.transaction_date = transaction_date;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}