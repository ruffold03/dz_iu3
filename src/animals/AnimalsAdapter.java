package animals;

import com.google.gson.*;
import java.lang.reflect.Type;

public class AnimalsAdapter implements JsonSerializer<Animals>, JsonDeserializer<Animals> {

    @Override
    public JsonElement serialize(Animals src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = context.serialize(src).getAsJsonObject();
        result.addProperty("type", src.getClass().getSimpleName());
        return result;
    }

    @Override
    public Animals deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("type")) {
            throw new JsonParseException("Missing 'type' field in Unit JSON");
        }

        JsonElement typeElement = jsonObject.get("type");
        if (typeElement == null || typeElement.isJsonNull()) {
            throw new JsonParseException("Unit type cannot be null");
        }
        String type = jsonObject.get("type").getAsString();

        // 4. Проверяем, что тип не пустой
        if (type == null || type.trim().isEmpty()) {
            throw new JsonParseException("Unit type cannot be empty");
        }

        /*try {
            Class<? extends Animals> unitClass =
                    (Class<? extends Animals>) Class.forName("units." + type);
            return context.deserialize(json, unitClass);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown unit type: " + type, e);
        }*/

        return switch (type) {
            case "Eagle" -> context.deserialize(json, Eagle.class);
            case "Husky" -> context.deserialize(json, Husky.class);
            case "Whales" -> context.deserialize(json, Whales.class);
            default -> throw new JsonParseException("Unknown unit type: " + type);
        };
    }
}

