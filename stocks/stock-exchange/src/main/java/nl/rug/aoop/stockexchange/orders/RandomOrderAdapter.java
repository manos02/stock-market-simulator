package nl.rug.aoop.stockexchange.orders;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A custom TypeAdapter for serializing and deserializing RandomOrder objects to/from JSON.
 */
public class RandomOrderAdapter extends TypeAdapter<RandomOrder> {

    /**
     * Writes the RandomOrder object to a JSON writer.
     *
     * @param jsonWriter   The JSON writer used for serialization.
     * @param randomOrder  The RandomOrder object to be serialized.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    @Override
    public void write(JsonWriter jsonWriter, RandomOrder randomOrder) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type").value(randomOrder.getType());
        jsonWriter.name("stock").value(randomOrder.getStockName());
        jsonWriter.name("price").value(randomOrder.getPrice());
        jsonWriter.name("traderId").value(randomOrder.getTraderId());
        jsonWriter.name("quantity").value(randomOrder.getQuantity());
        jsonWriter.name("uniqueId").value(randomOrder.getUniqueId());
        jsonWriter.name("resolved").value(randomOrder.isResolved());
        jsonWriter.endObject();
    }

    /**
     * Reads a RandomOrder object from a JSON reader.
     *
     * @param jsonReader The JSON reader used for deserialization.
     * @return The deserialized RandomOrder object.
     * @throws IOException If an I/O error occurs during the reading process.
     */
    @Override
    public RandomOrder read(JsonReader jsonReader) throws IOException {
        String type = null;
        String stockName = null;
        double price = 0;
        String id = null;
        int quantity = -1;
        String uniqueId = null;
        boolean resolved = false;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if ("type".equals(name)) {
                type = jsonReader.nextString();
            } else if ("stock".equals(name)) {
                stockName = jsonReader.nextString();
            } else if ("price".equals(name)) {
                price = jsonReader.nextDouble();
            } else if ("traderId".equals(name)) {
                id = jsonReader.nextString();
            } else if ("quantity".equals(name)) {
                quantity = jsonReader.nextInt();
            } else if ("uniqueId".equals(name)) {
                uniqueId = jsonReader.nextString();
            } else if ("resolved".equals(name)) {
                resolved = jsonReader.nextBoolean();
            } else {
                jsonReader.skipValue();
            }
        } jsonReader.endObject();
        return new RandomOrder(type, stockName, price, id, quantity, uniqueId); }
}
