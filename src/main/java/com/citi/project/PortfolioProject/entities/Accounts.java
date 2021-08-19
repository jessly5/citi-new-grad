package com.citi.project.PortfolioProject.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name="accounts")
public class Accounts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") private Integer id;
    @Column(name="amount") private Double amount;
    @Column(name="type") private String type;
    @Column(name="name") private String name;


    public Accounts() {
    }

    public Accounts(Integer id, Double amount, String type, String name, List<Securities> securitiesList) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.name = name;
        this.securitiesList = securitiesList;
    }

    public Accounts(Integer id, Double amount, String type, String name) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.name = name;
    }

    public Accounts(Double amount, String type, String name) {
        this.amount = amount;
        this.type = type;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Adding relationships
    @JoinColumn(name="account_id", referencedColumnName="id")
    @OneToMany( cascade={CascadeType.MERGE, CascadeType.PERSIST})
    private List<Securities> securitiesList = new ArrayList<Securities>();

    public List<Securities> getSecuritiesList() {
        return securitiesList;
    }

    public void setSecuritiesList(List<Securities> securitiesList) {
        this.securitiesList = securitiesList;
    }

    public void addSecurity(Securities security){
        this.securitiesList.add(security);
    }

//    TODO Might need another removal by ID method
    public void removeSecurity(Securities security){
        this.securitiesList.remove(security);
    }
    public double removeSecurityBySymbol(String symbol) {
        double totalValue=0;
        Iterator<Securities> i = securitiesList.iterator();
        while (i.hasNext()) {
            Securities currSecurities = i.next();
            if (currSecurities.getSymbol().equals(symbol)) {
                totalValue+=currSecurities.getHoldings()*currSecurities.getCurrent_cost();
                i.remove();
            }
        }
        return totalValue;
    }


    @JoinColumn(name="account_id", referencedColumnName="id")
    @OneToMany ( cascade={CascadeType.MERGE, CascadeType.PERSIST})
    private List<History> historyList = new ArrayList<>();

    public List<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
    }
    public void addHistory(History history){
        this.historyList.add(history);
    }

    public void updateAmount(double change){
        this.amount+=change;
    }

    public double removeSecurityQuantityBySymbol(String symbol, int quantity){
        //for now, assume no duplicates

        double totalValue=0;
        int currentQuantity=0;
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i=0; i < securitiesList.size() ; i++) {
            if (securitiesList.get(i).getSymbol().equals(symbol)) {
                currentQuantity += securitiesList.get(i).getHoldings();
                indexes.add(i);
            }
        }

        if (currentQuantity >= quantity){
            securitiesList.get(indexes.get(0)).setHoldings(currentQuantity-quantity);
            return quantity*securitiesList.get(indexes.get(0)).getCurrent_cost();

        } else {
            return 0.0;
        }

    }

}
