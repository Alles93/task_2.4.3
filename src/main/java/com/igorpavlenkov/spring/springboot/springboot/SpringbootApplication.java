package com.igorpavlenkov.spring.springboot.springboot;

import com.igorpavlenkov.spring.springboot.springboot.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringbootApplication {

    static final String URL_USERS = "http://91.241.64.178:7081/api/users";

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);


        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        // Request to return JSON format
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("my_other_key", "my_other_value");

        // HttpEntity<String>: To get result as String.
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<String> response = restTemplate.exchange(URL_USERS, HttpMethod.GET, entity, String.class);
        String users = response.getBody();
        System.out.println(users);
        //Смотрим заголовок с id-session
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        System.out.println(cookies);
        //Назначаем другим запросам id-session
        headers.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        //Сохранить пользователя с id = 3, name = James, lastName = Brown, age = на ваш выбор
        User newUser = new User(3L, "James", "Brown", (byte) 50);
        HttpEntity<User> entityAdd = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> responseAddUser = restTemplate.exchange(URL_USERS, HttpMethod.POST, entityAdd, String.class);
        String firstPart = responseAddUser.getBody();
        System.out.println(firstPart);
        //Изменить пользователя с id = 3. Необходимо поменять name на Thomas, а lastName на Shelby
        User updateUser = new User(3L, "Thomas", "Shelby", (byte) 50);
        HttpEntity<User> entityUpdate = new HttpEntity<>(updateUser, headers);
        ResponseEntity<String> responseUpdate = restTemplate.exchange(URL_USERS, HttpMethod.PUT, entityUpdate, String.class);
        String secondPart = responseUpdate.getBody();
        System.out.println(secondPart);
        //Удалить пользователя с id = 3
        Long id = updateUser.getId();
        ResponseEntity<String> responseDelete = restTemplate.exchange(URL_USERS + "/" + id, HttpMethod.DELETE, entityUpdate, String.class);
        String thirdPart = responseDelete.getBody();
        System.out.println(thirdPart);
        StringBuilder t1 = new StringBuilder();
        t1.append(firstPart).append(secondPart).append(thirdPart);
        System.out.println(t1);
    }
}