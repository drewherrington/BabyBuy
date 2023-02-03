package com.project.babybuy.product;

public class ProductData {
    String docId;
    String title;
    String description;
    String price;
    String filePath;
    String Email;
    String Purchased;

    public ProductData() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public ProductData( String title, String description, String price, String filePath, String email, String purchased) {

        this.title = title;
        this.description = description;
        this.price = price;
        this.filePath = filePath;
        Email = email;
        Purchased = purchased;
    }

    public String getPurchased() {
        return Purchased;
    }

    public void setPurchased(String purchased) {
        Purchased = purchased;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}