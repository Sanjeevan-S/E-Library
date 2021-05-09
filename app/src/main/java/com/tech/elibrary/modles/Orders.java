package com.tech.elibrary.modles;

public class Orders {

    String id;
    String name;
    String author;
    String price;
    String pages;
    String booktype;
    String getDate;
    String giveDate;

    public Orders() {
    }

    public Orders(String id, String name, String author, String price, String pages, String booktype, String getDate, String giveDate) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.pages = pages;
        this.booktype = booktype;
        this.getDate = getDate;
        this.giveDate = giveDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getBooktype() {
        return booktype;
    }

    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public String getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(String giveDate) {
        this.giveDate = giveDate;
    }
}
