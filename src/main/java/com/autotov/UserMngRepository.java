package com.autotov;

import com.autotov.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

//import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class UserMngRepository {

    private static Logger log = LoggerFactory.getLogger(UserMngRepository.class);
    // used only for dev on windows
    private Map<String, User> defaultUsers = new ConcurrentHashMap<>(); //username to userDetails

    private Map<String, User> tokenToUser = new ConcurrentHashMap<>(); //token to userDetails

    @Autowired
    private Environment env;

    // In minutes, default 15 minutes
    @Value("${idle.timeout:30}")
    private int idleTimeout;

    @Value("${non.expired.user:#{null}}")
    private String nonExpiredIdleTimeoutUser;

    /*@PostConstruct
    public void init() {

    }*/

    public User findByUsername(String username) {
        return defaultUsers.get(username.toUpperCase(Locale.ROOT));
    }

    public void addToken(String token, User user) {
        log.debug("Token added for user {}", user);
        tokenToUser.put(token, user);
    }

    public User findByToken(String token) {
        return tokenToUser.get(token);
    }

    public void removeToken(String token) {
        log.debug("Token {} removed for user {}", token, tokenToUser.get(token));
        tokenToUser.remove(token);
    }

    public Map<String, User> getTokenToUser() {
        return tokenToUser;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public String getNonExpiredIdleTimeoutUser() {
        return nonExpiredIdleTimeoutUser;
    }
}
