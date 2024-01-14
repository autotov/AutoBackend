package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.core.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "autotovdriver")
public class Driver {
    private Integer tenant;
    private Integer company;
    @Nullable
    private Date creationDate;
    @Nullable
    private Date lastUpdateDate;

    @Nullable
    private String status = "ok";
    @Nullable
    private String messages;

    private long index;

    //מספר תעודת זהות/דרכון
    @Id
    private String driverId;
    //מספר רישוי
    @Nullable
    private String risuyNumber;
    //מספר רישיון
    @Nullable
    private String licenseNumber;
    //שם פרטי
    @Nullable
    private String name;
    //שם משפחה
    @Nullable
    private String lastName;
    //דרגת רישיון
    @Nullable
    private String licenseLevel;
    //תוקף רישיון
    @Nullable
    private Date licenseExpireDate;
    @Nullable
    private String licenseExpireDateStatus;

    //תאריך הוצאה
    @Nullable
    private Date licenseDate;

    //הצהרת בריאות לשנתיים
    @Nullable
    private Date HealthDeclarationDate;
    @Nullable
    private String HealthDeclarationDateStatus;

    //הדרכה
    @Nullable
    private String training;

    //תאריך הדרכה
    @Nullable
    private Date trainingDate;
    @Nullable
    private String trainingDateStatus;

    //ארץ
    @Nullable
    private String country;

    //עיר
    @Nullable
    private String city;

    //רחוב
    @Nullable
    private String street;

    //מספר בית
    @Nullable
    private String houseNumber;

    //מספר נייד 1
    @Nullable
    private String phoneNumber1;

    //מספר נייד 2
    @Nullable
    private String phoneNumber2;

    //מספר טלפון בבית
    @Nullable
    private String phoneNumberHome;

    //מספר טלפון משרד
    @Nullable
    private String phoneNumberOffice;

    //מצב משפחתי ???

    //תאריך לידה
    @Nullable
    private Date birthDate;

    //תאריך תחילת עבודה
    @Nullable
    private Date startWorkDate;

    //מספר עובד
    @Nullable
    private String workerNumber;

    //דואר אלקטרוני
    @Nullable
    private String email;

    //שם סניף העבודה
    @Nullable
    private String workPlace;

    //מנהל עבודה ישיר
    @Nullable
    private long managerId;

    //חומס
    @Nullable
    private boolean homes;

    //מנוף
    @Nullable
    private boolean crane;

    //עבודה בגובה
    @Nullable
    private boolean workingInHigh;

    //מחלקה
    @Nullable
    private String department;

    //תפקיד
    @Nullable
    private String role;

    //נהגים נלווים
    @Nullable
    private String moreDrivers;

    //נוהל 6
    @Nullable
    private String procedure6;

    //מעקב תאונות

    //צילום רשיון

    //צילום נהג

    //הערות
    @Nullable
    private String comments;

    public Driver(DriverResp driver) {
        this.index = driver.getIndex();
        this.status = driver.getStatus();
        this.messages = driver.getMessages();
        this.birthDate = driver.getBirthDate();
        this.driverId = driver.getDriverId();
        this.risuyNumber = driver.getRisuyNumber();
        this.licenseNumber = driver.getLicenseNumber();
        this.name = driver.getName();
        this.lastName = driver.getLastName();
        this.licenseLevel = driver.getLicenseLevel();
        this.licenseExpireDate = driver.getLicenseExpireDate();
        this.licenseDate = driver.getLicenseDate();
        this.HealthDeclarationDate = driver.getHealthDeclarationDate();
        this.training = driver.getTraining();
        this.trainingDate = driver.getTrainingDate();
        this.country = driver.getCountry();
        this.city = driver.getCity();
        this.street = driver.getStreet();
        this.houseNumber = driver.getHouseNumber();
        //this.phoneNumber1 = driver.;
        //this.phoneNumber2;
        this.phoneNumberHome = driver.getPhoneNumberHome();
        this.phoneNumberOffice = driver.getPhoneNumberOffice();
        this.birthDate = driver.getBirthDate();
        this.startWorkDate = driver.getStartWorkDate();
        this.workerNumber = driver.getWorkerNumber();
        this.email = driver.getEmail();
        this.workPlace = driver.getWorkPlace();
        managerId = driver.getManagerId();
        homes = driver.isHomes();
        crane = driver.isCrane();
        workingInHigh = driver.isWorkingInHigh();
        this.department = driver.getDepartment();
        this.role = driver.getRole();
        this.moreDrivers = driver.getMoreDrivers();
        this.procedure6 = driver.getProcedure6();
        this.comments = driver.getComments();
    }
}

