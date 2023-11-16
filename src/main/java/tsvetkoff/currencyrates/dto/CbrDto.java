package tsvetkoff.currencyrates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Объект ответа для ЦБ РФ.
 */
@Data
public class CbrDto {

    @JsonProperty("Date")
    private OffsetDateTime date;

    @JsonProperty("PreviousDate")
    private OffsetDateTime previousDate;

    @JsonProperty("PreviousURL")
    private String previousURL;

    @JsonProperty("Timestamp")
    private OffsetDateTime timestamp;

    @JsonProperty("Valute")
    private Map<String, Valute> valute;

    @Data
    public static class Valute {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("NumCode")
        private String numCode;

        @JsonProperty("CharCode")
        private String charCode;

        @JsonProperty("Nominal")
        private BigDecimal nominal;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Value")
        private BigDecimal value;

        @JsonProperty("Previous")
        private BigDecimal previous;

    }

}
