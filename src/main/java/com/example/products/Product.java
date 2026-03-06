package com.example.products;

class Product {
	private String id;
	private String name;
	private String code;

	Product() {
	}

	Product(final String id, final String code, final String name) {
		this.id = id;
		this.code = code;
		this.name = name;
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

}