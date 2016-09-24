package org.pennyledger.http;

import org.osgi.service.component.annotations.Component;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@Context("/ddd")
@Resource(extensions={".js"}, base="static")
@Component(immediate=true)
public class TripleDReource implements HttpHandler {

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    //ResponseCodeHandler.HANDLE_404.handleRequest(exchange);
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
    exchange.getResponseSender().send("/ddd handler...");
  }

}
