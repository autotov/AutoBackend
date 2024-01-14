package com.autotov.service;

import com.autotov.model.Alerts;

public interface AlertsService {
    public Iterable<Alerts> getAlertsList();

    void deleteAllAlerts();

    Iterable<Alerts> getAlertsList(String tenant, String company);

    void addAlerts(Alerts Alerts);
}
