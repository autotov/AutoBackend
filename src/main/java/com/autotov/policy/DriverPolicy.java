package com.autotov.policy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "autotovdriver")
public class DriverPolicy {
    private String tenant;
    private String company;
    private Date creationDate;
    private Date lastUpdateDate;

    @Id
    private long index;

    //תוקף רישיון
    private int licenseExpireDateAlarm = 15;
    private int licenseExpireDateWarn  = 30;

    //הצהרת בריאות לשנתיים
    private int HealthDeclarationDateAlarm = 15;
    private int HealthDeclarationDateWarn  = 30;

    //תאריך הדרכה
    private int trainingDateAlarm = 15;
    private int trainingDateWarn  = 30;

}

