package com.ou.pojos;

import java.util.List;

public class SalePercent {
    private Integer sperId;
    private Float sperPercent;
    private List<Sale> sales;

    public Integer getSperId() {
        return sperId;
    }

    public void setSperId(Integer sperId) {
        this.sperId = sperId;
    }

    public Float getSperPercent() {
        return sperPercent;
    }

    public void setSperPercent(Float sperPercent) {
        this.sperPercent = sperPercent;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}