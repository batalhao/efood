package com.batalhao.efood.pedido.incoming;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import com.batalhao.efood.pedido.dto.PedidoRealizadoDTO;

@ApplicationScoped
public class PedidoRealizadoIncoming {

  @Incoming("pedidos")
  public void lerPedidos(PedidoRealizadoDTO dto) {
    System.out.println("Inicio ===========================");
    System.out.println(dto.toString());
    System.out.println("Fim ===========================");
  }

}
