package com.citi.project.PortfolioProject.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="accounts")
public class Accounts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") int id;
    @Column(name="amount") private Double amount;
    @Column(name="type") private String type;
    @Column(name="name") private String name;


    public Accounts() {
    }

    public Accounts(int id, Double amount, String type, String name, List<Securities> securitiesList) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.name = name;
        this.securitiesList = securitiesList;
    }

    public Accounts(int id, Double amount, String type, String name) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    @JoinColumn(name="accountId", referencedColumnName="id")
    @OneToMany( cascade={CascadeType.MERGE, CascadeType.PERSIST})
    private List<Securities> securitiesList = new ArrayList<Securities>();

    public List<Securities> getSecuritiesList() {
        return securitiesList;
    }

    public void setTrackTitles(List<Securities> securitiesList) {
        this.securitiesList = securitiesList;
    }

    public void addSecurity(Securities security){
        this.securitiesList.add(security);
    }

}
