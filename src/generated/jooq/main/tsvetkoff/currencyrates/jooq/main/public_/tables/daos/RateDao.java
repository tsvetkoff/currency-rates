/*
 * This file is generated by jOOQ.
 */
package tsvetkoff.currencyrates.jooq.main.public_.tables.daos;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;

import tsvetkoff.currencyrates.jooq.main.public_.tables.Rate;
import tsvetkoff.currencyrates.jooq.main.public_.tables.records.RateRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RateDao extends DAOImpl<RateRecord, tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate, UUID> {

    /**
     * Create a new RateDao without any configuration
     */
    public RateDao() {
        super(Rate.RATE, tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate.class);
    }

    /**
     * Create a new RateDao with an attached configuration
     */
    public RateDao(Configuration configuration) {
        super(Rate.RATE, tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate.class, configuration);
    }

    @Override
    public UUID getId(tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchRangeOfId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(Rate.RATE.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchById(UUID... values) {
        return fetch(Rate.RATE.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate fetchOneById(UUID value) {
        return fetchOne(Rate.RATE.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchOptionalById(UUID value) {
        return fetchOptional(Rate.RATE.ID, value);
    }

    /**
     * Fetch records that have <code>bank BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchRangeOfBank(String lowerInclusive, String upperInclusive) {
        return fetchRange(Rate.RATE.BANK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>bank IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchByBank(String... values) {
        return fetch(Rate.RATE.BANK, values);
    }

    /**
     * Fetch records that have <code>currency BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchRangeOfCurrency(String lowerInclusive, String upperInclusive) {
        return fetchRange(Rate.RATE.CURRENCY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>currency IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchByCurrency(String... values) {
        return fetch(Rate.RATE.CURRENCY, values);
    }

    /**
     * Fetch records that have <code>date BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchRangeOfDate(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Rate.RATE.DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>date IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchByDate(LocalDateTime... values) {
        return fetch(Rate.RATE.DATE, values);
    }

    /**
     * Fetch records that have <code>purchase BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchRangeOfPurchase(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Rate.RATE.PURCHASE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>purchase IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchByPurchase(BigDecimal... values) {
        return fetch(Rate.RATE.PURCHASE, values);
    }

    /**
     * Fetch records that have <code>sale BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchRangeOfSale(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Rate.RATE.SALE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>sale IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate> fetchBySale(BigDecimal... values) {
        return fetch(Rate.RATE.SALE, values);
    }
}
