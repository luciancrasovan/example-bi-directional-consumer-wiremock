package com.example.products;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductClient {
  private static final String VERSIONED_MEDIA_TYPE = "application/json; x-api-version=3.0";
  private static final String AUTHORIZATION_HEADER_VALUE = "Bearer synthetic-token";
  private final String url;

  public ProductClient(@Value("${basepath}") final String url) {
    this.url = url;
  }

  public Product getKuttyById(final String id) throws IOException {
    return Request.Get(this.url + "/v3/GI/Kutty/" + id)
      .addHeader("Accept", VERSIONED_MEDIA_TYPE)
      .addHeader("Authorization", AUTHORIZATION_HEADER_VALUE)
      .execute().handleResponse(httpResponse -> {
        try {
          final ObjectMapper mapper = new ObjectMapper();
          final Product product = mapper.readValue(httpResponse.getEntity().getContent(), Product.class);

          return product;
        } catch (final JsonMappingException e) {
          throw new IOException(e);
        }
      });
  }

  public List<Product> getKutty() throws IOException {
    return Request.Get(this.url + "/v3/GI/Kutty")
      .addHeader("Accept", VERSIONED_MEDIA_TYPE)
      .addHeader("Authorization", AUTHORIZATION_HEADER_VALUE)
      .execute().handleResponse(httpResponse -> {
        try {
          final ObjectMapper mapper = new ObjectMapper();
          final List<Product> kutty = mapper.readValue(httpResponse.getEntity().getContent(), new TypeReference<List<Product>>(){});

          return kutty;
        } catch (final JsonMappingException e) {
          throw new IOException(e);
        }
      });
  }
}
