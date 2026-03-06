package com.example.products;

class Product {
	private String id;
	private String name;
	private String code;
	private String category;

	Product() {
	}

	Product(final String id, final String code, final String name, final String category) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.category = category;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}

	public String getCategory() {
		return this.category;
	}

}