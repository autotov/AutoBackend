package com.autotov.controller;

import com.autotov.app.Constants;
import com.autotov.model.Company;
import com.autotov.model.Tenant;
import com.autotov.model.User;
import com.autotov.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("/tenant/all")
    public ResponseEntity<Object> getAllTenant() {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        Iterable<Tenant> tenantItem = tenantService.getAllTenant();
        List<Tenant> newTenantList = new ArrayList<>();
        tenantItem.forEach(tenant -> {
            newTenantList.add(tenant);
        });


        return new ResponseEntity<>(newTenantList, HttpStatus.OK);
    }

    @GetMapping("/tenant")
    public ResponseEntity<Object> getTenantObject() {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        Optional<Tenant> tenantItem = tenantService.getTenant(tenantService.getLoginUser().getTenant());
        if(!tenantItem.isPresent()) {
            return new ResponseEntity<>("תאגיד לא קיים.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(tenantItem.get(), HttpStatus.OK);
    }

    @PostMapping("/tenant/add")
    public ResponseEntity<Object> addTenant(@RequestBody Tenant tenant){
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            tenantService.addTenant(tenant);
            return new ResponseEntity<>("תאגיד נוסף בהצלחה.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/tenant/{tenant}/delete")
    public ResponseEntity<Object> deleteTenant(@PathVariable("tenant") Integer tenant) {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            tenantService.deleteTenant(tenant);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("חברה נמחקה בהצלחה", HttpStatus.OK);
    }

    @GetMapping("/tenant/companies")
    public ResponseEntity<Object> getAllCompaniesInTenant() {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN) && !user.getRole().equals(Constants.ADMIN_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            Map<Integer, Company> companies = tenantService.getAllCompaniesForTenant(user.getTenant());
            return new ResponseEntity<>(companies.values(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tenant/company/add")
    public ResponseEntity<Object> addCompany(@RequestBody Company company) {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN) && !user.getRole().equals(Constants.ADMIN_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        company.setTenant(user.getTenant());
        try {
            tenantService.addCompanyToTenant(user.getTenant(), company);
            return new ResponseEntity<>("חברה נוספה בהצלחה", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tenant/company/update")
    public ResponseEntity<Object> updateCompany(@RequestBody Company company) {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN) && !user.getRole().equals(Constants.ADMIN_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        company.setTenant(user.getTenant());
        try {
            tenantService.updateCompanyToTenant(user.getTenant(), company);
            return new ResponseEntity<>("חברה עודכנה בהצלחה", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/tenant/company/{company}/delete")
    public ResponseEntity<Object>  deleteCompany(@PathVariable("company") int company) {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN) && !user.getRole().equals(Constants.ADMIN_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            tenantService.deleteCompanyInTenant(user.getTenant(), company);
            return new ResponseEntity<>("חברה נמחקה בהצלחה", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
