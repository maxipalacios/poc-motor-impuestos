package com.prismamp.model;

public class Enriched {
  private String cuit;
  private String name;
  private String taxId;
  private Double taxRate;
  private Double taxAmount = 0D;

  public Enriched(Tax tax, Merchant merchant) {
    this.cuit = tax.getCuit();
    this.taxId = tax.getTaxId();
    this.taxRate = tax.getTaxRate();
    this.taxAmount = tax.getTaxAmount();
    this.name = merchant.getName();
  }

  public String getCuit() {
    return cuit;
  }

  public void setCuit(String cuit) {
    this.cuit = cuit;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public Double getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(Double taxRate) {
    this.taxRate = taxRate;
  }

  public Double getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(Double taxAmount) {
    this.taxAmount = taxAmount;
  }
}
