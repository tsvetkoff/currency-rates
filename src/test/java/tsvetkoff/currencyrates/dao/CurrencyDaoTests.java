package tsvetkoff.currencyrates.dao;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Currency;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tsvetkoff.currencyrates.jooq.main.public_.Tables.CURRENCY;

@JooqTest
@Import(CurrencyDao.class)
@Testcontainers
class CurrencyDaoTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private DSLContext dslContext;

    @Test
    void testExistsById() {
        // given
        List<String> currencies = dslContext.select(CURRENCY.ID)
                .from(CURRENCY)
                .fetchInto(String.class);

        // then
        for (String currency : currencies) {
            assertTrue(currencyDao.existsById(currency));
        }
    }

    @Test
    void testInsert() {
        // given
        Currency expected = new Currency();
        expected.setId("test-id");
        expected.setName("test-name");

        // when
        currencyDao.insert(expected);

        // then
        Currency actual = dslContext.selectFrom(CURRENCY)
                .where(CURRENCY.ID.eq(expected.getId()))
                .fetchOneInto(Currency.class);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }

}
