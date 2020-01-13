package com.ghostman.kisanapp;

class Crop {
    private String crop;
    private Integer stock;
    private Integer price;
    private Integer govtPrice;

    Crop() {    }

    Crop(String crop, Integer stock, Integer price, Integer govtPrice) {
        this.crop = crop;
        this.stock = stock;
        this.price = price;
        this.govtPrice = govtPrice;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getGovtPrice() {
        return govtPrice;
    }

    public void setGovtPrice(Integer govtPrice) {
        this.govtPrice = govtPrice;
    }
}
