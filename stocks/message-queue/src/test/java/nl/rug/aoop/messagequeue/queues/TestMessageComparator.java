package nl.rug.aoop.messagequeue.queues;

import nl.rug.aoop.messagequeue.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestMessageComparator {
    private MessageComparator comparator;
    private List<Message> messages;

    @BeforeEach
    void setUp() {
        comparator = new MessageComparator();
        messages = new ArrayList<>();


    }

    @Test
    void testMessageComparatorSorting() {
        messages.add(new Message("Header1", "Body1", LocalDateTime.of(2023, 9, 15, 10, 0)));
        messages.add(new Message("Header2", "Body2", LocalDateTime.of(2023, 9, 15, 11, 0)));
        messages.add(new Message("Header3", "Body3", LocalDateTime.of(2023, 9, 15, 9, 0)));

        Collections.shuffle(messages);

        messages.sort(comparator);

        assertEquals("Header3", messages.get(0).getHeader());
        assertEquals("Header1", messages.get(1).getHeader());
        assertEquals("Header2", messages.get(2).getHeader());
    }

    @Test
    void testMessageComparatorSameTime() {
        messages.add(new Message("Header1", "Body1", LocalDateTime.of(2023, 9, 15, 10, 0)));
        messages.add(new Message("Header2", "Body2", LocalDateTime.of(2023, 9, 15, 10, 0)));
        messages.add(new Message("Header3", "Body3", LocalDateTime.of(2023, 9, 15, 9, 0)));

        Collections.shuffle(messages);

        messages.sort(comparator);

        assertEquals("Header3", messages.get(0).getHeader());
        String header1Or2 = messages.get(1).getHeader();
        assertTrue(header1Or2.equals("Header1") || header1Or2.equals("Header2"));

    }

}
