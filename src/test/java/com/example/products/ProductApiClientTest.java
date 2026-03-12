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
	void getAllKuttyReturnsKuttyList() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v2/Kutty"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json; x-api-version=2.0")
						.withBody("[{ \"id\": \"K-101\", \"name\": \"Archie Kutty\" }]")));

		final JsonNode result = this.productClient.getAllKutty();

		assertThat(result.isArray(), is(true));
		assertThat(result.get(0).get("id").asText(), is("K-101"));
		assertThat(result.get(0).get("name").asText(), is("Archie Kutty"));
	}

	@Test
	void getKuttyByIdReturnsKuttyObject() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v2/Kutty/Kutty/K-101"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json; x-api-version=2.0")
						.withBody("{ \"id\": \"K-101\", \"name\": \"Archie Kutty\" }")));

		final JsonNode result = this.productClient.getKuttyById("K-101");

		assertThat(result.get("id").asText(), is("K-101"));
		assertThat(result.get("name").asText(), is("Archie Kutty"));
	}

	@Test
	void getAllWittyReturnsWittyList() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v2/Witty"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json; x-api-version=2.0")
						.withBody("[{ \"id\": \"W-201\", \"items\": [{\"scope\": {\"ref\": \"SCOPE-1\"}, \"metric\": 1.5, \"refId\": \"REF-1\"}] }]")));

		final JsonNode result = this.productClient.getAllWitty();

		assertThat(result.isArray(), is(true));
		assertThat(result.get(0).get("id").asText(), is("W-201"));
	}

	@Test
	void getWittyByIdReturnsWittyObject() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v2/Witty/Witty/W-201"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json; x-api-version=2.0")
						.withBody("{ \"id\": \"W-201\", \"items\": [{\"scope\": {\"ref\": \"SCOPE-1\"}, \"metric\": 1.5, \"refId\": \"REF-1\"}] }")));

		final JsonNode result = this.productClient.getWittyById("W-201");

		assertThat(result.get("id").asText(), is("W-201"));
		assertThat(result.get("items").get(0).get("scope").get("ref").asText(), is("SCOPE-1"));
	}

	@Test
	void getHealthReturnsStatus() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/health"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)));

		final JsonNode result = this.productClient.getHealth();

		assertThat(result.isObject(), is(true));
	}
}