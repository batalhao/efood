package com.batalhao.efood.cadastro.resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import com.batalhao.efood.cadastro.dto.AdicionarPratoDTO;
import com.batalhao.efood.cadastro.dto.AdicionarRestauranteDTO;
import com.batalhao.efood.cadastro.dto.AtualizarPratoDTO;
import com.batalhao.efood.cadastro.dto.AtualizarRestauranteDTO;
import com.batalhao.efood.cadastro.dto.PratoDTO;
import com.batalhao.efood.cadastro.dto.RestauranteDTO;
import com.batalhao.efood.cadastro.infra.ConstraintViolationResponse;
import com.batalhao.efood.cadastro.mapper.PratoMapper;
import com.batalhao.efood.cadastro.mapper.RestauranteMapper;
import com.batalhao.efood.cadastro.model.Prato;
import com.batalhao.efood.cadastro.model.Restaurante;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "efood-oauth", type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(password = @OAuthFlow(
        tokenUrl = "http://localhost:8180/auth/realms/efood/protocol/openid-connect/token")))
@SecurityRequirement(name = "efood-oauth", scopes = {})
// @SecurityRequirements(value = {@SecurityRequirement(name = "efood-oauth", scopes = {})})
// Anotaçao utiizada para fazer o trace de camadas de negocio
// @Traced
public class RestauranteResource {

  @Inject
  RestauranteMapper restauranteMapper;

  @Inject
  PratoMapper pratoMapper;

  @GET
  @Tag(name = "restaurante")
  public List<RestauranteDTO> buscar() {
    Stream<Restaurante> restaurantes = Restaurante.streamAll();
    return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r))
        .collect(Collectors.toList());
  }

  @POST
  @Transactional
  @Tag(name = "restaurante")
  @APIResponse(responseCode = "201", description = "Caso o restaurante seja cadastrado com sucesso")
  @APIResponse(responseCode = "400",
      content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
  public Response adicionar(@Valid AdicionarRestauranteDTO dto) {
    Restaurante restaurante = restauranteMapper.toRestaurante(dto);
    restaurante.persist();
    return Response.status(Status.CREATED).build();
  }

  @PUT
  @Transactional
  @Path("{id}")
  @Tag(name = "restaurante")
  public void atualizar(@PathParam("id") Long id, AtualizarRestauranteDTO dto) {
    Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
    if (restauranteOp.isEmpty()) {
      throw new NotFoundException();
    }
    Restaurante restaurante = restauranteOp.get();

    // MapStruct: aqui passo a referencia para ser atualizada
    restauranteMapper.toRestaurante(dto, restaurante);

    restaurante.persist();
  }

  @DELETE
  @Transactional
  @Path("{id}")
  @Tag(name = "restaurante")
  public void atualizar(@PathParam("id") Long id) {
    Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
    restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
      throw new NotFoundException();
    });
  }

  /* Pratos */

  @GET
  @Path("{idRestaurante}/pratos")
  @Tag(name = "prato")
  public List<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
    Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
    if (restauranteOp.isEmpty()) {
      throw new NotFoundException("Restaurante não existe");
    }
    Stream<Prato> pratos = Prato.stream("restaurante", restauranteOp.get());
    return pratos.map(p -> pratoMapper.toDTO(p)).collect(Collectors.toList());
  }

  @POST
  @Path("{idRestaurante}/pratos")
  @Transactional
  @Tag(name = "prato")
  public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante,
      AdicionarPratoDTO dto) {
    Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

    if (restauranteOp.isEmpty()) {
      throw new NotFoundException("Restaurante não existe");
    }

    Prato prato = pratoMapper.toPrato(dto);
    prato.restaurante = restauranteOp.get();
    prato.persist();
    return Response.status(Status.CREATED).build();
  }

  @PUT
  @Path("{idRestaurante}/pratos/{id}")
  @Transactional
  @Tag(name = "prato")
  public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id,
      AtualizarPratoDTO dto) {
    Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
    if (restauranteOp.isEmpty()) {
      throw new NotFoundException("Restaurante não existe");
    }

    // No nosso caso, id do prato vai ser único, independente do restaurante...
    Optional<Prato> pratoOp = Prato.findByIdOptional(id);

    if (pratoOp.isEmpty()) {
      throw new NotFoundException("Prato não existe");
    }
    Prato prato = pratoOp.get();
    pratoMapper.toPrato(dto, prato);
    prato.persist();
  }

  @DELETE
  @Path("{idRestaurante}/pratos/{id}")
  @Transactional
  @Tag(name = "prato")
  public void delete(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
    Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
    if (restauranteOp.isEmpty()) {
      throw new NotFoundException("Restaurante não existe");
    }

    Optional<Prato> pratoOp = Prato.findByIdOptional(id);

    pratoOp.ifPresentOrElse(Prato::delete, () -> {
      throw new NotFoundException();
    });
  }

}
