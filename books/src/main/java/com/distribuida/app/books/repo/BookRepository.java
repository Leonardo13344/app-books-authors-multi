package com.distribuida.app.books.repo;

import com.distribuida.app.books.db.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookRepository implements PanacheRepositoryBase<Book, Long> {

}
