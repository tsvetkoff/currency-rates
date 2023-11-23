package tsvetkoff.currencyrates.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import tsvetkoff.currencyrates.dao.CurrencyDao;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CurrencyServiceTests {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyDao currencyDao;

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
    void testSaveIfMissingWhenExists() {
        // given
        String currency = "any-currency";

        // when
        when(currencyDao.existsById(currency))
                .thenReturn(true);

        // then
        currencyService.saveIfMissing(currency);

        verify(currencyDao, times(1)).existsById(currency);
        verify(currencyDao, never()).insert(any(Currency.class));
    }

    @Test
    void testSaveIfMissingWhenMissing() {
        // given
        String currency = "USD";

        // when
        when(currencyDao.existsById(currency))
                .thenReturn(false);

        // then
        currencyService.saveIfMissing(currency);

        verify(currencyDao, times(1)).existsById(currency);
        verify(currencyDao, times(1)).insert(any(Currency.class));
    }

}
