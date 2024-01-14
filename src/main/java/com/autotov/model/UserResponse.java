package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    public UserResponse(User user) {
        tenant = user.getTenant();
        username = user.getUsername();
        name = user.getName();
        lastName = user.getLastName();
        phone = user.getPhone();
        mobilePhone = user.getMobilePhone();
        role = user.getRole();
        companies = user.getCompanies();
        rights = user.getRights();
    }

    private Integer tenant;

    private String username;
    private String name;
    private String lastName;
    private String phone;
    private String mobilePhone;
    private String role;

    private List<Integer> companies;
    private CompanyItem[] companiesNames;

    private Integer currentCompany;

    private String rights;
}
