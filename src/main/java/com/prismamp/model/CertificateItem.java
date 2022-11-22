package com.prismamp.model;

import java.math.BigDecimal;

public class CertificateItem {

  private Double taxRate;
  private Double totalBaseTax;
  private Double totalTaxAmount;

  public CertificateItem(Double taxRate) {
    this.taxRate = taxRate;
    this.totalBaseTax = 0D;
    this.totalTaxAmount = 0D;
  }

  public Double getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(Double taxRate) {
    this.taxRate = taxRate;
  }

  public Double getTotalBaseTax() {
    return totalBaseTax;
  }

  public void setTotalBaseTax(Double totalBaseTax) {
    this.totalBaseTax = totalBaseTax;
  }

  public Double getTotalTaxAmount() {
    return totalTaxAmount;
  }

  public void setTotalTaxAmount(Double totalTaxAmount) {
    this.totalTaxAmount = totalTaxAmount;
  }

  public void addToTotalBaseTax(Double amount) {
    Double newAmount = add(this.totalBaseTax, amount);
    this.setTotalBaseTax(newAmount);
  }

  public void addToTotalTaxAmount(Double amount) {
    Double newAmount = add(this.totalTaxAmount, amount);
    this.setTotalTaxAmount(newAmount);
  }

  private Double add(Double value, Double aggValue) {
    return BigDecimal.valueOf(value).add(BigDecimal.valueOf(aggValue)).doubleValue();
  }
}
