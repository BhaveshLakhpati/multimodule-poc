package co.ambit.ie.researchportal.profileservice.controller;

import co.ambit.ie.researchportal.profileservice.feignclient.ReportFeignClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {
    @MockitoBean
    private ReportFeignClient reportFeignClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnProfileOkWhenPinged() throws Exception {
        this.mockMvc.perform(get("/v1/profile/ping")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile Ok."));
    }

    @Test
    void shouldReturnReportOkWhenReportPinged() throws Exception {
        // 1. Arrange: Stub the external client to return a mock ResponseEntity
        String mockReportResponse = "Report Service Ok.";
        Mockito.when(this.reportFeignClient.getPingResponse())
                .thenReturn(ResponseEntity.ok(mockReportResponse));

        this.mockMvc.perform(get("/v1/profile/ping-report")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile Ok. " + mockReportResponse));
    }

    @Test
    void shouldReturnDummyWhenGetWhoAmI() throws Exception {
        this.mockMvc.perform(get("/v1/profile/whoami")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("dummy"));
    }
}
