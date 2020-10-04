package com.batalhao.efood.pedido.incoming;

import java.io.IOException;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import org.bson.types.Decimal128;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import com.batalhao.efood.pedido.dto.PedidoRealizadoDTO;
import com.batalhao.efood.pedido.dto.PratoPedidoDTO;
import com.batalhao.efood.pedido.model.Pedido;
import com.batalhao.efood.pedido.model.Prato;
import com.batalhao.efood.pedido.model.Restaurante;

@ApplicationScoped
public class PedidoRealizadoIncoming {

  // @Inject
  // ESService elastic;

  @Incoming("pedidos")
  public void lerPedidos(PedidoRealizadoDTO dto) throws IOException {
    System.out.println(dto.toString());

    Pedido p = new Pedido();
    p.cliente = dto.cliente;
    p.pratos = new ArrayList<>();
    dto.pratos.forEach(prato -> p.pratos.add(from(prato)));
    Restaurante restaurante = new Restaurante();
    restaurante.nome = dto.restaurante.nome;
    p.restaurante = restaurante;

    // Elasticsearch
    // String json = JsonbBuilder.create().toJson(dto);
    // elastic.index("pedidos", json);

    p.persist();
  }

  private Prato from(PratoPedidoDTO prato) {
    Prato p = new Prato();
    p.descricao = prato.descricao;
    p.nome = prato.nome;
    p.preco = new Decimal128(prato.preco);
    return p;
  }

}
