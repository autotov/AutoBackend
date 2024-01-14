package com.autotov.service;

import com.autotov.model.Company;
import com.autotov.model.Tenant;
import com.autotov.model.User;

import java.util.Map;
import java.util.Optional;

public interface TenantService {
    Iterable<Tenant> getAllTenant();
    void addTenant(Tenant tenant) throws Exception;
    Optional<Tenant> getTenant(Integer tenant);

    void addCompanyToTenant(Integer tenant, Company company) throws Exception;
    void updateCompanyToTenant(Integer tenant, Company company) throws Exception;

    void deleteTenant(Integer tenant) throws Exception;
    void deleteCompanyInTenant(Integer tenant, int company) throws Exception;
    void markForDeleteCompanyToTenant(Integer tenant, int company) throws Exception;

    Company getCompany(Integer company, Integer tenant) throws Exception;
    Company getCompanyByName(String companyName, Tenant tenant) throws Exception;

    Map<Integer, Company> getAllCompaniesForTenant(Integer tenant) throws Exception;
    Map<Integer, Company> getAllCompanies() throws Exception;

    User getLoginUser();
}
