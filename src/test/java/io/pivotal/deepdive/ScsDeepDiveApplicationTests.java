package io.pivotal.deepdive;

import io.pivotal.deepdive.model.PersonDetails;
import io.pivotal.deepdive.repositories.PersonDetailsRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScsDeepDiveApplicationTests {

    @Autowired
    private Processor processor;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private PersonDetailsRepository personDetailsRepository;

	@Test
	public void test() throws Exception {
        PersonDetails personDetails = new PersonDetails("Raul", 22, Arrays.asList("12345", "112233"));

        processor.input().send(aMessage(personDetails));

        assertThat(pollPhone(processor.output()), is("12345"));
        assertThat(pollPhone(processor.output()), is("112233"));

        PersonDetails expectedPersonDetails = new PersonDetails("RAUL", 22, Arrays.asList("12345", "112233"));

        assertThat(personDetailsRepository.getPersonDetailsByName("Raul"), Matchers.samePropertyValuesAs(expectedPersonDetails));
    }

    private Message<PersonDetails> aMessage(PersonDetails personDetails) {
        return MessageBuilder.withPayload(personDetails).build();
    }

    private PersonDetails pollPersonDetails(MessageChannel channel) throws InterruptedException {
        Message<?> messagePolled = messageCollector.forChannel(channel).poll(2, TimeUnit.SECONDS);
        return (PersonDetails) messagePolled.getPayload();
    }

    private String pollPhone(MessageChannel channel) throws InterruptedException {
        Message<?> messagePolled = messageCollector.forChannel(channel).poll(2, TimeUnit.SECONDS);
        return (String) messagePolled.getPayload();
    }

    private boolean channelIsEmpty(MessageChannel channel) {
        return messageCollector.forChannel(channel).poll() == null;
    }

}
