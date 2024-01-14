package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResp {
    private long index;

    private String status;
    private String messages;

    //מספר תעודת זהות/דרכון
    private String driverId;
    //מספר רישוי
    private String risuyNumber;
    //מספר רישיון
    private String licenseNumber;
    //שם פרטי
    private String name;
    //שם משפחה
    private String lastName;
    //דרגת רישיון
    private String licenseLevel;
    //תוקף רישיון
    private Date licenseExpireDate;
    private boolean licenseExpireDateAlarm;
    private boolean licenseExpireDateWarn;
    //תאריך הוצאה
    private Date licenseDate;
    //הצהרת בריאות לשנתיים
    private Date HealthDeclarationDate;
    private boolean HealthDeclarationDateAlarm;
    private boolean HealthDeclarationDateWarn;
    //הדרכה
    private String training;
    //תאריך הדרכה
    private Date trainingDate;
    private boolean trainingDateAlarm;
    private boolean trainingDateWarn;
    //ארץ
    private String country;
    //עיר
    private String city;
    //רחוב
    private String street;
    //מספר בית
    private String houseNumber;
    //מספר נייד 1
    private String mobile1;
    //מספר נייד 2
    private String mobile2;
    //מספר טלפון בבית
    private String phoneNumberHome;
    //מספר טלפון משרד
    private String phoneNumberOffice;
    //מצב משפחתי ???

    //תאריך לידה
    private Date birthDate;
    //תאריך תחילת עבודה
    private Date startWorkDate;
    //מספר עובד
    private String workerNumber;
    //דואר אלקטרוני
    private String email;
    //שם סניף העבודה
    private String workPlace;
    //מנהל עבודה ישיר
    private long managerId;
    //חומס
    private boolean homes;
    //מנוף
    private boolean crane;
    //עבודה בגובה
    private boolean workingInHigh;
    //מחלקה
    private String department;
    //תפקיד
    private String role;
    //נהגים נלווים
    private String moreDrivers;
    //נוהל 6
    private String procedure6;
    //מעקב תאונות

    //צילום רשיון

    //צילום נהג

    //הערות
    private String comments;
    //רכבים שהיו ברשותו

}

