package co.ambit.ie.researchportal.profileservice.v1.service;

import co.ambit.ie.researchportal.profileservice.feignclient.ReportFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {
    private final ReportFeignClient reportClient;

    @CircuitBreaker(name = "reportService", fallbackMethod = "fallback")
    public String getReportPing() {
        return this.reportClient
                .getPingResponse()
                .getBody();
    }

    public String fallback(final Throwable ex) {
        return "Service Unavailable";
    }
}
