package com.prismamp.model;

public class EnrichedWithTaxAmountSum {

  private Enriched enriched;
  private Double taxAmountSum;

  public EnrichedWithTaxAmountSum(Enriched enriched) {
    this.enriched = enriched;
  }

  public Enriched getEnriched() {
    return enriched;
  }

  public void setEnriched(Enriched enriched) {
    this.enriched = enriched;
  }

  public Double getTaxAmountSum() {
    return taxAmountSum;
  }

  public void setTaxAmountSum(Double taxAmountSum) {
    this.taxAmountSum = taxAmountSum;
  }
}
