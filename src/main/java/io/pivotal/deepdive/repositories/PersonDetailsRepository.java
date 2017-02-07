package io.pivotal.deepdive.repositories;

import io.pivotal.deepdive.model.PersonDetails;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PersonDetailsRepository {

    Map<String, PersonDetails> personDetailsMap = new HashMap<>();

    public void savePersonDetails(PersonDetails personDetails) {
        personDetailsMap.put(personDetails.getName(), personDetails);
    }

    public PersonDetails getPersonDetailsByName(String name) {
        return personDetailsMap.get(name);
    }
}
