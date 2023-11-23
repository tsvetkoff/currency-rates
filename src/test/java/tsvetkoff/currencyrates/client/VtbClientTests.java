package tsvetkoff.currencyrates.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import tsvetkoff.currencyrates.config.RestProperties;
import tsvetkoff.currencyrates.config.RestTemplateConfiguration;
import tsvetkoff.currencyrates.dto.VtbDto;
import tsvetkoff.currencyrates.dto.VtbDto.VtbRate;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(
        components = {
                VtbClient.class,
                RestTemplateConfiguration.class,
                RestProperties.class
        }
)
class VtbClientTests {

    @Autowired
    private VtbClient vtbClient;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${data.bank-clients.vtb.base-url}")
    private String baseUrl;

    @Value("${data.bank-clients.vtb.rate-url}")
    private String rateUrl;

    @Value("classpath:vtb-response.json")
    private Resource resource;

    @Test
    void testGetRates() throws IOException {
        // given
        String response = resource.getContentAsString(StandardCharsets.UTF_8);
        VtbDto vtbDto = objectMapper.readValue(response, VtbDto.class);
        Map<VtbDto.Currency, VtbRate> vtbRatesMap = vtbDto.getRates().stream()
                .collect(Collectors.toMap(
                        VtbRate::getCurrency1,
                        Function.identity(),
                        (a, b) -> a
                ));

        // when
        this.server.expect(requestTo(baseUrl + rateUrl))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        // then
        List<Rate> rates = vtbClient.getRates();

        assertEquals(vtbRatesMap.size(), rates.size());

        for (Map.Entry<VtbDto.Currency, VtbRate> entry : vtbRatesMap.entrySet()) {
            String currency = entry.getKey().getCode();
            VtbRate expected = entry.getValue();
            Rate actual = rates.stream()
                    .filter(r -> currency.equals(r.getCurrency()))
                    .findFirst()
                    .orElseThrow();

            assertNull(actual.getId());
            assertEquals("VTB", actual.getBank());
            assertEquals(currency, actual.getCurrency());
            assertNull(actual.getDate());
            assertEquals(0, expected.getBid().compareTo(actual.getPurchase()));
            assertEquals(0, expected.getOffer().compareTo(actual.getSale()));
        }
    }

}
