package com.autotov.service;

import com.autotov.model.Alerts;
import com.autotov.repo.AlertsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertsServiceImpl implements AlertsService {
    @Autowired
    private AlertsRepository alertsRepository;

    @Override
    public Iterable<Alerts> getAlertsList() {
        return alertsRepository.findAll();
    }

    @Override
    public void deleteAllAlerts() {
        alertsRepository.deleteAll();
    }

    @Override
    public Iterable<Alerts> getAlertsList(String tenant, String company) {
        return alertsRepository.findByTenantAndCompany(tenant, company);
    }

    @Override
    public void addAlerts(Alerts alerts) {
        alertsRepository.save(alerts);
    }
}

