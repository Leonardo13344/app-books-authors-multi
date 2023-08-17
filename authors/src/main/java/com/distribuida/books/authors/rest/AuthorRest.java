package com.distribuida.books.authors.rest;

import com.distribuida.books.authors.db.Author;
import com.distribuida.books.authors.repo.AuthorRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AuthorRest {

    @Inject
    AuthorRepository rep;

    //books GET
    @GET
    @Operation(summary = "GET ALL", description = "Obtains all authors")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Authors Listed"),
                    @APIResponse(responseCode = "404", description = "Method GETALL not found")
            }
    )
    public List<Author> findAll() {
        return rep.findAll().list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "GET", description = "Obtains author by id")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Author Returned"),
                    @APIResponse(responseCode = "404", description = "Method GET by id not found")
            }
    )
    public Response getById(@PathParam("id") Long id) {
        var book = rep.findByIdOptional(id);
        if (book.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(book.get()).build();
    }

    @POST
    @Operation(summary = "POST", description = "Create an author")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Auhtor created"),
                    @APIResponse(responseCode = "404", description = "Method POST an author not found")
            }
    )
    public Response create(Author p) {
        rep.persist(p);

        return Response.status(Response.Status.CREATED.getStatusCode(), "author created").build();
    }

    @PUT
    @Operation(summary = "PUT", description = "Update an author")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Author Updated"),
                    @APIResponse(responseCode = "404", description = "Method PUT not found")
            }
    )
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Author authorObj) {
        Author author = rep.findById(id);
        author.setFirstName(authorObj.getFirstName());
        author.setLastName(authorObj.getLastName());

        //rep.persistAndFlush(author);

        return Response.ok().build();
    }

    //books/{id} DELETE
    @DELETE
    @Operation(summary = "DELETE", description = "Deletes an author by id")
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Author Deleted"),
                    @APIResponse(responseCode = "404", description = "Method DELETED not found")
            }
    )
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        rep.deleteById(id);

        return Response.ok( )
                .build();
    }


}
