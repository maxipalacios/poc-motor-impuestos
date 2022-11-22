package com.prismamp.model;

import java.util.Map;

public class Certificate {

  private String cuit;
  private String taxId;
  private Map<Double, CertificateItem> taxRates;

  public Certificate(Map<Double, CertificateItem> taxRates) {
    this.taxRates = taxRates;
  }

  public String getCuit() {
    return cuit;
  }

  public void setCuit(String cuit) {
    this.cuit = cuit;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public Map<Double, CertificateItem> getTaxRates() {
    return taxRates;
  }

  public void setTaxRates(Map<Double, CertificateItem> taxRates) {
    this.taxRates = taxRates;
  }
}
