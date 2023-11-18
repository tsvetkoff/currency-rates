package tsvetkoff.currencyrates.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tsvetkoff.currencyrates.config.TestSecurityConfiguration;
import tsvetkoff.currencyrates.dto.RateDto;
import tsvetkoff.currencyrates.dto.RateHistoryDto;
import tsvetkoff.currencyrates.service.RateService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RateController.class)
@Import(TestSecurityConfiguration.class)
class RateControllerTests {

    private static final TypeReference<List<RateDto>> LIST_RATE_DTO = new TypeReference<>() {
    };

    private static final TypeReference<List<RateHistoryDto>> LIST_RATE_HISTORY_DTO = new TypeReference<>() {
    };

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RateService rateService;

    @Value("classpath:get-current-rates-response.json")
    private Resource getCurrentResource;

    @Value("classpath:get-rate-history-response.json")
    private Resource getRateHistoryResource;

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetCurrentRates() throws Exception {
        // given
        String responseAsString = getCurrentResource.getContentAsString(StandardCharsets.UTF_8);
        List<RateDto> rates = objectMapper.readValue(responseAsString, LIST_RATE_DTO);

        String expected = objectMapper.writeValueAsString(rates);

        // when
        when(rateService.getCurrentRates())
                .thenReturn(rates);

        // then
        MvcResult mvcResult = mockMvc.perform(get("/api/rates/current"))
                .andExpect(status().isOk())
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals(expected, actual);
    }

    @Test
    @WithAnonymousUser
    void testGetCurrentRatesUnauthorized() throws Exception {
        mockMvc.perform(get("/api/rates/current"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetRateHistory() throws Exception {
        // given
        String bank = "CBR";
        String currency = "USD";
        String responseAsString = getRateHistoryResource.getContentAsString(StandardCharsets.UTF_8);

        List<RateHistoryDto> rateHistory = objectMapper.readValue(responseAsString, LIST_RATE_HISTORY_DTO);
        String expected = objectMapper.writeValueAsString(rateHistory);

        // when
        when(rateService.getRateHistory(bank, currency))
                .thenReturn(rateHistory);

        // then
        MvcResult mvcResult = mockMvc.perform(get("/api/rates?bank={bank}&currency={currency}", bank, currency))
                .andExpect(status().isOk())
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals(expected, actual);
    }

    @Test
    @WithAnonymousUser
    void testGetRateHistoryUnauthorized() throws Exception {
        // given
        String bank = "CBR";
        String currency = "USD";

        // then
        mockMvc.perform(get("/api/rates?bank={bank}&currency={currency}", bank, currency))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateRates() throws Exception {
        mockMvc.perform(post("/api/rates/update"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testUpdateRatesUnauthorized() throws Exception {
        mockMvc.perform(post("/api/rates/update"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testUpdateRatesForbidden() throws Exception {
        mockMvc.perform(post("/api/rates/update"))
                .andExpect(status().isForbidden());
    }

}
