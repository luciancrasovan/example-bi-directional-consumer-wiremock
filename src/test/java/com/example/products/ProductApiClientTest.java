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
	void getAllVkLReturnsCollection() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/AL/VkL"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("[{ \"shortName\": \"ALPHA\" }, { \"shortName\": \"BETA\" }]")));

		final JsonNode result = this.productClient.getAllVkL();

		assertThat(result.isArray(), is(true));
		assertThat(result.get(0).get("shortName").asText(), is("ALPHA"));
	}

	@Test
	void deleteVkLByShortNameReturnsDeletedItem() throws IOException {

		this.wiremock.stubFor(WireMock.delete(WireMock.urlPathEqualTo("/v1/AL/VkL/ALPHA"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{ \"shortName\": \"ALPHA\", \"deleted\": true }")));

		final JsonNode result = this.productClient.deleteVkLByShortName("ALPHA");

		assertThat(result.get("shortName").asText(), is("ALPHA"));
		assertThat(result.get("deleted").asBoolean(), is(true));
	}

	@Test
	void getAllKuttyReturnsKuttyList() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/Kutty"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json; x-api-version=1.0")
						.withBody("[{ \"id\": 101, \"name\": \"Archie Item\", \"code\": \"ALPHA\", \"metaB\": \"core\" }]")));

		final JsonNode result = this.productClient.getAllKutty();

		assertThat(result.isArray(), is(true));
		assertThat(result.get(0).get("id").asInt(), is(101));
	}

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

	@Test
	void getHealthReturnsStatus() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/health"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.willReturn(aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{ \"status\": \"UP\" }")));

		final JsonNode result = this.productClient.getHealth();

		assertThat(result.get("status").asText(), is("UP"));
	}
}