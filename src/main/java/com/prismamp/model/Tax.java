package com.prismamp.model;

import java.util.UUID;

public class Tax {
  private String createdAt;
  private String cuit;
  private UUID transactionId;
  private String taxId;
  private Double baseTax;
  private String taxStatus;
  private Double taxRate;
  private Double taxAmount = 0D;
  private Double exclusionRate;

  public String getCreatedAt() {
    return createdAt;
  }

  public String getCuit() {
    return cuit;
  }

  public UUID getTransactionId() {
    return transactionId;
  }

  public String getTaxId() {
    return taxId;
  }

  public Double getBaseTax() {
    return baseTax;
  }

  public String getTaxStatus() {
    return taxStatus;
  }

  public Double getTaxRate() {
    return taxRate;
  }

  public Double getTaxAmount() {
    return taxAmount;
  }

  public Double getExclusionRate() {
    return exclusionRate;
  }
}
