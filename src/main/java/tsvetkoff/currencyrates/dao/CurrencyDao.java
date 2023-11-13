package tsvetkoff.currencyrates.dao;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Currency;

import static tsvetkoff.currencyrates.jooq.main.public_.tables.Currency.CURRENCY;

@Repository
@RequiredArgsConstructor
public class CurrencyDao {

    private final DSLContext dslContext;

    public boolean existsById(String id) {
        return dslContext.fetchExists(CURRENCY, CURRENCY.ID.eq(id));
    }

    public void insert(Currency currency) {
        dslContext.insertInto(CURRENCY)
                .set(dslContext.newRecord(CURRENCY, currency))
                .execute();
    }

}
