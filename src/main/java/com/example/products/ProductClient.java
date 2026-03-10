package com.example.products;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.entity.ContentType;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductClient {
  private static final String MEDIA_TYPE = "application/json";
  private static final String FESTIVAL_PASS_HEADER = "X-WOODSTOCK-PASS";
  private static final String FESTIVAL_PASS_VALUE = "festival-pass";
  private final String url;

  public ProductClient(@Value("${basepath}") final String url) {
    this.url = url;
  }

  public JsonNode postNotty(final String pittyBody, final boolean force) throws IOException {
    return this.postNotty(pittyBody, force, true);
  }

  public JsonNode postNotty(final String pittyBody, final boolean force, final boolean includeFestivalPass) throws IOException {
    final Request request = Request.Post(this.url + "/pitty/notty?force=" + force)
      .addHeader("Accept", MEDIA_TYPE)
      .addHeader("Content-Type", MEDIA_TYPE)
      .bodyByteArray(pittyBody.getBytes(StandardCharsets.UTF_8), ContentType.APPLICATION_JSON);

    if (includeFestivalPass) {
      request.addHeader(FESTIVAL_PASS_HEADER, FESTIVAL_PASS_VALUE);
    }

    return request
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
