package tsvetkoff.currencyrates.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class RateDto {
    private String bank;
    private String bankName;
    private String currency;
    private OffsetDateTime date;
    private BigDecimal purchase;
    private BigDecimal sale;
}
