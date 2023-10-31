package tsvetkoff.currencyrates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkoff.currencyrates.jooq.main.public_.tables.daos.RateDao;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;
import tsvetkoff.currencyrates.util.RateUtils;

import static tsvetkoff.currencyrates.jooq.main.public_.tables.Rate.RATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

    private final RateDao rateDao;

    @Transactional
    public void saveIfChanged(Rate rate) {
        Rate prevRate = rateDao.ctx()
                .selectFrom(RATE)
                .where(
                        RATE.BANK.eq(rate.getBank()),
                        RATE.CURRENCY.eq(rate.getCurrency()),
                        RATE.DATE.lessOrEqual(rate.getDate())
                ).orderBy(RATE.DATE.desc())
                .limit(1)
                .fetchOneInto(Rate.class);

        if (RateUtils.isSame(prevRate, rate)) {
            return;
        }

        log.debug("Save rate: {}", rate);
        rateDao.insert(rate);
    }

}
