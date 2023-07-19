package com.distribuida.app.books.rest;

import com.distribuida.app.books.clients.AuthorRestClient;
import com.distribuida.app.books.db.Book;
import com.distribuida.app.books.dto.AuthorDto;
import com.distribuida.app.books.dto.BookDto;
import com.distribuida.app.books.repo.BookRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class BookRest {

    @Inject
    BookRepository rep;

    @Inject
    @RestClient
    AuthorRestClient clientAuthors;


    @GET
    public List<BookDto> findAll() {
        return rep.findAll().list()
                .stream()
                .map(obj -> {
                    System.out.println(obj.getAuthorIdd());
                    BookDto dto = new BookDto();
                    dto.setId(obj.getId());
                    dto.setIsbn(obj.getIsbn());
                    dto.setPrice(obj.getPrice());
                    dto.setTitle(obj.getTitle());
                    dto.setAuthorId(obj.getAuthorIdd());
                    AuthorDto authorDto = clientAuthors.getById(dto.getAuthorId());
                    dto.setAuthorName(authorDto.getFirstName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        var book = rep.findByIdOptional(id);

        System.out.println("*********" + clientAuthors);

        if (book.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Book obj = book.get();
        BookDto dto = new BookDto();

        dto.setId(obj.getId());
        dto.setIsbn(obj.getIsbn());
        dto.setPrice(obj.getPrice());
        dto.setTitle(obj.getTitle());
        dto.setAuthorId(obj.getAuthorIdd());
        System.out.println(dto.getAuthorId());
        AuthorDto authorDto = clientAuthors.getById(dto.getAuthorId());
        dto.setAuthorName(authorDto.getFirstName());

        return Response.ok(dto).build();
    }

    @POST
    public Response create(Book p) {
        rep.persist(p);

        return Response.status(Response.Status.CREATED.getStatusCode(), "book created").build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Book bookObj) {
        Book book = rep.findById(id);

        book.setIsbn(bookObj.getIsbn());
        book.setPrice(bookObj.getPrice());
        book.setTitle(bookObj.getTitle());

        //rep.persistAndFlush(book);

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        rep.deleteById(id);

        return Response.ok( )
                .build();
    }


}
