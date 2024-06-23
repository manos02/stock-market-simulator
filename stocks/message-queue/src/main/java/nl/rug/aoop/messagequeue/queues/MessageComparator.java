package nl.rug.aoop.messagequeue.queues;

import nl.rug.aoop.messagequeue.message.Message;

import java.util.Comparator;

/**
 * Comparator class for our orderedQueue based on the timestamps.
 */
public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message message1, Message message2) {
        return message1.getTimestamp().compareTo(message2.getTimestamp());
    }
}
