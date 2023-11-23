package tsvetkoff.currencyrates.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Component
@ConditionalOnProperty(
        name = "data.bank-clients.koshelev.enabled",
        havingValue = "true"
)
public class KoshelevClient implements BankClient {

    private static final String BANK = "Koshelev Bank";

    private final String baseUrl;
    private final String rateUrl;

    public KoshelevClient(@Value("${data.bank-clients.koshelev.base-url}") String baseUrl,
                          @Value("${data.bank-clients.koshelev.rate-url}") String rateUrl) {
        this.baseUrl = baseUrl;
        this.rateUrl = rateUrl;
    }

    public List<Rate> getRates() {
        Document document;
        try {
            document = Jsoup.connect(baseUrl + rateUrl).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements rows = document.select("table tr");
        List<String> currencies = rows.select("td.forex__currency")
                .stream()
                .map(Element::text)
                .map(c -> c.replace('*', ' ').trim())
                .toList();

        Elements purchases = rows.select("td.forex__rate.forex__rate_buying strong");
        Elements sales = rows.select("td.forex__rate.forex__rate_selling strong");

        return currencies.stream()
                .map(currency -> {
                    int index = currencies.indexOf(currency);
                    String purchaseAsString = purchases.get(index).text();
                    String saleAsString = sales.get(index).text();

                    Rate rate = new Rate();
                    rate.setBank(BANK);
                    rate.setCurrency(currency.toUpperCase());
                    rate.setPurchase(new BigDecimal(purchaseAsString.replace(',', '.')));
                    rate.setSale(new BigDecimal(saleAsString.replace(',', '.')));
                    return rate;
                })
                .toList();
    }

}
