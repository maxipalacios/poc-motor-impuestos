package com.prismamp;

import java.util.Objects;

public class TaxGroupingKey {

  private String cuit;
  private String taxId;

  public TaxGroupingKey(String cuit, String taxId) {
    this.cuit = cuit;
    this.taxId = taxId;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TaxGroupingKey that = (TaxGroupingKey) o;
    return cuit.equals(that.cuit) && taxId.equals(that.taxId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cuit, taxId);
  }

  @Override
  public String toString() {
    return cuit + '|' + taxId;
  }
}
