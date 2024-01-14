package com.autotov.controller;

import com.autotov.model.Alert;
import com.autotov.model.Alerts;
import com.autotov.service.AlertsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AlertsController {

    @Autowired
    private AlertsService alertsService;

    @Operation(summary="Get Alerts" , description="Get Alerts." ,
            responses = { @ApiResponse( responseCode = "200", description = ""  ,
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}) })
    @GetMapping("/alerts")
    public List<Alerts> getAlertsList() {
        Iterable<Alerts> alerts = alertsService.getAlertsList();
        List<Alerts> newAlertsList = new ArrayList<>();
        alerts.forEach(alert -> {
            newAlertsList.add(alert);
            System.out.println(alert);
        });
        return newAlertsList;
    }

    @Operation(summary="Add Alerts" , description="Add Alerts." ,
            responses = { @ApiResponse( responseCode = "200", description = ""  ,
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}) })
    @PostMapping("/alerts/add")
    public void addAlerts(@Parameter(description="Tenant") @RequestParam String tenant,
                          @Parameter(description="Company") @RequestParam String company,
                          @Parameter(description="Index") @RequestParam Long index,
                          @Parameter(description="ID") @RequestParam String id){
        Alerts alerts = new Alerts();
        alerts.setTenant(tenant);
        alerts.setCompany(company);
        alerts.setId(id);
        alerts.setIndex(index);
        List<Alert> alertList = new ArrayList<>();
        Alert alert = new Alert("ABC", "OK");
        alertList.add(alert);
        alert = new Alert("ABC_2", "ALARM");
        alertList.add(alert);
        alertsService.addAlerts(alerts);
    }

    @Operation(summary="Update Alerts" , description="Update Alerts." ,
            responses = { @ApiResponse( responseCode = "200", description = ""  ,
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}) })
    @PostMapping("/alerts/update")
    public void addAlertsObject(@Parameter(description="Tenant") @RequestParam String tenant,
                                @Parameter(description="Company") @RequestParam String company,
                                @Parameter(description="Alerts") @RequestBody Alerts alerts){
        alertsService.addAlerts(alerts);
    }

    @Operation(summary="Delete All Alerts" , description="Delete All Alerts." ,
            responses = { @ApiResponse( responseCode = "200", description = ""  ,
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}) })
    @DeleteMapping("/deleteAllAlerts")
    public void deleteAllAlerts(){
        alertsService.deleteAllAlerts();
    }
}
