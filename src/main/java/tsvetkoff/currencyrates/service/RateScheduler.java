package tsvetkoff.currencyrates.service;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
