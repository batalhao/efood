package com.batalhao.efood.pedido.model;

import java.util.List;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

@MongoEntity(collection = "pedidos", database = "efood_pedidos")
public class Pedido extends PanacheMongoEntity {

  public String cliente;

  public List<Prato> pratos;

  public Restaurante restaurante;

  public String entregador;

  public Localizacao localizacaoEntregador;

}
