package tsvetkoff.currencyrates.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RateUtils {

    /**
     * Проверка, что два курса валют совпадают по всем полям, кроме даты
     *
     * @param rate1 Первый курс валют
     * @param rate2 Первый курс валют
     * @return Признак того, что два курса валют совпадают по всем полям, кроме даты
     */
    public static boolean isSame(Rate rate1, Rate rate2) {
        return rate1 != null &&
                rate2 != null &&
                rate1.getBank().equals(rate2.getBank()) &&
                rate1.getCurrency().equals(rate2.getCurrency()) &&
                rate1.getPurchase().compareTo(rate2.getPurchase()) == 0 &&
                rate1.getSale().compareTo(rate2.getSale()) == 0;
    }

}
