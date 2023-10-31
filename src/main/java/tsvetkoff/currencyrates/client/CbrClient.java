package tsvetkoff.currencyrates.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tsvetkoff.currencyrates.dto.CbrDto;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.util.List;

@Component
@ConditionalOnProperty(
        name = "data.bank-clients.cbr.enabled",
        havingValue = "true"
)
public class CbrClient implements BankClient {

    private static final String BANK = "CBR";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String rateUrl;

    public CbrClient(RestTemplate restTemplate,
                     ObjectMapper objectMapper,
                     @Value("${data.bank-clients.cbr.base-url}") String baseUrl,
                     @Value("${data.bank-clients.cbr.rate-url}") String rateUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.rateUrl = rateUrl;
    }

    @Override
    public List<Rate> getRates() {
        String responseASString = restTemplate.getForObject(baseUrl + rateUrl, String.class);
        CbrDto cbrDto;
        try {
            cbrDto = objectMapper.readValue(responseASString, CbrDto.class);
        } catch (JsonProcessingException e) {
            // restTemplate cannot parse js-file (https://www.cbr-xml-daily.ru/daily_json.js) automatically
            throw new RuntimeException(e);
        }

        return cbrDto.getValute().values().stream()
                .map(v -> {
                    Rate rate = new Rate();
                    rate.setBank(BANK);
                    rate.setCurrency(v.getCharCode());
                    rate.setPurchase(v.getValue());
                    rate.setSale(v.getValue());
                    return rate;
                }).toList();
    }

}
