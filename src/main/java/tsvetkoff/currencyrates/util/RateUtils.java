package tsvetkoff.currencyrates.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tsvetkoff.currencyrates.jooq.main.public_.tables.pojos.Rate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RateUtils {

    public static boolean isSame(Rate rate1, Rate rate2) {
        return rate1 != null &&
                rate2 != null &&
                rate1.getPurchase().compareTo(rate2.getPurchase()) == 0 &&
                rate1.getSale().compareTo(rate2.getSale()) == 0;
    }

}
