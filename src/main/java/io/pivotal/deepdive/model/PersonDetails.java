package io.pivotal.deepdive.model;

import java.util.List;

public class PersonDetails {

    private String name;
    private int age;
    private List<String> phoneNumbers;

    public PersonDetails(
            String name,
            int age,
            List<String> phoneNumbers
    ) {
        this.name = name;
        this.age = age;
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }
}
