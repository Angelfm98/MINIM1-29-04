package edu.upc.dsa.services;


import edu.upc.dsa.Covid19Manager;
import edu.upc.dsa.Covid19ManagerImpl;
import edu.upc.dsa.models.Seguimiento;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/usuaris", description = "Endpoint to Usuaris Service")
@Path("/usuaris")
public class UsuarisService {

    private Covid19Manager tm;

    public UsuarisService() {
        this.tm = Covid19ManagerImpl.getInstance();
        if (tm.VacunaSize()==0) {
            this.tm.addUsuari("Angel", "Franco Martos", false);
            this.tm.addUsuari("Ariadna", "Fàbrega Roig", false);
            this.tm.addUsuari("Trinidad", "Pancorbo Garrido",true);
        }
    }

    @GET
    @ApiOperation(value = "get all Seguimientos", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Seguimiento.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeguimiento() {

        List<Seguimiento> seguimientos = this.tm.findAllSeguimientos();

        GenericEntity<List<Seguimiento>> entity = new GenericEntity<List<Seguimiento>>(seguimientos) {};
        return Response.status(201).entity(entity).build()  ;

    }

    @GET
    @ApiOperation(value = "get an Seguimiento", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Seguimiento.class),
            @ApiResponse(code = 404, message = "Vacuna not found")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeguimiento(@PathParam("id") String id) {
        Seguimiento a = this.tm.getSeguimiento(id);
        if (a == null) return Response.status(404).build();
        else  return Response.status(201).entity(a).build();
    }

    @DELETE
    @ApiOperation(value = "delete a Seguimiento", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Vacuna not found")
    })
    @Path("/{id}")
    public Response deleteSeguimiento(@PathParam("id") String id) {
        Seguimiento a = this.tm.getSeguimiento(id);
        if (a == null) return Response.status(404).build();
        else this.tm.deleteSeguimiento(id);
        return Response.status(201).build();
    }

    @PUT
    @ApiOperation(value = "update an Seguimiento", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Vacuna not found")
    })
    @Path("/")
    public Response updateSeguimiento(Seguimiento seguimiento) {

        Seguimiento a = this.tm.updateSeguimiento(seguimiento);

        if (a == null) return Response.status(404).build();

        return Response.status(201).build();
    }



    @POST
    @ApiOperation(value = "create a new Seguimiento", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Seguimiento.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })

    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newSeguimiento(Seguimiento seguimiento) {

        if (seguimiento.getFechaVacunacion()==null || seguimiento.getNombreVacunado()==null)  return Response.status(500).entity(seguimiento).build();
        this.tm.addSeguimiento(seguimiento);
        return Response.status(201).entity(seguimiento).build();
    }

}
