package com.example.products;

public class WittyItem {
  private WittyLevel scope;
  private Double metric;
  private String refId;

  public WittyItem() {
  }

  public WittyItem(final WittyLevel scope, final Double metric, final String refId) {
    this.scope = scope;
    this.metric = metric;
    this.refId = refId;
  }

  public WittyLevel getScope() {
    return this.scope;
  }

  public Double getMetric() {
    return this.metric;
  }

  public String getRefId() {
    return this.refId;
  }
}
