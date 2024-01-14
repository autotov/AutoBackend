package com.autotov.service;

import com.autotov.app.Constants;
import com.autotov.model.Car;
import com.autotov.model.CarDocument;
import com.autotov.model.User;
import com.autotov.policy.CarPolicy;
import com.autotov.repo.CarDocsRepository;
import com.autotov.repo.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarDocsRepository carDocsRepository;

    @Autowired
    private TenantService tenantService;

    @Override
    public Iterable<Car> getCarList() {
       return carRepository.findAll();
    }

    @Override
    public void deleteAllCars() {
        carRepository.deleteAll();
    }

    @Override
    public Iterable<Car> getCarList(Integer tenant, Integer company) {
        return carRepository.findByTenantAndCompany(tenant, company);
    }

    @Override
    public Optional<Car> getCar(String license, Integer tenant, Integer company) throws Exception {
        Optional<Car> existsCar = carRepository.findByLicense(license);
        if(!existsCar.isPresent()) {
            throw new Exception("רכב לא קיים");
        }
        Car tempCar = existsCar.get();
        if(tempCar.getTenant() != tenant || tempCar.getCompany() != company) {
            throw new Exception("רכב לא קיים");
        }

        return carRepository.findByLicense(license);
    }

    @Override
    public void addCar(Car car) throws Exception {
        Optional<Car> existsCars = carRepository.findByLicense(car.getLicense());
        User user = tenantService.getLoginUser();
        if(existsCars.isPresent()) {
            if(existsCars.get().getTenant() != user.getTenant() || existsCars.get().getCompany() != user.getCurrentCompany()) {
                throw new Exception("רכב קיים במערכת בחברה אחרת");
            }
            throw new Exception("רכב קיים במערכת");
        }

        car.setTenant(user.getTenant());
        car.setCompany(user.getCurrentCompany());

        checkCarRules(car);
        car.setCreationDate(car.getLastUpdateDate());
        car.setLastUpdateDate(new Date());

        try {
            carRepository.save(car);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCar(Car car) throws Exception {
        Optional<Car> existsCar = carRepository.findByLicense(car.getLicense());
        if(!existsCar.isPresent()) {
            throw new Exception("רכב לא קיים");
        }
        Car tempCar = existsCar.get();
        if(tempCar.getTenant() != car.getTenant() || tempCar.getCompany() != car.getCompany()) {
            throw new Exception("רכב לא קיים");
        }
        if(tempCar.getCreationDate() != null) {
            car.setCreationDate(tempCar.getCreationDate());
        } else {
            car.setCreationDate(new Date());
        }
        checkCarRules(car);
        car.setLastUpdateDate(new Date());

        try {
            carRepository.save(car);
        } catch (Exception e) {
            // TODO: Take care of the exception
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCar(String license, Integer tenant, Integer company) throws Exception {
        Optional<Car> existsCar = carRepository.findByLicense(license);
        if(!existsCar.isPresent()) {
            throw new Exception("רכב לא קיים");
        }
        if(existsCar.get().getTenant() != tenant || existsCar.get().getCompany() != company) {
            throw new Exception("רכב לא קיים");
        }

        carRepository.deleteByLicense(license);
    }

    @Override
    public void storeCarFile(MultipartFile file, String name, String type, String carLicense) throws Exception {
        String fileName;

        User user = tenantService.getLoginUser();

        if(StringUtils.isEmpty(name)) {
            fileName = StringUtils.cleanPath(file.getOriginalFilename());
        } else {
            fileName = name;
            if(!name.contains(".")) {
                // Take the suffix from file name
                int index = file.getOriginalFilename().lastIndexOf(".");
                if(index > 0) {
                    fileName = fileName + file.getOriginalFilename().substring(index);
                }
            }
        }
        if(fileName.contains("..") || fileName.contains("\\") || fileName.contains("/")) {
            throw new Exception("קובץ מכיל תווים לא חוקיים " + fileName);
        }

        Path fileStorageLocation = Paths.get(Constants.BASE_FILES_DIR, String.valueOf(user.getTenant()), carLicense).toAbsolutePath().normalize();
        Path targetLocation;
        try {
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            } else {
                if (Files.isRegularFile(fileStorageLocation)) {
                    throw new Exception("אי אפשר לשמור לקובץ קיים");
                }
            }
            targetLocation = fileStorageLocation.resolve(System.currentTimeMillis() + "_" + fileName);
            Files.copy(file.getInputStream(), targetLocation);

        } catch (IOException ex) {
            throw new Exception(".שמירת הקובץ נכשלה, אנא נסה שוב ", ex);
        }

        CarDocument carDocument = new CarDocument();
        carDocument.setTenant(user.getTenant());
        carDocument.setCompany(user.getCurrentCompany());
        carDocument.setCarLicense(carLicense);
        carDocument.setCreationDate(new Date());
        carDocument.setLastUpdateDate(carDocument.getCreationDate());
        carDocument.setDocName(fileName);
        carDocument.setDocType(type);
        carDocument.setUploadUserName(user.getName() + " " + user.getLastName());
        carDocument.setDocPath(targetLocation.toAbsolutePath().toString());

        try {
            carDocsRepository.save(carDocument);
        } catch (RuntimeException e) {
            // TODO fix Unable to parse response body for Response
        }
    }

    @Override
    public Iterable<CarDocument> getCarDocsList(Integer tenant, Integer company, String carLicence) {
        return carDocsRepository.findByTenantAndCompanyAndCarLicense(tenant, company, carLicence);
    }

    @Override
    public Optional<CarDocument> getCarDocument(String docId, String license, Integer tenant, Integer company) throws Exception {
        return carDocsRepository.findById(docId);
    }

    @Override
    public void deleteCarDoc(Integer tenant, Integer company, String docId) throws Exception {
        Optional<CarDocument> carDocument = carDocsRepository.findById(docId);
        if(!carDocument.isPresent()) {
            throw new Exception("מסמך לא קיים.");
        }

        CarDocument carDocumentParent = carDocument.get();
        if(carDocumentParent.getTenant() != tenant || carDocumentParent.getCompany() != company) {
            throw new Exception("מסמך לא קיים.");
        }

        try {
            File fileForDelete = new File(carDocument.get().getDocPath());
            fileForDelete.delete();
        } catch (Exception e) {
            // TODO take care - failed delete doc
            log.error("Failed delete from file system", e);
        }

        try {
            carDocsRepository.deleteById(docId);
        } catch (RuntimeException e) {
            // TODO take care - Unable to parse response body for
        } catch (Exception e) {
            log.error("Failed delete from repository", e);
            throw e;
        }
    }

    @Override
    public CarDocument getCarDocFile(String docId, Integer tenant, Integer company) throws Exception {
        Optional<CarDocument> carDocument = carDocsRepository.findById(docId);
        if(!carDocument.isPresent()) {
            throw new Exception("מסמך לא קיים.");
        }

        CarDocument carDocumentParent = carDocument.get();
        if(carDocumentParent.getTenant() != tenant || carDocumentParent.getCompany() != company) {
            throw new Exception("מסמך לא קיים.");
        }

        return carDocument.get();
    }

    public void checkCarRules(Car car) {
        CarPolicy carPolicy = new CarPolicy();
        long diffInMS;
        long diff;
        StringBuilder warnMessage = new StringBuilder();
        StringBuilder alarmMessage = new StringBuilder();

        if(car.getCurrentKM() > 0 ) {
            if (car.getCurrentKM() - car.getKmNextTipul() > carPolicy.getKmNextTipulAlarm()) {
                car.setKmNextTipulStatus(Constants.ALARM_STATUS);
                alarmMessage.append("מספר הקילומטרים קרוב מאד לטיפול \n");
            } else {
                if (car.getCurrentKM() - car.getKmNextTipul() > carPolicy.getKmNextTipulWarn()) {
                    car.setKmNextTipulStatus(Constants.WARN_STATUS);
                    warnMessage.append("מספר הקילומטרים קרוב לטיפול \n");
                } else {
                    car.setKmNextTipulStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getLicenseDate() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getLicenseDate().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getLicenseDateAlarm()) {
                car.setLicenseDateStatus(Constants.ALARM_STATUS);
                alarmMessage.append("שימו לב: תקופת הרשיון בקרוב מאד מסתיימת \n");
            } else {
                if (diff < carPolicy.getLicenseDateWarn()) {
                    car.setLicenseDateStatus(Constants.WARN_STATUS);
                    warnMessage.append("שימו לב: תקופת הרשיון בקרוב מסתיימת \n");
                } else {
                    car.setLicenseDateStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getInsuranceHova() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getInsuranceHova().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getInsuranceHovaAlarm()) {
                car.setInsuranceHovaStatus(Constants.ALARM_STATUS);
                alarmMessage.append("ביטוח חובה מסתיים בקרוב מאד. \n");
            } else {
                if (diff < carPolicy.getInsuranceHovaWarn()) {
                    car.setInsuranceHovaStatus(Constants.WARN_STATUS);
                    warnMessage.append("ביטוח חובה מסתיים בקרוב. \n");
                } else {
                    car.setInsuranceHovaStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getInsuranceSelishi() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getInsuranceSelishi().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getInsuranceSelishiAlarm()) {
                car.setInsuranceSelishiStatus(Constants.ALARM_STATUS);
                alarmMessage.append("ביטוח צד ג' מסתיים בקרוב מאד. \n");
            } else {
                if (diff < carPolicy.getInsuranceSelishiWarn()) {
                    car.setInsuranceSelishiStatus(Constants.WARN_STATUS);
                    warnMessage.append("ביטוח צד ג' מסתיים בקרוב. \n");
                } else {
                    car.setInsuranceSelishiStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getBreaksHalfYear() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getBreaksHalfYear().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getBreaksHalfYearAlarm()) {
                car.setBreaksHalfYearStatus(Constants.ALARM_STATUS);
                alarmMessage.append("תקופת בלמים חצי שנתי מסתיימת בקרוב מאד.\n");
            } else {
                if (diff < carPolicy.getBreaksHalfYearWarn()) {
                    car.setBreaksHalfYearStatus(Constants.WARN_STATUS);
                    warnMessage.append("תקופת בלמים חצי שנתי מסתיימת בקרוב. \n");
                } else {
                    car.setBreaksHalfYearStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getTechnograph() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getTechnograph().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getTechnographAlarm()) {
                car.setTechnographStatus(Constants.ALARM_STATUS);
                alarmMessage.append("תקופת טכנוגרף מסתיימת בקרוב מאד. \n");
            } else {
                if (diff < carPolicy.getTechnographWarn()) {
                    car.setTechnographStatus(Constants.WARN_STATUS);
                    warnMessage.append("תקופת טכנוגרף מסתיימת בקרוב.\n");
                } else {
                    car.setTechnographStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getEngineerApproval() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getEngineerApproval().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getEngineerApprovalAlarm()) {
                car.setEngineerApprovalStatus(Constants.ALARM_STATUS);
                alarmMessage.append("תקופת אישור מהנדס מסתיימת בקרוב מאד. \n");
            } else {
                if (diff < carPolicy.getEngineerApprovalWarn()) {
                    car.setEngineerApprovalStatus(Constants.WARN_STATUS);
                    warnMessage.append("תקופת אישור מהנדס מסתיימת בקרוב. \n");
                } else {
                    car.setEngineerApprovalStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getWinterReview() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getWinterReview().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getWinterReviewAlarm()) {
                car.setWinterReviewStatus(Constants.ALARM_STATUS);
                alarmMessage.append("תקופת בדיקת חורף מסתיימת בקרוב מאד. \n");
            } else {
                if (diff < carPolicy.getWinterReviewWarn()) {
                    car.setWinterReviewStatus(Constants.WARN_STATUS);
                    warnMessage.append("תקופת בדיקת חורף מסתיימת בקרוב. \n");
                } else {
                    car.setWinterReviewStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getLeadLicense() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getLeadLicense().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getLeadLicenseAlarm()) {
                car.setLeadLicenseStatus(Constants.ALARM_STATUS);
                alarmMessage.append("תקופת רשיון מוביל מסתיימת בקרוב מאד. \n");
            } else {
                if (diff < carPolicy.getLeadLicenseWarn()) {
                    car.setLeadLicenseStatus(Constants.WARN_STATUS);
                    warnMessage.append("תקופת רשיון מוביל מסתיימת בקרוב. \n");
                } else {
                    car.setLeadLicenseStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getLabTest() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getLabTest().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getLabTestAlarm()) {
                car.setLabTestStatus(Constants.ALARM_STATUS);
                alarmMessage.append("תקופת בדיקת מעבדה מסתיימת בקרוב מאד. \n");
            } else {
                if (diff < carPolicy.getLabTestWarn()) {
                    car.setLabTestStatus(Constants.WARN_STATUS);
                    warnMessage.append("תקופת בדיקת מעבדה מסתיימת בקרוב. \n");
                } else {
                    car.setLabTestStatus(Constants.OK_STATUS);
                }
            }
        }

        if(car.getLeasingEndDate() != null) {
            diffInMS = Math.abs((new Date()).getTime() - car.getLeasingEndDate().getTime());
            diff = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);
            if (diff < carPolicy.getLeasingEndDateAlarm()) {
                car.setLeasingEndDateStatus(Constants.ALARM_STATUS);
                alarmMessage.append("שימו לב: תקופת הליסינג בקרוב מאד מסתיימת \n");
            } else {
                if (diff < carPolicy.getLeasingEndDateWarn()) {
                    car.setLeasingEndDateStatus(Constants.WARN_STATUS);
                    warnMessage.append("שימו לב: תקופת הליסינג בקרוב מסתיימת \n");
                } else {
                    car.setLeasingEndDateStatus(Constants.OK_STATUS);
                }
            }
        }

        if(alarmMessage.length() > 0){
            car.setStatus(Constants.ALARM_STATUS);
            if(warnMessage.length() > 0){
                car.setMessages(alarmMessage + "\n" + warnMessage);
            } else {
                car.setMessages(alarmMessage.toString());
            }
        } else if(warnMessage.length() > 0) {
            car.setStatus(Constants.WARN_STATUS);
            car.setMessages(warnMessage.toString());

        } else {
            car.setStatus(Constants.OK_STATUS);
            car.setMessages("רשומת רכב תקינה");
        }

    }

}
