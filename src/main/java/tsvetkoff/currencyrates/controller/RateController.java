package tsvetkoff.currencyrates.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tsvetkoff.currencyrates.dto.RateDto;
import tsvetkoff.currencyrates.dto.RateHistoryDto;
import tsvetkoff.currencyrates.service.RateService;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class RateController {

    private final RateService rateService;

    @GetMapping("/current")
    public ResponseEntity<List<RateDto>> getCurrentRates() {
        List<RateDto> rates = rateService.getCurrentRates();
        return ResponseEntity.ok(rates);
    }

    @GetMapping
    public ResponseEntity<List<RateHistoryDto>> getRateHistory(@RequestParam String bank,
                                                               @RequestParam String currency) {
        List<RateHistoryDto> rateHistory = rateService.getRateHistory(bank, currency);
        return ResponseEntity.ok(rateHistory);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRates() {
        rateService.updateRates();
        return ResponseEntity.ok().build();
    }

}
