package co.ambit.ie.researchportal.profileservice.service;

import co.ambit.ie.researchportal.profileservice.v1.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProfileServiceTest {
    @Autowired
    private ProfileService service;

    @Test
    void shouldReturnServiceUnavailable_WhenFallbackIsCalled() {
        Throwable ex = new RuntimeException("Downstream failure");
        String result = this.service.fallback(ex);

        assertEquals("Service Unavailable", result);
    }
}
