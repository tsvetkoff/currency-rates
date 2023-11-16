package tsvetkoff.currencyrates.dao;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tsvetkoff.currencyrates.dto.RateDto;
import tsvetkoff.currencyrates.dto.RateHistoryDto;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tsvetkoff.currencyrates.jooq.main.public_.tables.Rate.RATE;

@JooqTest
@Import(RateDao.class)
@Testcontainers
class RateDaoTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private RateDao rateDao;

    @Autowired
    private DSLContext dslContext;

    @Test
    void testFindCurrentRates() {
        // given
        List<Rate> rates = getRates();
        for (Rate rate : rates) {
            dslContext.insertInto(RATE)
                    .set(dslContext.newRecord(RATE, rate))
                    .execute();
        }
        List<Rate> expectedList = rates.stream()
                .collect(Collectors.toMap(
                        r -> Pair.of(r.getCurrency(), r.getBank()),
                        Function.identity(),
                        (a, b) -> {
                            if (a.getDate().isBefore(b.getDate())) {
                                return b;
                            } else {
                                return a;
                            }
                        }
                )).values().stream()
                .sorted(Comparator.comparing(Rate::getCurrency)
                        .thenComparing(Rate::getBank))
                .toList();

        // then
        List<RateDto> actualList = rateDao.findCurrentRates();
        for (int i = 0; i < expectedList.size(); i++) {
            Rate expected = expectedList.get(i);
            RateDto actual = actualList.get(i);

            assertEquals(expected.getBank(), actual.getBank());
            assertNotNull(actual.getBankName());
            assertEquals(expected.getCurrency(), actual.getCurrency());
            assertEquals(expected.getDate(), actual.getDate());
            assertEquals(0, expected.getPurchase().compareTo(actual.getPurchase()));
            assertEquals(0, expected.getSale().compareTo(actual.getSale()));
        }
    }

    @Test
    void testFindRateHistory() {
        // given
        String bank = "CBR";
        String currency = "USD";
        List<Rate> rates = getRates();
        for (Rate rate : rates) {
            dslContext.insertInto(RATE)
                    .set(dslContext.newRecord(RATE, rate))
                    .execute();
        }

        List<Rate> expectedList = rates.stream()
                .filter(r -> bank.equals(r.getBank()))
                .filter(r -> currency.equals(r.getCurrency()))
                .sorted(Comparator.comparing(Rate::getDate))
                .toList();

        // then
        List<RateHistoryDto> actualList = rateDao.findRateHistory(bank, currency);
        for (int i = 0; i < expectedList.size(); i++) {
            Rate expected = expectedList.get(i);
            RateHistoryDto actual = actualList.get(i);

            assertEquals(expected.getDate(), actual.getDate());
            assertEquals(0, expected.getPurchase().compareTo(actual.getPurchase()));
            assertEquals(0, expected.getSale().compareTo(actual.getSale()));
        }
    }

    @Test
    void testFindRateLessOrEqual() {
        // given
        List<Rate> rates = getRates();
        for (Rate rate : rates) {
            dslContext.insertInto(RATE)
                    .set(dslContext.newRecord(RATE, rate))
                    .execute();
        }

        // then
        for (Rate expected : rates) {
            Rate actual = rateDao.findRateLessOrEqual(expected.getBank(), expected.getCurrency(), expected.getDate());

            assertEquals(expected.getBank(), actual.getBank());
            assertEquals(expected.getCurrency(), actual.getCurrency());
            assertEquals(expected.getDate(), actual.getDate());
            assertEquals(0, expected.getPurchase().compareTo(actual.getPurchase()));
            assertEquals(0, expected.getSale().compareTo(actual.getSale()));
        }
    }

    @Test
    void testInsert() {
        // given
        Rate expected = new Rate();
        expected.setBank("VTB");
        expected.setCurrency("EUR");
        expected.setDate(OffsetDateTime.parse("2023-11-13T12:20:00.204+04:00"));
        expected.setPurchase(new BigDecimal("93.3000"));
        expected.setSale(new BigDecimal("100.2000"));

        // when
        rateDao.insert(expected);

        // then
        Rate actual = dslContext.selectFrom(RATE)
                .where(RATE.ID.eq(expected.getId()))
                .fetchOneInto(Rate.class);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBank(), actual.getBank());
        assertEquals(expected.getCurrency(), actual.getCurrency());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(0, expected.getPurchase().compareTo(actual.getPurchase()));
        assertEquals(0, expected.getSale().compareTo(actual.getSale()));
    }

    private List<Rate> getRates() {
        Rate rate1 = new Rate();
        rate1.setBank("VTB");
        rate1.setCurrency("EUR");
        rate1.setDate(OffsetDateTime.parse("2023-11-13T12:20:00.204+04:00"));
        rate1.setPurchase(new BigDecimal("93.3000"));
        rate1.setSale(new BigDecimal("100.2000"));

        Rate rate2 = new Rate();
        rate2.setBank("CBR");
        rate2.setCurrency("USD");
        rate2.setDate(OffsetDateTime.parse("2023-11-11T11:30:00+04:00"));
        rate2.setPurchase(new BigDecimal("92.0535"));
        rate2.setSale(new BigDecimal("92.0535"));

        Rate rate3 = new Rate();
        rate3.setBank("VTB");
        rate3.setCurrency("USD");
        rate3.setDate(OffsetDateTime.parse("2023-11-07T17:10:00+04:00"));
        rate3.setPurchase(new BigDecimal("89.8500"));
        rate3.setSale(new BigDecimal("94.7000"));

        Rate rate4 = new Rate();
        rate4.setBank("VTB");
        rate4.setCurrency("USD");
        rate4.setDate(OffsetDateTime.parse("2023-11-08T12:20:00+04:00"));
        rate4.setPurchase(new BigDecimal("89.8500"));
        rate4.setSale(new BigDecimal("94.6500"));

        Rate rate5 = new Rate();
        rate5.setBank("VTB");
        rate5.setCurrency("USD");
        rate5.setDate(OffsetDateTime.parse("2023-11-09T10:10:00+04:00"));
        rate5.setPurchase(new BigDecimal("88.0500"));
        rate5.setSale(new BigDecimal("95.1000"));

        return List.of(rate1, rate2, rate3, rate4, rate5);
    }

}
