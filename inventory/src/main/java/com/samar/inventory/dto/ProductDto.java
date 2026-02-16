package com.samar.inventory.dto;

public class ProductDto {

    private int inventoryId;
    private int quantity;
    private int productId;
    private String productName;
    private int productPrice;
    private String category;
    private String description;

    public ProductDto() {
    }

    public ProductDto(int inventoryId, String description, String category, int productPrice, String productName, int productId, int quantity) {
        this.inventoryId = inventoryId;
        this.description = description;
        this.category = category;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
