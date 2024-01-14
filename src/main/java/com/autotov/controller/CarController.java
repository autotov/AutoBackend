package com.autotov.controller;

import com.autotov.app.Constants;
import com.autotov.model.Car;
import com.autotov.model.CarDocument;
import com.autotov.model.CarResp;
import com.autotov.model.User;
import com.autotov.service.CarService;
//import com.autotov.service.ExistCarsService;
import com.autotov.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class CarController {

    @Autowired
    ServletContext context;

    @Autowired
    private CarService carService;

    //@Autowired
    //private ExistCarsService existCarsService;

    @Autowired
    private TenantService tenantService;

    @GetMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCars(@RequestParam Integer company) {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.ADMIN_ROLE) && !user.getCompanies().contains(company)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.FORBIDDEN);
        }

        Iterable<Car> cars = carService.getCarList(user.getTenant(), company);
        List<CarResp> newCars = new ArrayList<>();
        cars.forEach(car -> {
            CarResp carResp = new CarResp(car);
            newCars.add(carResp);
        });

        return new ResponseEntity<>(newCars, HttpStatus.OK);
    }

    @GetMapping("/car/{license}")
    public ResponseEntity<Object> getCarByLicense(@PathVariable("license") String license) {
        User user = tenantService.getLoginUser();

        Optional<Car> cars;
        try {
            cars = carService.getCar(license, user.getTenant(), user.getCurrentCompany());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if(cars.isPresent()) {
            CarResp carResp = new CarResp(cars.get());
            return new ResponseEntity<>(carResp, HttpStatus.OK);
        }

        return new ResponseEntity<>("רכב לא קיים", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/car/add")
    public  ResponseEntity<Object> addCar(@RequestBody CarResp carReq) {
        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            Car car = new Car(carReq);
            carService.addCar(car);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("רכב נוסף בהצלחה", HttpStatus.OK);
    }

    @PostMapping("/car/update")
    public  ResponseEntity<Object> updateCar(@RequestBody CarResp carReq) {
        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            Car car = new Car(carReq);
            car.setTenant(user.getTenant());
            car.setCompany(user.getCurrentCompany());

            carService.updateCar(car);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("רכב עודכן בהצלחה", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllCars")
    public void deleteAllCars() throws Exception {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN)) {
            throw new Exception("אין הרשאה לבקשה זו.");
        }

        carService.deleteAllCars();
    }

    @DeleteMapping("/car/docs/{docId}/delete")
    public ResponseEntity<Object> deleteCar(@PathVariable("docId") String docId) {
        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            carService.deleteCarDoc(user.getTenant(), user.getCurrentCompany(), docId);
        } catch (Exception e) {
            log.error("מחיקת רכב נכשלה", e);
            return new ResponseEntity<>("מחיקת רכב נכשלה", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("רכב נמחק בהצלחה", HttpStatus.OK);
    }

    @PostMapping("/upload/car/doc")
    public ResponseEntity<Object> uploadCarFile(@RequestPart("file") MultipartFile file, @RequestParam String name, @RequestParam String type, @RequestParam String carId) {
        try {
            carService.storeCarFile(file, name, type, carId);

        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(".שמירת הקובץ נכשלה, אנא נסה שוב", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("הקובץ מכיל תווים לא חוקיים", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("קובץ נשמר בהצלחה", HttpStatus.OK);
    }

    @GetMapping("/car/docs/{docId}")
    public ResponseEntity<Object> getCarDocFile(@PathVariable String docId) throws Exception {
        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        CarDocument carDocument = carService.getCarDocFile(docId, user.getTenant(), user.getCurrentCompany());
        byte[] encoded = Files.readAllBytes(Paths.get(carDocument.getDocPath()));

        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + carDocument.getDocName() + "\"")
                .body(new ByteArrayResource(encoded));
    }

    @DeleteMapping("/car/{license}/delete")
    public ResponseEntity<Object> deleteCarDoc(@PathVariable("license") String license) throws Exception {
        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        carService.deleteCar(license, user.getTenant(), user.getCurrentCompany());

        return new ResponseEntity<>("רכב נמחק בהצלחה", HttpStatus.OK);
    }

    @GetMapping(value = "/cars/docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCarDocs(@RequestParam Integer company, @RequestParam String carLicence) {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.ADMIN_ROLE) && !user.getCompanies().contains(company)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.FORBIDDEN);
        }

        Iterable<CarDocument> carDocs = carService.getCarDocsList(user.getTenant(), company, carLicence);
        /*List<CarResp> newCars = new ArrayList<>();
        carDocs.forEach(document -> {
            CarResp carResp = new CarResp(car);
            newCars.add(carResp);
        });*/

        return new ResponseEntity<>(carDocs, HttpStatus.OK);
    }
/*
    @GetMapping("/exist/car/{license}")
    public ResponseEntity<Object> getExistCarByMisparRechev(@PathVariable("license") String license) {
        User user = tenantService.getLoginUser();

        Optional<Car> cars;
        try {
            cars = carService.getCar(license, user.getTenant(), user.getCurrentCompany());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if(cars.isPresent()) {
            CarResp carResp = new CarResp(cars.get());
            return new ResponseEntity<>(carResp, HttpStatus.OK);
        }

        return new ResponseEntity<>("רכב לא קיים", HttpStatus.BAD_REQUEST);
    }
*/
    /*@GetMapping("/exist/car/{misparRechev}")
    public ResponseEntity<Object> getExistCarByMisparRechev(@PathVariable("license") Long existCar) throws Exception {
        existCarsService.getCar(existCar);
    }*/

    /*@GetMapping("/exist/car/list")
    public ResponseEntity<Object> getAllExistCarByMisparRechev(Long existCar) throws Exception {
        existCarsService.getCar(existCar);
    }*/
}
