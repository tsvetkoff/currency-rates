package tsvetkoff.currencyrates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tsvetkoff.currencyrates.client.BankClient;
import tsvetkoff.currencyrates.dao.RateDao;
import tsvetkoff.currencyrates.dto.RateDto;
import tsvetkoff.currencyrates.dto.RateHistoryDto;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;
import tsvetkoff.currencyrates.util.RateUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

    private final List<BankClient> bankClients;
    private final CurrencyService currencyService;
    private final RateDao rateDao;

    public List<RateDto> getCurrentRates() {
        log.debug("Get current rates");
        List<RateDto> rates = rateDao.findCurrentRates();
        log.debug("Result: {}", rates);
        return rates;
    }

    public List<RateHistoryDto> getRateHistory(String bank, String currency) {
        log.debug("Get rate history for bank = {} and currency = {}", bank, currency);
        List<RateHistoryDto> rateHistory = rateDao.findRateHistory(bank, currency);
        log.debug("Result: {}", rateHistory);
        return rateHistory;
    }

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
                saveIfChanged(rate);
            }
        } catch (Exception e) {
            log.error("Error while update rates for bank", e);
        }
    }

    private void saveIfChanged(Rate rate) {
        Rate prevRate = rateDao.findRateLessOrEqual(
                rate.getBank(),
                rate.getCurrency(),
                rate.getDate()
        );

        if (RateUtils.isSame(prevRate, rate)) {
            return;
        }

        rateDao.insert(rate);
        log.debug("Save rate: {}", rate);
    }

}
