package com.autotov.policy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "autotovcarpolicy")
public class CarPolicy {
    private String tenant;
    private String company;
    private Date creationDate;
    private Date lastUpdateDate;

    //מספר סידורי
    @Id
    private long index;

    //קמ טיפול הבא
    private long kmNextTipulAlarm = 500;
    private long kmNextTipulWarn  = 1000;

    //רישיון רכב
    private int licenseDateAlarm = 15;
    private int licenseDateWarn  = 30;

    //ביטוח חובה
    private int insuranceHovaAlarm = 15;
    private int insuranceHovaWarn = 30;

    //ביטוח צד ג
    private int insuranceSelishiAlarm = 15;
    private int insuranceSelishiWarn = 30;

    //בלמים חצי שנתי
    private int breaksHalfYearAlarm = 15;
    private int breaksHalfYearWarn = 30;

    //טכוגרף
    private int technographAlarm = 15;
    private int technographWarn = 30;

    //אישור מהנדס
    private int engineerApprovalAlarm = 15;
    private int engineerApprovalWarn = 30;

    //ביקורת חורף
    private int winterReviewAlarm = 15;
    private int winterReviewWarn = 30;

    //רישיון מוביל
    private int leadLicenseAlarm = 15;
    private int leadLicenseWarn = 30;

    //בדיקת מעבדה
    private int labTestAlarm = 15;
    private int labTestWarn = 30;

    //תום ליסינג
    private int leasingEndDateAlarm = 15;
    private int leasingEndDateWarn = 30;

    private Date lastUpdateTimestamp;


}
