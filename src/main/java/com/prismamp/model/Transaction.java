package com.prismamp.model;

import java.util.UUID;

public class Transaction {
  private String timestamp;
  private UUID id;
  private String cuit;
  private String taxId;

  public String getCuit() {
    return cuit;
  }

  public String getTaxId() {
    return taxId;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public UUID getId() {
    return id;
  }
}
