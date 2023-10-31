package tsvetkoff.currencyrates.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tsvetkoff.currencyrates.dto.VtbDto;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(
        name = "data.bank-clients.vtb.enabled",
        havingValue = "true"
)
public class VtbClient implements BankClient {

    private static final String BANK = "VTB";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String rateUrl;

    public VtbClient(RestTemplate restTemplate,
                     @Value("${data.bank-clients.vtb.base-url}") String baseUrl,
                     @Value("${data.bank-clients.vtb.rate-url}") String rateUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.rateUrl = rateUrl;
    }

    @Override
    public List<Rate> getRates() {
        VtbDto vtbDto = restTemplate.getForObject(baseUrl + rateUrl, VtbDto.class);
        if (vtbDto == null) {
            return Collections.emptyList();
        }

        Map<String, Rate> rateMap = vtbDto.getRates().stream()
                .map(v -> {
                    Rate rate = new Rate();
                    rate.setBank(BANK);
                    rate.setCurrency(v.getCurrency1().getCode());
                    rate.setPurchase(v.getBid());
                    rate.setSale(v.getOffer());
                    return rate;
                }).collect(Collectors.toMap(
                        Rate::getCurrency,
                        Function.identity(),
                        (a, b) -> a
                ));

        return new ArrayList<>(rateMap.values());
    }

}
