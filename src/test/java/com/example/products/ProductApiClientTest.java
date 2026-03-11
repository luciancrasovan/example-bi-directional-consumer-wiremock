package com.example.products;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;

@SpringBootTest
@AutoConfigureMockMvc
class ProductApiClientTest extends WireMockPactBaseTest {
    @InjectWireMock("wiremock-service-name")
    private WireMockServer wiremock;
	
	@Autowired
	private ProductClient productClient;

	@Test
	void getKuttyByIdReturnsKuttyObject() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/Kutty/Id/101"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json; x-api-version=1.0")
						.withBody("{ \"id\": 101, \"name\": \"Archie Item\", \"code\": \"ALPHA\", \"metaB\": \"core\" }")));

		final JsonNode result = this.productClient.getKuttyById(101);

		assertThat(result.get("id").asInt(), is(101));
		assertThat(result.get("name").asText(), is("Archie Item"));
		assertThat(result.get("code").asText(), is("ALPHA"));
		assertThat(result.get("metaB").asText(), is("core"));
	}

	@Test
	void getKuttyByShortNameReturnsKuttyObject() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/Kutty/ShortName/ALPHA"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json; x-api-version=1.0")
						.withBody("{ \"id\": 101, \"name\": \"Archie Item\", \"code\": \"ALPHA\", \"metaB\": \"core\" }")));

		final JsonNode result = this.productClient.getKuttyByShortName("ALPHA");

		assertThat(result.get("id").asInt(), is(101));
		assertThat(result.get("code").asText(), is("ALPHA"));
	}
}