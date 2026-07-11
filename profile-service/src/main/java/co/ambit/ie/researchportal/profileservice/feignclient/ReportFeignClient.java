package co.ambit.ie.researchportal.profileservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "report-service", url = "${service.routes.reports-service}", path = "/api")
public interface ReportFeignClient {
    @GetMapping(path = "/v1/reports/ping")
    ResponseEntity<String> getPingResponse();
}
