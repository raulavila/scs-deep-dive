package io.pivotal.deepdive;

import io.pivotal.deepdive.model.PersonDetails;
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

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScsDeepDiveApplicationTests {

    @Autowired
    private Processor processor;

    @Autowired
    private MessageCollector messageCollector;

	@Test
	public void test() throws Exception {
        assertTrue(channelIsEmpty(processor.output()));
    }

    private Message<PersonDetails> aMessage(PersonDetails personDetails) {
        return MessageBuilder.withPayload(personDetails).build();
    }

    private PersonDetails pollPersonDetails(MessageChannel channel) throws InterruptedException {
        Message<?> messagePolled = messageCollector.forChannel(channel).poll(2, TimeUnit.SECONDS);
        return (PersonDetails)messagePolled.getPayload();
    }

    private boolean channelIsEmpty(MessageChannel channel) {
        return messageCollector.forChannel(channel).poll() == null;
    }

}
