package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.core.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "autotovcar")
public class Car {

    public Car(CarResp carResp) {
        index = carResp.getIndex();
        license = carResp.getLicense();
        driverId = carResp.getDriverId();
        driverName = carResp.getDriverName();
        phone = carResp.getPhone();
        cellPhone = carResp.getCellPhone();
        kmNextTipul = carResp.getKmNextTipul();
        toolType = carResp.getToolType();
        carType = carResp.getCarType();
        carCreateCountry = carResp.getCarCreateCountry();
        carSubType = carResp.getCarSubType();
        weight = carResp.getWeight();
        carCreateYear = carResp.getCarCreateYear();
        currentKM = carResp.getCurrentKM();
        licenseDate = carResp.getLicenseDate();
        insurance = carResp.getInsurance();
        insuranceHova = carResp.getInsuranceHova();
        insuranceSelishi = carResp.getInsuranceSelishi();
        breaksHalfYear = carResp.getBreaksHalfYear();
        technograph = carResp.getTechnograph();
        engineerApproval = carResp.getEngineerApproval();
        winterReview = carResp.getWinterReview();
        leadLicense = carResp.getLeadLicense();
        labTest = carResp.getLabTest();
        labTestStatus = carResp.getLabTestStatus();
        department = carResp.getDepartment();
        tollRoad = carResp.isTollRoad();
        dalkan = carResp.isDalkan();
        fuelType = carResp.getFuelType();
        startCode = carResp.getStartCode();
        locateCompany = carResp.getLocateCompany();
        comments = carResp.getComments();
        ownership = carResp.getOwnership();
        business = carResp.getBusiness();
        leasingStartDate = carResp.getLeasingStartDate();
        leasingEndDate = carResp.getLeasingEndDate();
    }

    private Integer tenant;
    private Integer company;
    private Date creationDate;
    private Date lastUpdateDate;

    //מספר סידורי
    private long index;

    //מספר רישוי
    @Id
    private String license;

    @Nullable
    private String status = "ok";

    @Nullable
    private String messages;

    @Nullable
    //נהג
    private long driverId;

    @Nullable
    //נהג
    private String driverName;

    @Nullable
    //טלפון
    private String phone;

    @Nullable
    //פלאפון
    private String cellPhone;

    @Nullable
    //קמ טיפול הבא
    private long kmNextTipul;
    private String kmNextTipulStatus;

    @Nullable
    //סוג כלי
    private String toolType;
    @Nullable
    //סוג רכב
    private String carType;

    @Nullable
    //תוצר
    private int carCreateCountry;
    @Nullable
    //דגם
    private String carSubType;
    @Nullable
    // קוד דגם
    private String typeCode;
    @Nullable
    //משקל
    private int weight;
    //שנתון
    private int carCreateYear;
    //קמ עדכני
    private long currentKM;

    //רישיון רכב
    @Nullable
    private Date licenseDate;
    @Nullable
    private String licenseDateStatus;
    //סוג ביטוח
    @Nullable
    private String insurance;
    //ביטוח חובה
    @Nullable
    private Date insuranceHova;
    @Nullable
    private String insuranceHovaStatus;
    //ביטוח צד ג
    @Nullable
    private Date insuranceSelishi;
    @Nullable
    private String insuranceSelishiStatus;
    //בלמים חצי שנתי
    @Nullable
    private Date breaksHalfYear;
    @Nullable
    private String breaksHalfYearStatus;
    //טכוגרף
    @Nullable
    private Date technograph;
    @Nullable
    private String technographStatus;
    //אישור מהנדס
    @Nullable
    private Date engineerApproval;
    @Nullable
    private String engineerApprovalStatus;
    //ביקורת חורף
    @Nullable
    private Date winterReview;
    @Nullable
    private String winterReviewStatus;
    //רישיון מוביל
    @Nullable
    private Date leadLicense;
    @Nullable
    private String leadLicenseStatus;
    //בדיקת מעבדה
    @Nullable
    private Date labTest;
    @Nullable
    private String labTestStatus;
    //מחלקה
    @Nullable
    private String department;
    //מיקום
    @Nullable
    private String location;

    //כבישי אגרה
    private boolean tollRoad;
    //דלקן
    private boolean dalkan;
    //סוג דלק
    @Nullable
    private String fuelType;
    //קודנית
    @Nullable
    private long startCode;
    //איתוראן / פויינטר
    @Nullable
    private String locateCompany;
    //הערות
    @Nullable
    private String comments;
    //ליסינג/בעלות
    @Nullable
    private String ownership;
    //סוג עסקה
    @Nullable
    private String business;
    //תחילת ליסינג
    @Nullable
    private Date leasingStartDate;
    //תום ליסינג
    @Nullable
    private Date leasingEndDate;
    @Nullable
    private String leasingEndDateStatus;
}
