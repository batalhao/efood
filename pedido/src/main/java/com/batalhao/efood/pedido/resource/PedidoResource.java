package com.batalhao.efood.pedido.resource;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import com.batalhao.efood.pedido.model.Localizacao;
import com.batalhao.efood.pedido.model.Pedido;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
// @SuppressWarnings("deprecation")
public class PedidoResource {

  // @Inject
  // Vertx vertx;
  //
  // @Inject
  // EventBus eventBus;

  // void startup(@Observes Router router) {
  // router.route("/localizacoes*").handler(localizacaoHandler());
  // }

  // private SockJSHandler localizacaoHandler() {
  // SockJSHandler handler = SockJSHandler.create(vertx);
  // PermittedOptions permitted = new PermittedOptions();
  // permitted.setAddress("novaLocalizacao");
  // BridgeOptions bridgeOptions = new BridgeOptions().addOutboundPermitted(permitted);
  // handler.bridge(bridgeOptions);
  // return handler;
  // }

  @GET
  @Tag(name = "pedido")
  public List<PanacheMongoEntityBase> hello() {
    return Pedido.listAll();
  }

  @POST
  @Path("{idPedido}/localizacao")
  @Tag(name = "pedido")
  public Pedido novaLocalizacao(@PathParam("idPedido") String idPedido, Localizacao localizacao) {
    Pedido pedido = Pedido.findById(new ObjectId(idPedido));

    pedido.localizacaoEntregador = localizacao;

    // VertX and webpage
    // String json = JsonbBuilder.create().toJson(localizacao);
    // eventBus.sendAndForget("novaLocalizacao", json);

    pedido.persistOrUpdate();
    return pedido;
  }

}
