package com.autotov.controller;

import com.autotov.app.Constants;
import com.autotov.model.*;
import com.autotov.security.jwt.JwtUtils;
import com.autotov.security.services.UserDetailsImpl;
import com.autotov.service.TenantService;
import com.autotov.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    private void setCompaniesNames(UserResponse user) {
        List<CompanyItem> companies = new ArrayList<>();
        Map<Integer, Company> allCompanies;
        try {
            if(Constants.SUPER_ADMIN.equalsIgnoreCase(user.getRole())) {
                allCompanies = tenantService.getAllCompanies();
            } else {
                allCompanies = tenantService.getAllCompaniesForTenant(tenantService.getLoginUser().getTenant());
            }

            for (Company company : allCompanies.values()) {
                if(Constants.SUPER_ADMIN.equalsIgnoreCase(user.getRole())
                        || Constants.ADMIN_ROLE.equalsIgnoreCase(user.getRole()) || user.getCompanies().contains(company.getNumberId())) {
                    companies.add(new CompanyItem(company.getNumberId(), company.getName(), company));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setCompaniesNames(companies.toArray(new CompanyItem[companies.size()]));
        List<Integer> companiesIdList = new ArrayList<>();
        companies.forEach(item -> companiesIdList.add(item.getId()));
        user.setCompanies(companiesIdList);

        user.setCurrentCompany((companies.size() > 0) ? 1 : 0);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest login) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserResponse userResponse = new UserResponse(userDetails.getUser());
        setCompaniesNames(userResponse);
        //userDetails.getUser().setCurrentCompany(userResponse.getCurrentCompany());
        userResponse.setCurrentCompany(userDetails.getUser().getCurrentCompany());

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(userResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<Object> logout() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/user/details")
    public ResponseEntity<Object> getUserDetails() {
        try {
            UserResponse userResponse = new UserResponse(tenantService.getLoginUser());
            setCompaniesNames(userResponse);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        User loginUser = tenantService.getLoginUser();
        if(!loginUser.getRole().equals(Constants.ADMIN_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לקבל רשימת משתמשים.", HttpStatus.UNAUTHORIZED);
        }
        Integer tenant = loginUser.getTenant();

        Iterable<User> users = userService.getUsersList(tenant);
        List<UserResponse> newUserList = new ArrayList<>();
        users.forEach(userItem -> {
            UserResponse userResponse = new UserResponse(userItem);
            newUserList.add(userResponse);
        });

        return new ResponseEntity<>(newUserList, HttpStatus.OK);
    }

    @PostMapping("/user/set/company")
    public ResponseEntity<Object> setCompany(@RequestBody Integer companyId) {
        User loginUser = tenantService.getLoginUser();
        if(!loginUser.getRole().equals(Constants.ADMIN_ROLE) && !loginUser.getCompanies().contains(companyId)) {
            return new ResponseEntity<>("אין הרשאה להשתמש בחברה זו.", HttpStatus.UNAUTHORIZED);
        }

        loginUser.setCurrentCompany(companyId);
        try {
            userService.updateUser(loginUser, false);
        } catch (RuntimeException e) {
            if(!e.getMessage().contains("Unable to parse")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("שימוש בחברה נקבע בהצלחה", HttpStatus.OK);
    }

    @PostMapping("/user/add")
    public ResponseEntity<Object> addUser(@RequestBody User newUser) {
        User loginUser = tenantService.getLoginUser();
        if(!loginUser.getRole().equals(Constants.ADMIN_ROLE) && !loginUser.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה ליצור משתמשים.", HttpStatus.UNAUTHORIZED);
        }

        newUser.setTenant(loginUser.getTenant());
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        List<Integer> companies = new ArrayList<>();
        companies.add(1);
        companies.add(2);
        newUser.setCompanies(companies);
        try {
            userService.addUser(newUser);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("משתמש נוסף בהצלחה", HttpStatus.OK);
    }

    @PostMapping("/user/update")
    public ResponseEntity<Object> updateUser(@RequestBody User userToEdit) {
        User loginUser = tenantService.getLoginUser();
        if(!loginUser.getRole().equals(Constants.ADMIN_ROLE) && !loginUser.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה לערוך משתמשים.", HttpStatus.UNAUTHORIZED);
        }

        userToEdit.setTenant(loginUser.getTenant());
        boolean setPassword = true;
        if(StringUtils.isNotEmpty(userToEdit.getPassword())) {
            userToEdit.setPassword(encoder.encode(userToEdit.getPassword()));
            setPassword = false;
        }

        try {
            userService.updateUser(userToEdit, setPassword);
        } catch (RuntimeException e) {
            if(!e.getMessage().contains("Unable to parse")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("משתמש עודכן בהצלחה", HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Integer userId) {
        User loginUser = tenantService.getLoginUser();
        if(!loginUser.getRole().equals(Constants.ADMIN_ROLE) && !loginUser.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה למחוק משתמשים.", HttpStatus.UNAUTHORIZED);
        }

        try {
            userService.deleteUser(userId, loginUser.getTenant());
        } catch (RuntimeException e) {
            if(!e.getMessage().contains("Unable to parse")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("משתמש נמחק בהצלחה", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllUsers")
    public ResponseEntity<Object> deleteAllUsers() {
        User loginUser = tenantService.getLoginUser();
        if(!loginUser.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה למחוק משתמשים.", HttpStatus.UNAUTHORIZED);
        }

        userService.deleteAllUsers(loginUser.getTenant());

        return new ResponseEntity<>("כל המשתמשים נמחקו בהצלחה", HttpStatus.OK);
    }
}
