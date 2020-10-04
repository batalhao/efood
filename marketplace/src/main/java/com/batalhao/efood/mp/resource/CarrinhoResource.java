package com.batalhao.efood.mp.resource;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import com.batalhao.efood.mp.dto.PedidoRealizadoDTO;
import com.batalhao.efood.mp.dto.PratoDTO;
import com.batalhao.efood.mp.dto.PratoPedidoDTO;
import com.batalhao.efood.mp.dto.RestauranteDTO;
import com.batalhao.efood.mp.model.Prato;
import com.batalhao.efood.mp.model.PratoCarrinho;
import io.smallrye.mutiny.Uni;

@Path("carrinho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarrinhoResource {

  private static final String CLIENTE = "a";

  @Inject
  io.vertx.mutiny.pgclient.PgPool client;

  @Inject
  @Channel("pedidos")
  Emitter<PedidoRealizadoDTO> emitterPedido;
  // Emitter<String> emitterPedido;

  @GET
  @Tag(name = "carrinho")
  public Uni<List<PratoCarrinho>> buscarcarrinho() {
    return PratoCarrinho.findCarrinho(client, CLIENTE);
  }

  @POST
  @Path("/prato/{idPrato}")
  @Tag(name = "carrinho")
  public Uni<Long> adicionarPrato(@PathParam("idPrato") Long idPrato) {
    // TODO - Can return the last PEDIDO
    PratoCarrinho pc = new PratoCarrinho();
    pc.cliente = CLIENTE;
    pc.prato = idPrato;
    return PratoCarrinho.save(client, CLIENTE, idPrato);

  }

  @POST
  @Path("/realizar-pedido")
  @Tag(name = "carrinho")
  public Uni<Boolean> finalizar() {
    PedidoRealizadoDTO pedido = new PedidoRealizadoDTO();
    String cliente = CLIENTE;
    pedido.cliente = cliente;
    List<PratoCarrinho> pratoCarrinho =
        PratoCarrinho.findCarrinho(client, cliente).await().indefinitely();

    // TODO - Change to MapStruts

    List<PratoPedidoDTO> pratos =
        pratoCarrinho.stream().map(pc -> from(pc)).collect(Collectors.toList());
    pedido.pratos = pratos;

    RestauranteDTO restaurante = new RestauranteDTO();
    restaurante.nome = "nome restaurante";
    pedido.restaurante = restaurante;

    // Sending to Kafka
    emitterPedido.send(pedido);
    // Jsonb create = JsonbBuilder.create();
    // String json = create.toJson(pedido);
    // emitterPedido.send(json);

    return PratoCarrinho.delete(client, cliente);
  }

  private PratoPedidoDTO from(PratoCarrinho pratoCarrinho) {
    PratoDTO dto = Prato.findById(client, pratoCarrinho.prato).await().indefinitely();
    return new PratoPedidoDTO(dto.nome, dto.descricao, dto.preco);
  }

}
