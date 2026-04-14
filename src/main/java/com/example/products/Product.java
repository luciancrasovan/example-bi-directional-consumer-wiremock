package com.example.products;

class Product {
	private String id;
	private String name;
	private String code;
	private String name2;

	Product() {
	}

	Product(final String id, final String code, final String name) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.name2 = name;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getName2() {
		return this.name2;
	}

	public String getCode() {
		return this.code;
	}

}