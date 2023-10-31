package tsvetkoff.currencyrates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tsvetkoff.currencyrates.client.BankClient;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateScheduler {

    private final List<BankClient> bankClients;
    private final CurrencyService currencyService;
    private final RateService rateService;

    @Scheduled(cron = "${data.update_rates.cron}")
    @SchedulerLock(
            name = "currency-rates_updateRates",
            lockAtLeastFor = "5m",
            lockAtMostFor = "10m"
    )
    public void updateRates() {
        log.debug("Start updateRates()");
        try {
            OffsetDateTime date = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            for (BankClient bankClient : bankClients) {
                updateRatesForBank(bankClient, date);
            }
        } finally {
            log.debug("Finish updateRates()");
        }
    }

    private void updateRatesForBank(BankClient bankClient, OffsetDateTime date) {
        try {
            List<Rate> rates = bankClient.getRates();
            for (Rate rate : rates) {
                rate.setDate(date);
                log.debug("Parsed rate: {}", rate);
            }

            for (Rate rate : rates) {
                currencyService.saveIfMissing(rate.getCurrency());
                rateService.saveIfChanged(rate);
            }
        } catch (Exception e) {
            log.error("Error while update rates for bank", e);
        }
    }

}
