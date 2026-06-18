package com.trucknest.backend.invoices;

import com.trucknest.backend.invoices.dto.InvoiceResponse;
import com.trucknest.backend.invoices.internal.InvoiceService;
import com.trucknest.backend.invoices.internal.InvoiceStatus;
import com.trucknest.backend.clients.ClientQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
@DisplayName("InvoiceController Web Layer Tests")
@Import(InvoiceControllerTest.TestConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://dummy-auth/protocol/openid-connect/certs",
        "keycloak.admin.client-secret=dummy",
        "brevo.from-email=test@test.com"
})
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private InvoiceService invoiceService;

    @MockitoBean
    private ClientQueryService clientQueryService;

    private static final UUID COMPANY_ID = UUID.randomUUID();

    @TestConfiguration
    static class TestConfig {
        @Bean("auditorAwareImpl")
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("test-user");
        }
    }

    private static org.springframework.security.test.web.servlet.request
            .SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt() {
        return org.springframework.security.test.web.servlet.request
                .SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(token -> token
                        .claim("company_id", COMPANY_ID.toString())
                        .claim("sub", "test-user"));
    }

    @Test
    @DisplayName("GET /api/v1/invoices returns 200 with invoice list")
    void shouldReturnAllInvoices() throws Exception {
        InvoiceResponse response = new InvoiceResponse(
                UUID.randomUUID(), COMPANY_ID, null,
                "INV-2026-001", LocalDate.now(), LocalDate.now().plusDays(30),
                new BigDecimal("1500.00"), InvoiceStatus.PENDING,
                null, null, null, null,
                LocalDateTime.now(), LocalDateTime.now()
        );
        when(invoiceService.getAllInvoices(null)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/invoices")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].invoiceNumber").value("INV-2026-001"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /api/v1/invoices without token returns 401")
    void shouldReturn401WhenNoToken() throws Exception {
        mockMvc.perform(get("/api/v1/invoices"))
                .andExpect(status().isUnauthorized());
    }
}