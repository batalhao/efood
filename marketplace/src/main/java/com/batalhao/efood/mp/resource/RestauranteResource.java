package com.batalhao.efood.mp.resource;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import com.batalhao.efood.mp.dto.PratoDTO;
import com.batalhao.efood.mp.model.Prato;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

@Path("restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

  @Inject
  PgPool client;

  @GET
  @Tag(name = "restaurante")
  @Path("{idRestaurante}/pratos")
  @APIResponse(responseCode = "200", content = @Content(
      schema = @Schema(type = SchemaType.ARRAY, implementation = PratoDTO.class)))
  public Multi<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
    return Prato.findAll(client, idRestaurante);
  }

}
