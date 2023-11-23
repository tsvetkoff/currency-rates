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
import tsvetkoff.currencyrates.dto.CbrDto;
import tsvetkoff.currencyrates.dto.CbrDto.Valute;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(
        components = {
                CbrClient.class,
                RestTemplateConfiguration.class,
                RestProperties.class
        }
)
class CbrClientTests {

    @Autowired
    private CbrClient cbrClient;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${data.bank-clients.cbr.base-url}")
    private String baseUrl;

    @Value("${data.bank-clients.cbr.rate-url}")
    private String rateUrl;

    @Value("classpath:cbr-response.json")
    private Resource resource;

    @Test
    void testGetRates() throws IOException {
        // given
        String response = resource.getContentAsString(StandardCharsets.UTF_8);
        CbrDto cbrDto = objectMapper.readValue(response, CbrDto.class);

        // when
        this.server.expect(requestTo(baseUrl + rateUrl))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        // then
        List<Rate> rates = cbrClient.getRates();

        assertEquals(cbrDto.getValute().size(), rates.size());

        for (Map.Entry<String, Valute> entry : cbrDto.getValute().entrySet()) {
            String currency = entry.getKey();
            Valute expected = cbrDto.getValute().get(currency);
            Rate actual = rates.stream()
                    .filter(r -> currency.equals(r.getCurrency()))
                    .findFirst()
                    .orElseThrow();

            assertNull(actual.getId());
            assertEquals("CBR", actual.getBank());
            assertEquals(currency, actual.getCurrency());
            assertNull(actual.getDate());

            BigDecimal value = expected.getValue().divide(expected.getNominal(), RoundingMode.HALF_UP);
            assertEquals(0, value.compareTo(actual.getPurchase()));
            assertEquals(0, value.compareTo(actual.getSale()));
        }
    }

}
