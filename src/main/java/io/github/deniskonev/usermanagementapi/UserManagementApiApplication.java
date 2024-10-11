package io.github.deniskonev.usermanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:db.properties")
public class UserManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApiApplication.class, args);
    }

}
