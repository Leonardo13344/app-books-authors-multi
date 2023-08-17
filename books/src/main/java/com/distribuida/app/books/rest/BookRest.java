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
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    @Operation(summary = "GET ALL", description = "Obtains all books")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Books Returned"),
                    @APIResponse(responseCode = "404", description = "Method GET all not found")
            }
    )
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
                    String aname = String.format("%s, %s", authorDto.getLastName(), authorDto.getFirstName());
                    dto.setAuthorName(aname);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GET
    @Operation(summary = "GET", description = "Obtains a book by id")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Book by id returned"),
                    @APIResponse(responseCode = "404", description = "Method GET by id not found")
            }
    )
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        var book = rep.findByIdOptional(id);

        System.out.println("*********" + clientAuthors);

        if (book.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        //Proxy Manual
//        var config = ConfigProvider.getConfig();
//        String.format("http://%s:%s",
//                config.getValue("app.authors.host", String.class),
//                config.getValue("app.authors.port", String.class)
//        );
//        RestClientBuilder.newBuilder()
//                .baseUri(URI.create("http://127.0.0.1:9090"))
//                .connectTimeout(400, TimeUnit.MILLISECONDS)
//                .build(AuthorRestClient.class);

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
    @Operation(summary = "POST", description = "Create a book")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Book created"),
                    @APIResponse(responseCode = "404", description = "Method POST not found or something else...")
            }
    )
    public Response create(Book p) {
        rep.persist(p);

        return Response.status(Response.Status.CREATED.getStatusCode(), "book created").build();
    }

    @PUT
    @Operation(summary = "PUT", description = "Update a book by id")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Book updated"),
                    @APIResponse(responseCode = "404", description = "Method PUT by id not found")
            }
    )
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
    @Operation(summary = "DELETE", description = "Delete a book by id")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Book deleted"),
                    @APIResponse(responseCode = "404", description = "Method DELETE by id not found or something else...")
            }
    )
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        rep.deleteById(id);

        return Response.ok( )
                .build();
    }


}
