package com.citi.project.PortfolioProject.services;

import java.util.Date;

public class HistoryData {
    private Date date;
    private Double value;

    public HistoryData() {
    }

    public HistoryData(Date date, Double value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
