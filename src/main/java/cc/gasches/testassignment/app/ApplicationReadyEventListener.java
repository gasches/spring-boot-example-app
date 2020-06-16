package cc.gasches.testassignment.app;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cc.gasches.testassignment.service.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger log = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

    private static final String USER_VAR = "APP_USER";
    private static final String PASSWORD_VAR = "APP_PASSWORD";

    private final Environment env;
    private final UserService userService;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        String username = env.getProperty(USER_VAR);
        String password = env.getProperty(PASSWORD_VAR);

        if (StringUtils.isAnyBlank(username, password)) {
            return;
        }
        log.info("Create user: {}", username);
        userService.createIfNotExists(username, password);
    }
}
