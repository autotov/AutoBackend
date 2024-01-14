package com.autotov.service;

import com.autotov.app.Constants;
import com.autotov.model.Driver;
import com.autotov.model.User;
import com.autotov.policy.DriverPolicy;
import com.autotov.repo.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TenantService tenantService;

    @Override
    public void addDriver(Driver driver) throws Exception {
        Optional<Driver> driverInRepo = driverRepository.findById(driver.getDriverId());
        if(driverInRepo.isPresent()) {
            throw new Exception("נהג קיים.");
        }

        checkDriverRules(driver);

        User user =  tenantService.getLoginUser();
        driver.setTenant(user.getTenant());
        driver.setCompany(user.getCurrentCompany());

        try {
            driverRepository.save(driver);
        } catch (Exception e) {
            // TODO: NullPointerException
            e.printStackTrace();
        }
    }

    @Override
    public void updateDriver(Driver driver) throws Exception {
        Optional<Driver> driverInRepo = driverRepository.findById(driver.getDriverId());
        if(!driverInRepo.isPresent()) {
            throw new Exception("נהג לא רשום במערכת");
        }
        if(driverInRepo.get().getTenant() != driver.getTenant()
                || driverInRepo.get().getCompany() != driver.getCompany()) {
            throw new Exception("נהג לא רשום במערכת");
        }

        checkDriverRules(driver);

        try {
            driverRepository.save(driver);
        } catch (Exception e) {
            // TODO: NullPointerException
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<Driver> getDriverList(Integer tenant, Integer company) {
        return driverRepository.findAllByTenantAndCompany(tenant, company);
    }

    @Override
    public void deleteDriver(String driverId, Integer tenant, Integer company) {
        driverRepository.deleteByDriverIdAndTenantAndCompany(driverId, tenant, company);
    }

    @Override
    public void deleteAllDrivers() {
        driverRepository.deleteAll();
    }

    public void checkDriverRules(Driver driver) {
        DriverPolicy driverPolicy = new DriverPolicy();
        long diffInMS;
        long diff;
        StringBuilder warnMessage = new StringBuilder();
        StringBuilder alarmMessage = new StringBuilder();

        if (driver.getLicenseExpireDate() != null) {
            diffInMS = Math.abs((new Date()).getTime() - driver.getLicenseExpireDate().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < driverPolicy.getLicenseExpireDateAlarm()) {
                driver.setLicenseExpireDateStatus(Constants.ALARM_STATUS);
                alarmMessage.append("  מועד רשיון מסתיים בקרוב מאד.\n");
            } else if (diff < driverPolicy.getLicenseExpireDateWarn()) {
                driver.setLicenseExpireDateStatus(Constants.WARN_STATUS);
                warnMessage.append("מועד רשיון מסתיים בקרוב.\n");
            } else {
                driver.setLicenseExpireDateStatus(Constants.OK_STATUS);
            }
        } else {
            driver.setLicenseExpireDateStatus(Constants.OK_STATUS);
        }

        if (driver.getHealthDeclarationDate() != null) {
            diffInMS = Math.abs((new Date()).getTime() - driver.getHealthDeclarationDate().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < driverPolicy.getHealthDeclarationDateAlarm()) {
                driver.setHealthDeclarationDateStatus(Constants.ALARM_STATUS);
                alarmMessage.append("  מועד הצהרת בריאות לשנתיים מסתיים בקרוב מאד.\n");
            } else if (diff < driverPolicy.getHealthDeclarationDateWarn()) {
                driver.setHealthDeclarationDateStatus(Constants.WARN_STATUS);
                warnMessage.append("  מועד הצהרת בריאות לשנתיים מסתיים בקרוב מאד.\n");
            } else {
                driver.setHealthDeclarationDateStatus(Constants.OK_STATUS);
            }
        } else {
            driver.setHealthDeclarationDateStatus(Constants.OK_STATUS);
        }

        if (driver.getTrainingDate() != null && "true".equalsIgnoreCase(driver.getTraining())) {
            diffInMS = Math.abs((new Date()).getTime() - driver.getTrainingDate().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < driverPolicy.getTrainingDateAlarm()) {
                driver.setTrainingDateStatus(Constants.ALARM_STATUS);
                alarmMessage.append("  מועד הדרכה מסתיים בקרוב מאד.\n");
            } else if (diff < driverPolicy.getTrainingDateWarn()) {
                driver.setTrainingDateStatus(Constants.WARN_STATUS);
                warnMessage.append("  מועד הדרכה מסתיים בקרוב מאד.\n");
            } else {
                driver.setTrainingDateStatus(Constants.OK_STATUS);
            }
        } else {
            driver.setTrainingDateStatus(Constants.OK_STATUS);
        }

        if(alarmMessage.length() > 0){
            driver.setStatus(Constants.ALARM_STATUS);
            if(warnMessage.length() > 0){
                driver.setMessages(alarmMessage + "\n" + warnMessage);
            } else {
                driver.setMessages(alarmMessage.toString());
            }
        } else if(warnMessage.length() > 0) {
            driver.setStatus(Constants.WARN_STATUS);
            driver.setMessages(warnMessage.toString());

        } else {
            driver.setStatus(Constants.OK_STATUS);
            driver.setMessages("רשומת נהג תקינה");
        }
    }
}
