package tsvetkoff.currencyrates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tsvetkoff.currencyrates.jooq.main.public_.tables.daos.CurrencyDao;
import tsvetkoff.currencyrates.jooq.main.public_.tables.daos.RateDao;

@Configuration
public class JooqDaoConfiguration {

    @Bean
    public CurrencyDao currencyDao(org.jooq.Configuration configuration) {
        return new CurrencyDao(configuration);
    }

    @Bean
    public RateDao rateDao(org.jooq.Configuration configuration) {
        return new RateDao(configuration);
    }

}
