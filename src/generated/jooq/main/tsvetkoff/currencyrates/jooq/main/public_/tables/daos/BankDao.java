/*
 * This file is generated by jOOQ.
 */
package tsvetkoff.currencyrates.jooq.main.public_.tables.daos;


import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;

import tsvetkoff.currencyrates.jooq.main.public_.tables.Bank;
import tsvetkoff.currencyrates.jooq.main.public_.tables.records.BankRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BankDao extends DAOImpl<BankRecord, tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank, String> {

    /**
     * Create a new BankDao without any configuration
     */
    public BankDao() {
        super(Bank.BANK, tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank.class);
    }

    /**
     * Create a new BankDao with an attached configuration
     */
    public BankDao(Configuration configuration) {
        super(Bank.BANK, tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank.class, configuration);
    }

    @Override
    public String getId(tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank> fetchRangeOfId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Bank.BANK.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank> fetchById(String... values) {
        return fetch(Bank.BANK.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank fetchOneById(String value) {
        return fetchOne(Bank.BANK.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank> fetchOptionalById(String value) {
        return fetchOptional(Bank.BANK.ID, value);
    }

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Bank.BANK.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Bank> fetchByName(String... values) {
        return fetch(Bank.BANK.NAME, values);
    }
}
