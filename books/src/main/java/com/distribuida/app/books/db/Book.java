package com.distribuida.app.books.db;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String isbn;

    @Column
    private String title;

    @Column
    private Double price;

    @Column(name="author_id")
    private int authorIdd;

    public int getAuthorIdd() {
        return authorIdd;
    }

    public void setAuthorId(int author_id) {
        this.authorIdd = author_id;
    }

    public Book(){}

    public Book(String name){
        this.isbn = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", authorIdd=" + authorIdd +
                '}';
    }
}
