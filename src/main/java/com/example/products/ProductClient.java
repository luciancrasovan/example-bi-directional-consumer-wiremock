package com.example.products;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;

@Service
public class ProductClient {
  private static final String MEDIA_TYPE = "application/json; x-api-version=2.0";
  private final String url;

  public ProductClient(@Value("${basepath}") final String url) {
    this.url = url;
  }

  public JsonNode getAllKutty() throws IOException {
    return this.executeGet("/v2/Kutty");
  }

  public JsonNode getKuttyById(final String id) throws IOException {
    return this.executeGet("/v2/Kutty/Kutty/" + id);
  }

  public JsonNode getAllWitty() throws IOException {
    return this.executeGet("/v2/Witty");
  }

  public JsonNode getWittyById(final String id) throws IOException {
    return this.executeGet("/v2/Witty/Witty/" + id);
  }

  public JsonNode getHealth() throws IOException {
    return this.executeGet("/health");
  }

  private JsonNode executeGet(final String path) throws IOException {
    return Request.Get(this.url + path)
        .addHeader("Accept", MEDIA_TYPE)
        .execute().handleResponse(httpResponse -> {
          try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode body = this.parseBody(httpResponse.getEntity(), mapper);

            return body;
          } catch (final JsonMappingException e) {
            throw new IOException(e);
          }
        });
  }

  private JsonNode parseBody(final HttpEntity entity, final ObjectMapper mapper) throws IOException {
    if (entity == null) {
      return mapper.createObjectNode();
    }

    final byte[] payload = entity.getContent().readAllBytes();
    if (payload.length == 0) {
      return mapper.createObjectNode();
    }

    final String text = new String(payload, StandardCharsets.UTF_8).trim();
    if (text.isEmpty()) {
      return mapper.createObjectNode();
    }

    return mapper.readTree(text);
  }
}
