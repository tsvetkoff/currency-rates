package tsvetkoff.currencyrates.util;

import org.junit.jupiter.api.Test;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RateUtilsTests {

    @Test
    void testIsSame() {
        Rate rate1 = new Rate();
        rate1.setBank("any-bank");
        rate1.setCurrency("any-currency");
        rate1.setDate(OffsetDateTime.now());
        rate1.setPurchase(new BigDecimal("1.0"));
        rate1.setSale(new BigDecimal("2.0"));

        Rate rate2 = new Rate();
        rate2.setBank("any-bank");
        rate2.setCurrency("any-currency");
        rate2.setDate(OffsetDateTime.now().minusDays(1));
        rate2.setPurchase(new BigDecimal("1.0"));
        rate2.setSale(new BigDecimal("2.0"));

        assertTrue(RateUtils.isSame(rate1, rate2));
    }

}
