package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "autotovusers")
public class User extends GeneralModel {

    private Integer tenant;

    private String username;
    private String name;
    private String lastName;
    private String password;
    private String phone;
    private String mobilePhone;
    private String role;

    private List<Integer> companies;

    private Integer currentCompany;

    private String rights;

    private long lastAccess;

}
