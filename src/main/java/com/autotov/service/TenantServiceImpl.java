package com.autotov.service;

import com.autotov.model.Company;
import com.autotov.model.Tenant;
import com.autotov.model.User;
import com.autotov.repo.TenantRepository;
import com.autotov.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    public User getLoginUser() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public Iterable<Tenant> getAllTenant() {
        return tenantRepository.findAll();
    }

    @Override
    public Optional<Tenant> getTenant(Integer tenantId) {
        return tenantRepository.findById(tenantId);
    }

    private void setNumberIdForTenant(Tenant tenant, Iterable<Tenant> tenantList) {
        Iterator<Tenant> iterator = tenantList.iterator();
        int max = 1;
        while(iterator.hasNext()){
            int tenantNum = iterator.next().getNumberId();
            if(tenantNum >= max) {
                max = tenantNum + 1;
            }
        }
        tenant.setNumberId(max);
    }

    @Override
    public void addTenant(Tenant tenant) throws Exception {
        Optional<Tenant> tenantItem = getTenant(tenant.getNumberId());
        if(tenantItem.isPresent()) {
            throw new Exception("תאגיד קיים");
        }

        tenant.setCreationDate(new Date());
        tenant.setLastUpdateDate(tenant.getCreationDate());

        Iterable<Tenant> tenantList = tenantRepository.findAll();
        setNumberIdForTenant(tenant, tenantList);

        try {
            tenantRepository.save(tenant);

        } catch (Exception e) {
            Optional<Tenant> tmp = getTenant(tenant.getNumberId());
            if(!tmp.isPresent()) {
                throw e;
            }
        }
    }

    @Override
    public void deleteTenant(Integer tenant) throws Exception {
        Optional<Tenant> tenantItem = tenantRepository.findById(tenant);
        if(!tenantItem.isPresent()) {
            throw new Exception("התאגיד לא קיים במערכת.");
        }

        try {
        tenantRepository.deleteById(tenant);

        } catch (Exception e) {
            tenantItem = tenantRepository.findById(tenant);
            if(tenantItem.isPresent()) {
                throw e;
            }
        }
    }

    private void setNumberIdForCompany(Company company,  Map<Integer, Company> companyList) {
        int max = 1;
        for (Company companyItem: companyList.values()) {
            if(companyItem.getNumberId() >= max) {
                max = companyItem.getNumberId() + 1;
            }
        }
        company.setNumberId(max);
    }

    @Override
    public void addCompanyToTenant(Integer tenant, Company company) throws Exception {
        Optional<Tenant> tenantItem = getTenant(tenant);
        if(!tenantItem.isPresent()) {
            throw new Exception("תאגיד לא קיים");
        }

        Tenant tenantObject = tenantItem.get();
        Company existsCompany = getCompanyByName(company.getName(), tenantObject);
        if(existsCompany != null) {
            throw new Exception("חברה קיימת");
        }

        company.setCreationDate(new Date());
        company.setLastUpdateDate(tenantObject.getCreationDate());

        Map<Integer, Company> companyList = getAllCompaniesForTenant(tenant);
        setNumberIdForCompany(company, companyList);

        tenantObject.getCompanies().put(company.getNumberId(), company);
        try {
            tenantRepository.save(tenantObject);

        } catch (Exception e) {
            existsCompany = getCompany(company.getNumberId(), tenantObject.getNumberId());
            if(existsCompany == null) {
                throw e;
            }
        }
    }

    @Override
    public void updateCompanyToTenant(Integer tenant, Company company) throws Exception {
        Optional<Tenant> tenantItem = getTenant(tenant);
        if(!tenantItem.isPresent()) {
            throw new Exception("תאגיד לא קיים");
        }

        Tenant tenantObject = tenantItem.get();
        Company existsCompany = tenantObject.getCompanies().get(company.getNumberId());
        if(existsCompany == null) {
            throw new Exception("חברה לא קיימת");
        }

        company.setTenant(tenant);
        company.setNumberId(existsCompany.getNumberId());
        company.setCreationDate(existsCompany.getCreationDate());
        Date updateDate = new Date();
        company.setLastUpdateDate(updateDate);

        tenantObject.getCompanies().put(company.getNumberId(), company);
        try {
            tenantRepository.save(tenantObject);

        } catch (Exception e) {
            existsCompany = getCompany(company.getNumberId(), tenantObject.getNumberId());
            if(existsCompany == null) {
                throw e;
            }
            if(existsCompany.getLastUpdateDate().compareTo(updateDate) != 0) {
                throw e;
            }
        }
    }

    @Override
    public void deleteCompanyInTenant(Integer tenant, int company) throws Exception {
        deleteOrMarkForDeleteCompanyToTenant(tenant, company, true);
    }

    @Override
    public void markForDeleteCompanyToTenant(Integer tenant, int company) throws Exception {
        deleteOrMarkForDeleteCompanyToTenant(tenant, company, false);
    }

    private void deleteOrMarkForDeleteCompanyToTenant(Integer tenant, int company, boolean delete) throws Exception {
        Optional<Tenant> tenantItem = getTenant(tenant);
        if(tenantItem == null) {
            throw new Exception("תאגיד לא קיים");
        }

        Tenant tenantObject = tenantItem.get();
        Company companyToDelete = tenantObject.getCompanies().get(company);
        if(companyToDelete == null) {
            throw new Exception("חברה לא קיימת");
        }

        if(!delete) {
            companyToDelete.setMarkForDelete(true);
            companyToDelete.setLastUpdateDate(new Date());
        } else {
            tenantObject.getCompanies().remove(companyToDelete.getNumberId());
            tenantObject.setLastUpdateDate(new Date());
        }

        try {
            tenantRepository.save(tenantObject);
        } catch (Exception e){
            Company existsCompany = getCompany(company, tenant);
            if(delete) {
                if(existsCompany != null) {
                    throw e;
                }
            } else {
                if(!existsCompany.isMarkForDelete()){
                    throw e;
                }
            }
        }
    }

    @Override
    public Company getCompanyByName(String companyName, Tenant tenant) {
        for (Company companyItem: tenant.getCompanies().values()) {
            if(companyItem.getName().equalsIgnoreCase(companyName)) {
                return companyItem;
            }
        }

        return null;
    }

    @Override
    public Company getCompany(Integer companyId, Integer tenant) throws Exception {
        Optional<Tenant> tenantItem = getTenant(tenant);
        if(!tenantItem.isPresent()) {
            throw new Exception("תאגיד לא קיים");
        }

        return tenantItem.get().getCompanies().get(companyId);
    }

    @Override
    public Map<Integer, Company> getAllCompaniesForTenant(Integer tenant) throws Exception {
        Optional<Tenant> tenantItem = getTenant(tenant);
        if(!tenantItem.isPresent()) {
            throw new Exception("תאגיד לא קיים");
        }

        return tenantItem.get().getCompanies();
    }

    @Override
    public Map<Integer, Company> getAllCompanies() throws Exception {
        Iterable<Tenant> allTenants = getAllTenant();
        Map<Integer, Company> allCompanies = new HashMap<>();
        for (Tenant tenant: allTenants) {
            Map<Integer, Company> companies = tenant.getCompanies();
            allCompanies.putAll(companies);
        }

        return allCompanies;
    }
}
