package nl.rug.aoop.messagequeue.message;

import java.time.LocalDateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A model for the message.
 *
 * @param header of the message.
 * @param body of the message.
 * @param timestamp of the message.
 */

public record Message(String header, String body, LocalDateTime timestamp) implements Comparable<Message> {
    /**
     * constructor of message class.
     *
     * @param header to be initialised.
     * @param body to be initialised.
     */
    public Message(String header, String body) {
        this(header, body, LocalDateTime.now());
    }

    /**
     * Creates a Gson instance with a custom MessageAdapter for JSON serialization/deserialization.
     *
     * @return A Gson instance.
     */
    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        return gsonBuilder.create();
    }

    /**
     * Converts the message to a JSON-formatted string.
     *
     * @return A JSON-formatted string representation of the message.
     */

    public String convertToJsonString() {
        return createGson().toJson(this);
    }

    /**
     * Converts a JSON-formatted message string back into a Message object.
     *
     * @param jsonMessage The JSON-formatted message string.
     * @return A Message object.
     */

    public static Message convertToMessage(String jsonMessage) {
        return createGson().fromJson(jsonMessage, Message.class);
    }

    /**
     * Compares this message with another message based on their timestamps.
     *
     * @param other The other message to compare with.
     * @return A negative integer, zero, or a positive integer if this message is less than, equal to, or
     *         greater than the other message in terms of timestamp.
     */
    @Override
    public int compareTo(Message other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
