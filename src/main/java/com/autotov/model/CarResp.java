package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarResp {
    private Date creationDate;
    private Date lastUpdateDate;

    //מספר סידורי
    private long index;

    //מספר רישוי
    private String license;

    private String status;

    private String messages;

    //נהג
    private long driverId;

    //נהג
    private String driverName;

    //טלפון
    private String phone;

    //פלאפון
    private String cellPhone;

    //קמ טיפול הבא
    private long kmNextTipul;
    private String kmNextTipulStatus;

    //סוג כלי
    private String toolType;
    //סוג רכב
    private String carType;
    //תוצר
    private int carCreateCountry;
    //דגם
    private String carSubType;
    //משקל
    private int weight;
    //שנתון
    private int carCreateYear;
    //קמ עדכני
    private long currentKM;
    //רישיון רכב
    private Date licenseDate;
    private String licenseDateStatus;
    //ביטוח
    private String insurance;
    //ביטוח חובה
    private Date insuranceHova;
    private String insuranceHovaStatus;
    //ביטוח צד ג
    private Date insuranceSelishi;
    private String insuranceSelishiStatus;
    //בלמים חצי שנתי
    private Date breaksHalfYear;
    private String breaksHalfYearStatus;
    //טכוגרף
    private Date technograph;
    private String technographStatus;
    //אישור מהנדס
    private Date engineerApproval;
    private String engineerApprovalStatus;
    //ביקורת חורף
    private Date winterReview;
    private String winterReviewStatus;
    //רישיון מוביל
    private Date leadLicense;
    private String leadLicenseStatus;
    //בדיקת מעבדה
    private Date labTest;
    private String labTestStatus;
    //מחלקה
    private String department;
    //כבישי אגרה
    private boolean tollRoad;
    //דלקן
    private boolean dalkan;
    //סוג דלק
    private String fuelType;
    //קודנית
    private long startCode;
    //איתוראן / פויינטר
    private String locateCompany;
    //הערות
    private String comments;
    //ליסינג/בעלות
    private String ownership;
    //סוג עסקה
    private String business;
    //תחילת ליסינג
    private Date leasingStartDate;
    //תום ליסינג
    private Date leasingEndDate;
    private String leasingEndDateStatus;

    public CarResp(Car car) {
        this.creationDate = car.getCreationDate();
        this.lastUpdateDate = car.getLastUpdateDate();
        this.index = car.getIndex();
        this.license = car.getLicense();
        this.status = car.getStatus();
        this.messages = car.getMessages();
        this.driverId = car.getDriverId();
        this.driverName = car.getDriverName();
        this.phone = car.getPhone();
        this.cellPhone = car.getCellPhone();
        this.kmNextTipul = car.getKmNextTipul();
        this.kmNextTipulStatus = car.getKmNextTipulStatus();
        this.toolType = car.getToolType();
        this.carType = car.getCarType();
        this.carCreateCountry = car.getCarCreateCountry();
        this.carSubType = car.getCarSubType();
        this.weight = car.getWeight();
        this.carCreateYear = car.getCarCreateYear();
        this.currentKM = car.getCurrentKM();
        this.licenseDate = car.getLicenseDate();
        this.licenseDateStatus = car.getLicenseDateStatus();
        this.insurance = car.getInsurance();
        this.insuranceHova = car.getInsuranceHova();
        this.insuranceHovaStatus = car.getInsuranceHovaStatus();
        this.insuranceSelishi = car.getInsuranceSelishi();
        this.insuranceSelishiStatus = car.getInsuranceSelishiStatus();
        this.breaksHalfYear = car.getBreaksHalfYear();
        this.breaksHalfYearStatus = car.getBreaksHalfYearStatus();
        this.technograph = car.getTechnograph();
        this.technographStatus = car.getTechnographStatus();
        this.engineerApproval = car.getEngineerApproval();
        this.engineerApprovalStatus = car.getEngineerApprovalStatus();
        this.winterReview = car.getWinterReview();
        this.winterReviewStatus = car.getWinterReviewStatus();
        this.leadLicense = car.getLeadLicense();
        this.leadLicenseStatus = car.getLeadLicenseStatus();
        this.labTest = car.getLabTest();
        this.labTestStatus = car.getLabTestStatus();
        this.department = car.getDepartment();
        this.tollRoad = car.isTollRoad();
        this.dalkan = car.isDalkan();
        this.fuelType = car.getFuelType();
        this.startCode = car.getStartCode();
        this.locateCompany = car.getLocateCompany();
        this.comments = car.getComments();
        this.ownership = car.getOwnership();
        this.business = car.getBusiness();
        this.leasingStartDate = car.getLeasingStartDate();
        this.leasingEndDate = car.getLeasingEndDate();
        this.leasingEndDateStatus = car.getLeasingEndDateStatus();
    }
}
