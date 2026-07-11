package co.ambit.ie.researchportal.profileservice.controller;

import co.ambit.ie.researchportal.profileservice.feignclient.ReportFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/profile")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
    private final ReportFeignClient reportClient;

    @GetMapping(path = "/ping")
    public ResponseEntity<?> getPing() {
        String reportPingResponse = this.reportClient
                .getPingResponse()
                .getBody();

        return ResponseEntity.ok("Profile Ok. " + reportPingResponse);
    }

    @GetMapping(path = "/whoami")
    public ResponseEntity<?> getWhoAmI(@AuthenticationPrincipal Object object) {
        log.info("auth: {}", object);

        return ResponseEntity.ok(object);
    }
}
