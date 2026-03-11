package com.example.products;

import java.io.IOException;

import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductClient {
  private static final String MEDIA_TYPE = "application/json; x-api-version=1.0";
  private final String url;

  public ProductClient(@Value("${basepath}") final String url) {
    this.url = url;
  }

  public JsonNode getKuttyById(final int id) throws IOException {
    return this.executeGet("/v1/Kutty/Id/" + id);
  }

  public JsonNode getKuttyByShortName(final String shortName) throws IOException {
    return this.executeGet("/v1/Kutty/ShortName/" + shortName);
  }

  private JsonNode executeGet(final String path) throws IOException {
    return Request.Get(this.url + path)
        .addHeader("Accept", MEDIA_TYPE)
        .execute().handleResponse(httpResponse -> {
          try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode body = mapper.readTree(httpResponse.getEntity().getContent());

            return body;
          } catch (final JsonMappingException e) {
            throw new IOException(e);
          }
        });
  }
}
