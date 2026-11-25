package auca.ac.clinicsystem.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.clinicsystem.service.DashboardService;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping(value = "/summary")
    public ResponseEntity<?> getSummary(){
        Map<String, Long> summary = dashboardService.getSummary();
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}