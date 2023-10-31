package tsvetkoff.currencyrates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkoff.currencyrates.jooq.main.public_.tables.daos.CurrencyDao;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Currency;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final CurrencyDao currencyDao;

    @Transactional
    public void saveIfMissing(String id) {
        if (currencyDao.existsById(id)) {
            return;
        }

        Currency currency = new Currency();
        currency.setId(id);

        log.debug("Save currency: {}", currency);
        currencyDao.insert(currency);
    }

}
