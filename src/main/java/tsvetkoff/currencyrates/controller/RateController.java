package tsvetkoff.currencyrates.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tsvetkoff.currencyrates.dto.RateDto;
import tsvetkoff.currencyrates.dto.RateHistoryDto;
import tsvetkoff.currencyrates.service.RateService;

import java.util.List;

@Tag(name = "rate-controller", description = "Контроллер для обработки запросов для курсов валют")
@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    /**
     * Получить текущие значения курсов валют для всех банков.
     *
     * @return Текущие значения курсов валют для всех банков
     */
    @Operation(summary = "Получить текущие значения курсов валют для всех банков")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Текущие значения курсов валют для всех банков",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RateDto.class))
                    )
            )
    })
    @GetMapping("/current")
    public ResponseEntity<List<RateDto>> getCurrentRates() {
        List<RateDto> rates = rateService.getCurrentRates();
        return ResponseEntity.ok(rates);
    }

    /**
     * Получить историю курса валют для указанного банка и валюты.
     *
     * @param bank     Банк
     * @param currency Валюта
     * @return Список курсов валют для указанного банка и валюты
     */
    @Operation(summary = "Получить историю курса валют для указанного банка и валюты")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "История курса валют для указанного банка и валюты",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RateHistoryDto.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<RateHistoryDto>> getRateHistory(@Parameter(description = "Банк", example = "VTB") @RequestParam String bank,
                                                               @Parameter(description = "Валюта", example = "USD") @RequestParam String currency) {
        List<RateHistoryDto> rateHistory = rateService.getRateHistory(bank, currency);
        return ResponseEntity.ok(rateHistory);
    }

    /**
     * Обновить курс валют во всех банках.
     *
     * @return Ответ без тела
     */
    @Operation(summary = "Обновить курс валют во всех банках")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ответ без тела",
                    content = @Content
            )
    })
    @PostMapping("/update")
    public ResponseEntity<?> updateRates() {
        rateService.updateRates();
        return ResponseEntity.ok().build();
    }

}
