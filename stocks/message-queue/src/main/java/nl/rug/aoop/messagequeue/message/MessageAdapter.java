package nl.rug.aoop.messagequeue.message;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom TypeAdapter for serializing and deserializing Message objects using Gson.
 */
public class MessageAdapter extends TypeAdapter<Message> {

    /**
     * Serializes a Message object to JSON format.
     *
     * @param out The JSON writer to which the Message object will be written.
     * @param message to be serialized.
     * @throws IOException If an I/O error occurs during serialization.
     */
    @Override
    public void write(JsonWriter out, Message message) throws IOException {
        out.beginObject();
        out.name("header").value(message.header());
        out.name("body").value(message.body());
        out.name("timestamp").value(message.timestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        out.endObject();
    }

    /**
     * Deserializes JSON data into a `Message` object.
     *
     * @param in The JSON reader from which JSON data will be read.
     * @return A deserialized `Message` object.
     * @throws IOException If an I/O error occurs during deserialization.
     */
    @Override
    public Message read(JsonReader in) throws IOException {
        in.beginObject();
        String header = null;
        String body = null;
        String timestamp = null;

        while (in.hasNext()) {
            String name = in.nextName();
            if ("header".equals(name)) {
                header = in.nextString();
            } else if ("body".equals(name)) {
                body = in.nextString();
            } else if ("timestamp".equals(name)) {
                timestamp = in.nextString();
            } else {
                in.skipValue();
            }
        }

        in.endObject();

        if (timestamp != null) {
            return new Message(header, body, LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            throw new IOException("Invalid JSON format for message");
        }

    }
}
