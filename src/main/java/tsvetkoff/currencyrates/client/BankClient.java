package tsvetkoff.currencyrates.client;

import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.util.List;

public interface BankClient {
    List<Rate> getRates();
}
