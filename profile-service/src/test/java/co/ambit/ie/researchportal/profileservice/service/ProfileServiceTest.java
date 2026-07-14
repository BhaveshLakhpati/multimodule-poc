package co.ambit.ie.researchportal.profileservice.service;

import co.ambit.ie.researchportal.profileservice.v1.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProfileServiceTest {
    @MockitoBean
    private ProfileService service;

    @Test
    void shouldReturnServiceUnavailable_WhenFallbackIsCalled() {
        Throwable ex = new RuntimeException("Downstream failure");
        String result = this.service.fallback(ex);

        assertEquals("Service Unavailable", result);
    }
}
