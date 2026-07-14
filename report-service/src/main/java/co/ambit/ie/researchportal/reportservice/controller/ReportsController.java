package co.ambit.ie.researchportal.reportservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/reports")
public class ReportsController {
    @GetMapping(path = "/ping")
    public ResponseEntity<String> getPing() {
        return ResponseEntity.ok("Reports Ok");
    }
}
