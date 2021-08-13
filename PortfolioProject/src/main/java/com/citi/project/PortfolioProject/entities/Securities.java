package com.citi.project.PortfolioProject.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name="securities")
public class Securities implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") public int id;
    @Column(name = "type") public String type;
    @Column(name = "symbol") public String symbol;
    @Column(name = "holdings") public Integer holdings;
    @Column(name = "purchase_cost") public Double purchase_cost;
    @Column(name = "closing_cost") public Double closing_cost;
    @Column(name = "current_cost") public Double current_cost;
    @Column(name = "account_id") public Integer account_id;

    public Securities() {
    }

    public Securities(int id, String type, String symbol, Integer holdings, Double purchaseCost, Double closingCost, Double currentCost, Integer accountId) {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;
        this.purchase_cost = purchaseCost;
        this.closing_cost = closingCost;
        this.current_cost = currentCost;
        this.account_id = accountId;
    }

    public Securities(int id, String type, String symbol, Integer holdings, Double purchaseCost, Integer accountId) {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;
        this.purchase_cost = purchaseCost;
        this.account_id = accountId;
        this.current_cost = purchaseCost;
        Random r = new Random();
        this.closing_cost = (purchaseCost-1.0) + (1.0) * r.nextDouble();
       
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Double getPurchase_cost() {
        return purchase_cost;
    }

    public void setPurchase_cost(Double purchase_cost) {
        this.purchase_cost = purchase_cost;
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

    public Integer getAccountId() {
        return account_id;
    }

    public void setAccountId(Integer accountId) {
        this.account_id = accountId;
    }
}
