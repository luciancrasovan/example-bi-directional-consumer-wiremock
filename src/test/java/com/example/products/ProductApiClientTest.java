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

	private static final String PITY_REQUEST = """
			{
			  "c": {
			    "sourceSystem": "archie-app",
			    "a": [
			      {
			        "m": "payload",
			        "p": [
			          {
			            "id": 101,
			            "productNr": "P-101",
			            "modelNr": "M-101",
			            "pxid": "PX-101",
			            "t": []
			          }
			        ]
			      }
			    ]
			  }
			}
			""";

	@Test
	void postNottyReturnsOkDecision() throws IOException {

		this.wiremock.stubFor(WireMock.post(WireMock.urlPathEqualTo("/pitty/notty"))
				.withQueryParam("force", WireMock.equalTo("false"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.withHeader("Content-Type", WireMock.containing("application/json"))
				.withHeader("X-WOODSTOCK-PASS", WireMock.equalTo("festival-pass"))
				.willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
						.withBody("{ \"status\": \"Accepted\", \"decision\": \"P1\", \"validationResults\": [{\"topic\": \"Pitty.Payload\", \"errors\": [], \"warnings\": [{\"warningCode\": \"W-001\", \"message\": \"W-001 warning detected\"}]}] }")));

		final JsonNode result = this.productClient.postNotty(PITY_REQUEST, false);

		assertThat(result.get("status").asText(), is("Accepted"));
		assertThat(result.get("decision").asText(), is("P1"));
		assertThat(result.get("validationResults").get(0).get("topic").asText(), is("Pitty.Payload"));
	}

	@Test
	void postNottyReturnsUnauthorizedWithoutFestivalPass() throws IOException {

		this.wiremock.stubFor(WireMock.post(WireMock.urlPathEqualTo("/pitty/notty"))
				.withQueryParam("force", WireMock.equalTo("true"))
				.withHeader("Accept", WireMock.containing("application/json"))
				.withHeader("Content-Type", WireMock.containing("application/json"))
				.withHeader("X-WOODSTOCK-PASS", WireMock.absent())
				.willReturn(aResponse().withStatus(401).withHeader("Content-Type", "application/json")
						.withBody("{ \"statusCode\": \"Unauthorized\", \"description\": \"Missing festival pass.\", \"exception\": \"AuthorizationException\" }")));

		final JsonNode result = this.productClient.postNotty(PITY_REQUEST, true, false);
		assertThat(result.get("statusCode").asText(), is("Unauthorized"));
		assertThat(result.get("exception").asText(), is("AuthorizationException"));
	}
}