package com.citi.project.PortfolioProject.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name="securities")
public class Securities implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") private Integer id;
    @Column(name = "type") private String type;
    @Column(name = "symbol") private String symbol;
    @Column(name = "holdings") private Integer holdings;
    @Column(name = "closing_cost") private Double closing_cost;
    @Column(name = "current_cost") private Double current_cost;
    @Column(name = "cash_account_id") private Integer cash_account_id;
    @Column(name = "account_id") private Integer account_id;

    public Securities() {
    }

    public Securities(Integer id, String type, String symbol, Integer holdings, Double closing_cost, Double current_cost, Integer cash_account_id, Integer account_id) {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;
        this.closing_cost = closing_cost;
        this.current_cost = current_cost;
        this.cash_account_id = cash_account_id;
        this.account_id = account_id;
    }

    public Securities(Integer id, String type, String symbol, Integer holdings, Double currentCost, Integer accountId) {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;

        this.account_id = accountId;
        this.current_cost = currentCost;
        Random r = new Random();
        this.closing_cost = (currentCost-1.0) + (1.0) * r.nextDouble();
    }


    public Securities(String type, String symbol, Integer holdings,  Double closing_cost, Double current_cost) {
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;

        this.closing_cost = closing_cost;
        this.current_cost = current_cost;
    }

    public Securities(String type, String symbol, Integer holdings, Double purchase_cost) {
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;
        this.current_cost = purchase_cost;
        this.closing_cost = purchase_cost;
    }

    public Integer getCash_account_id() {
        return cash_account_id;
    }

    public void setCash_account_id(Integer cash_account_id) {
        this.cash_account_id = cash_account_id;
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getHoldings() {
        return holdings;
    }

    public void setHoldings(Integer holdings) {
        this.holdings = holdings;
    }

    public Double getClosing_cost() {
        return closing_cost;
    }

    public void setClosing_cost(Double closing_cost) {
        this.closing_cost = closing_cost;
    }

    public Double getCurrent_cost() {
        return current_cost;
    }

    public void setCurrent_cost(Double current_cost) {
        this.current_cost = current_cost;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }
}
