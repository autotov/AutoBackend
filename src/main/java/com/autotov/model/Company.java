package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.core.Nullable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company extends GeneralModel {
    private Integer tenant;
    private String name;
    private Long companyId;
    @Nullable
    private Date licenseDate;
    private String contact;
    private String contactMail;
    private String contactPhone;
    private String manager;
    private String managerMail;
    private String managerPhone;
    private boolean markForDelete = false;
}
