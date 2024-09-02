package com.example.demoapp.service;

import com.example.demoapp.entity.User;
import com.example.demoapp.repo.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserGeneratorService {

    private final static int USERNAME_LENGTH = 20;
    private final static String EMAIL_TEMPLATE = "%s@example.com";
    private final static int CHUNK_SIZE = 100000;

    private final UserRepository userRepository;

    public UserGeneratorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void generateAndSaveUsers(int amount, LocalDate birthdateFrom, LocalDate birthdayTo) {
        int counter = 0;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            if (counter == CHUNK_SIZE) {
                saveAllUsers(users);
                counter = 0;
            }
            User user = generateUser(birthdateFrom, birthdayTo);
            users.add(user);
            counter++;
        }
        if (!users.isEmpty()) {
            saveAllUsers(users);
        }
    }

    private static User generateUser(LocalDate birthdateFrom, LocalDate birthdayTo) {
        String username = getRandomUsername();
        return new User(
                username,
                String.format(EMAIL_TEMPLATE, username),
                getRandomCountry(),
                getRandomDate(birthdateFrom, birthdayTo)
        );
    }

    private void saveAllUsers(List<User> users) {
        userRepository.saveAll(users);
        users.clear();
        System.gc();
    }

    private static String getRandomUsername() {
        return RandomStringUtils.randomAlphanumeric(USERNAME_LENGTH);
    }

    private static LocalDate getRandomDate(LocalDate startDate, LocalDate endDate) {
        int days = (int) (endDate.toEpochDay() - startDate.toEpochDay());
        return startDate.plusDays(ThreadLocalRandom.current().nextInt(days + 1));
    }

    private static String getRandomCountry() {
        String[] countries = Locale.getISOCountries();
        return new Locale(
                "",
                countries[ThreadLocalRandom.current().nextInt(countries.length)]
        ).getCountry();
    }
}
