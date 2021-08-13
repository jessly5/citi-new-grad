package com.citi.project.PortfolioProject.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name="securities")
public class Securities implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") public int id;
    @Column(name = "type") public String type;
    @Column(name = "symbol") public String symbol;
    @Column(name = "holdings") public Integer holdings;
    @Column(name = "purchaseCost") public Double purchaseCost;
    @Column(name = "closingCost") public Double closingCost;
    @Column(name = "currentCost") public Double currentCost;
    @Column(name="accountId") public Integer accountId;

    public Securities() {
    }

    public Securities(int id, String type, String symbol, Integer holdings, Double purchaseCost, Double closingCost, Double currentCost, Integer accountId) {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;
        this.purchaseCost = purchaseCost;
        this.closingCost = closingCost;
        this.currentCost = currentCost;
        this.accountId = accountId;
    }

    public Securities(int id, String type, String symbol, Integer holdings, Double purchaseCost, Integer accountId) {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.holdings = holdings;
        this.purchaseCost = purchaseCost;
        this.accountId = accountId;
        this.currentCost = purchaseCost;
        Random r = new Random();
        this.closingCost= (purchaseCost-1.0) + (1.0) * r.nextDouble();
       
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

    public Double getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(Double purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public Double getClosingCost() {
        return closingCost;
    }

    public void setClosingCost(Double closingCost) {
        this.closingCost = closingCost;
    }

    public Double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(Double currentCost) {
        this.currentCost = currentCost;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
