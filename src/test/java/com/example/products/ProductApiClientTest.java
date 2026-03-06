package com.example.products;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

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
	void getKuttyById() throws IOException {

		// Arrange
		this.wiremock.stubFor(WireMock.get(WireMock.urlEqualTo("/v3/GI/Kutty/K-101"))
				.withHeader("Accept", WireMock.containing("application/json; x-api-version=3.0"))
				.withHeader("Authorization", WireMock.matching("Bearer .+"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json; x-api-version=3.0")
						.withBody("{ \"id\": \"K-101\", \"code\": \"KCODE-K-101\", \"name\": \"Synthetic Kutty K-101\", \"category\": \"standard\" }")));

		// Act
		final Product product = this.productClient.getKuttyById("K-101");

		// Assert
		assertThat(product.getId(), is("K-101"));
		assertThat(product.getCode(), is("KCODE-K-101"));
		assertThat(product.getCategory(), is("standard"));
	}

	@Test
	void getKutty() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlEqualTo("/v3/GI/Kutty"))
				.withHeader("Accept", WireMock.containing("application/json; x-api-version=3.0"))
				.withHeader("Authorization", WireMock.matching("Bearer .+"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json; x-api-version=3.0")
						.withBody("[{ \"id\": \"K-101\", \"code\": \"KCODE-K-101\", \"name\": \"Synthetic Kutty K-101\", \"category\": \"standard\" }]")));

		final List<Product> products = this.productClient.getKutty();
		assertThat(products.get(0).getId(), is("K-101"));
		assertThat(products.get(0).getCategory(), is("standard"));
	}

	@Test
	void getWittyById() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v3/BI/Witty/W-201"))
				.withQueryParam("includeDescription", WireMock.equalTo("true"))
				.withHeader("Accept", WireMock.containing("application/json; x-api-version=3.0"))
				.withHeader("Authorization", WireMock.matching("Bearer .+"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json; x-api-version=3.0")
						.withBody("{ \"id\": \"W-201\", \"description\": \"Synthetic Witty W-201\", \"items\": [{\"scope\": {\"ref\": \"SCOPE-1-1\"}, \"metric\": 1.5, \"refId\": \"REF-1-1\"}] }")));

		final Witty witty = this.productClient.getWittyById("W-201", true);
		assertThat(witty.getId(), is("W-201"));
		assertThat(witty.getDescription(), is("Synthetic Witty W-201"));
		assertThat(witty.getItems().get(0).getScope().getRef(), is("SCOPE-1-1"));
	}
}