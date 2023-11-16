package tsvetkoff.currencyrates.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Объект ответа для банка ВТБ.
 */
@Data
public class VtbDto {

    private Category category;
    private LocalDateTime dateFrom;
    private List<VtbRate> rates;

    @Data
    public static class Category {
        private Integer id;
        private String name;
        private String description;
        private String target;
    }

    @Data
    public static class VtbRate {
        private Currency currency1;
        private Currency currency2;
        private BigDecimal bid;
        private BigDecimal offer;
        private BigDecimal scale;
        private String tooltip;
    }

    @Data
    public static class Currency {
        private String code;
        private String rusName;
        private String symbol;
    }

}
