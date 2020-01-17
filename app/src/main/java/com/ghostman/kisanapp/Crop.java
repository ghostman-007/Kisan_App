package com.ghostman.kisanapp;

class Crop {
    private String crop;
    private Long stock;
    private Long price;
    private Long govtPrice;
    private String phone;

    Crop() {
    }

    public Crop(String crop, Long stock, Long price, Long govtPrice, String phone) {
        this.crop = crop;
        this.stock = stock;
        this.price = price;
        this.govtPrice = govtPrice;
        this.phone = phone;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getGovtPrice() {
        return govtPrice;
    }

    public void setGovtPrice(Long govtPrice) {
        this.govtPrice = govtPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }
}