package com.example.products;

import java.util.List;

public class Witty {
  private String id;
  private List<WittyItem> items;
  private String description;

  public Witty() {
  }

  public Witty(final String id, final List<WittyItem> items, final String description) {
    this.id = id;
    this.items = items;
    this.description = description;
  }

  public String getId() {
    return this.id;
  }

  public List<WittyItem> getItems() {
    return this.items;
  }

  public String getDescription() {
    return this.description;
  }
}
