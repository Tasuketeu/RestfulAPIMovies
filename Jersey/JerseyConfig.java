package com.company.base.accenture.movies.Jersey;

import com.company.base.accenture.movies.BL.MovieServiceImpl;
import com.company.base.accenture.movies.BL.UserServiceImpl;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/app")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
// регистрация REST контроллеров
        register(MovieServiceImpl.class);
        register(UserServiceImpl.class);
        register(IndexController.class);
        packages("com.company.accenture.movies");
    }
}

