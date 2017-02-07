package io.pivotal.deepdive;

import io.pivotal.deepdive.model.PersonDetails;
import io.pivotal.deepdive.repositories.PersonDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.*;

import java.util.Arrays;
import java.util.List;

@MessageEndpoint
public class MessageHandler {

    @Autowired
    private PersonDetailsRepository personDetailsRepository;

    @Transformer(inputChannel = Processor.INPUT, outputChannel = "adultFilter")
    public PersonDetails uppercaseName(PersonDetails personDetails) {
        return new PersonDetails(
            personDetails.getName().toUpperCase(),
            personDetails.getAge(),
            personDetails.getPhoneNumbers()
        );
    }

    @Filter(inputChannel = "adultFilter", outputChannel = "adults")
    public boolean filterAdults(PersonDetails personDetails) {
        return personDetails.getAge() >= 18;
    }

    @Router(inputChannel = "adults")
    public List<String> fan() {
        return Arrays.asList("database", "phoneExtractor");
    }

    @ServiceActivator(inputChannel = "database")
    public void persistePersonDetails(PersonDetails personDetails) {
        personDetailsRepository.savePersonDetails(personDetails);
    }

    @Splitter(inputChannel = "phoneExtractor", outputChannel = Processor.OUTPUT)
    public List<String> extractPhones(PersonDetails personDetails) {
        return personDetails.getPhoneNumbers();
    }
}
