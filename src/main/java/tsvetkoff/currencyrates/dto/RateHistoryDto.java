package tsvetkoff.currencyrates.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class RateHistoryDto {
    private OffsetDateTime date;
    private BigDecimal purchase;
    private BigDecimal sale;
}
