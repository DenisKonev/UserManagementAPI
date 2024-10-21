package io.github.deniskonev.config;

import lombok.experimental.UtilityClass;

//TODO Вынести настройки в файл конфигурации
@UtilityClass
public class ApiConstants {

    public static final String BASE_API = "/api/v1";
    public static final String USERS = "/users";
    public static final String EXTERNAL_ROLES_API = "http://localhost:8888/api/v1/roles";
}
