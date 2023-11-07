package tsvetkoff.currencyrates.dao;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.WindowDefinition;
import org.springframework.stereotype.Repository;
import tsvetkoff.currencyrates.dto.RateDto;
import tsvetkoff.currencyrates.dto.RateHistoryDto;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.select;
import static tsvetkoff.currencyrates.jooq.main.public_.tables.Bank.BANK;
import static tsvetkoff.currencyrates.jooq.main.public_.tables.Rate.RATE;

@Repository
@RequiredArgsConstructor
public class RateDao {

    private final DSLContext dslContext;

    public List<RateDto> findCurrentRates() {
        WindowDefinition w = name("w").as(partitionBy(RATE.CURRENCY, RATE.BANK).orderBy(RATE.DATE.desc()));
        return dslContext.selectFrom(
                        select(
                                RATE.CURRENCY,
                                RATE.BANK,
                                BANK.NAME.as("bank_name"),
                                RATE.DATE,
                                firstValue(RATE.PURCHASE).over(w).as(RATE.PURCHASE),
                                firstValue(RATE.SALE).over(w).as(RATE.SALE),
                                rank().over(w).as("pos")
                        )
                                .from(RATE)
                                .innerJoin(BANK).on(RATE.BANK.eq(BANK.ID))
                                .where(RATE.DATE.lessOrEqual(OffsetDateTime.now()))
                                .window(w)
                                .asTable()
                )
                .where(field("pos").eq(1))
                .orderBy(1, 2)
                .fetchInto(RateDto.class);
    }

    public List<RateHistoryDto> findRateHistory(String bank, String currency) {
        return dslContext.select(RATE.DATE, RATE.PURCHASE, RATE.SALE)
                .from(RATE)
                .where(
                        RATE.BANK.eq(bank),
                        RATE.CURRENCY.eq(currency),
                        RATE.DATE.lessOrEqual(OffsetDateTime.now())
                )
                .orderBy(1)
                .fetchInto(RateHistoryDto.class);
    }

    public Rate findRateLessOrEqual(String bank, String currency, OffsetDateTime date) {
        return dslContext.selectFrom(RATE)
                .where(
                        RATE.BANK.eq(bank),
                        RATE.CURRENCY.eq(currency),
                        RATE.DATE.lessOrEqual(date)
                ).orderBy(RATE.DATE.desc())
                .limit(1)
                .fetchOneInto(Rate.class);
    }

    public void insert(Rate rate) {
        UUID id = dslContext.insertInto(RATE)
                .set(dslContext.newRecord(RATE, rate))
                .returningResult(RATE.ID)
                .fetchOneInto(UUID.class);

        rate.setId(id);
    }

}
