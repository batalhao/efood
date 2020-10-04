package com.batalhao.efood.pedido.deserializer;

import com.batalhao.efood.pedido.dto.PedidoRealizadoDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PedidoDeserializer extends ObjectMapperDeserializer<PedidoRealizadoDTO> {

  public PedidoDeserializer() {
    super(PedidoRealizadoDTO.class);
  }

}
