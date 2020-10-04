package com.batalhao.efood.pedido.service;

import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class ESService {

  private RestHighLevelClient client;

  void startup(@Observes StartupEvent startupEvent) {
    client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
  }

  void shutdown(@Observes ShutdownEvent shutdownEvent) throws IOException {
    client.close();
  }

  public void index(String index, String json) throws IOException {
    IndexRequest ir = new IndexRequest(index).source(json, XContentType.JSON);
    client.index(ir, RequestOptions.DEFAULT);
  }

}
