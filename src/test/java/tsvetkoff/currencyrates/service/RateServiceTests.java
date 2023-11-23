package tsvetkoff.currencyrates.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import tsvetkoff.currencyrates.client.BankClient;
import tsvetkoff.currencyrates.client.CbrClient;
import tsvetkoff.currencyrates.client.KoshelevClient;
import tsvetkoff.currencyrates.client.VtbClient;
import tsvetkoff.currencyrates.dao.RateDao;

import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class RateServiceTests {

    @InjectMocks
    private RateService rateService;

    @Spy
    private ArrayList<BankClient> bankClients;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private VtbClient vtbClient;

    @Mock
    private CbrClient cbrClient;

    @Mock
    private KoshelevClient koshelevClient;

    @Mock
    private RateDao rateDao;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetCurrentRates() {
        rateService.getCurrentRates();

        verify(rateDao, times(1)).findCurrentRates();
    }

    @Test
    void testGetRateHistory() {
        String bank = "any-bank";
        String currency = "any-currency";

        rateService.getRateHistory(bank, currency);

        verify(rateDao, times(1)).findRateHistory(bank, currency);
    }

    @Test
    void testUpdateRates() {
        // given
        bankClients.add(vtbClient);
        bankClients.add(cbrClient);
        bankClients.add(koshelevClient);

        // then
        rateService.updateRates();

        verify(vtbClient, times(1)).getRates();
        verify(cbrClient, times(1)).getRates();
        verify(koshelevClient, times(1)).getRates();
    }

}
