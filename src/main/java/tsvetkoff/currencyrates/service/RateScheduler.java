package tsvetkoff.currencyrates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tsvetkoff.currencyrates.client.BankClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateScheduler {

    private final RateService rateService;

    @Scheduled(cron = "${data.update_rates.cron}")
    @SchedulerLock(
            name = "currency-rates_updateRates",
            lockAtLeastFor = "5m",
            lockAtMostFor = "10m"
    )
    public void startUpdateRates() {
        rateService.updateRates();
    }

}
