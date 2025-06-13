package units;

import com.google.gson.*;
import java.lang.reflect.Type;

public class UnitAdapter implements JsonSerializer<Unit>, JsonDeserializer<Unit> {

    @Override
    public JsonElement serialize(Unit src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = context.serialize(src).getAsJsonObject();
        result.addProperty("type", src.getClass().getSimpleName());
        return result;
    }

    @Override
    public Unit deserialize(JsonElement json, Type typeOfT,
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

        if (type == null || type.trim().isEmpty()) {
            throw new JsonParseException("Unit type cannot be empty");
        }

        try {
            Class<? extends Unit> unitClass =
                    (Class<? extends Unit>) Class.forName("units." + type);
            return context.deserialize(json, unitClass);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown unit type: " + type, e);
        }

        /*return switch (type) {
            case "Archer" -> context.deserialize(json, Archer.class);
            case "Cavalryman" -> context.deserialize(json, Cavalryman.class);
            case "Champion" -> context.deserialize(json, Champion.class);
            case "Crusader" -> context.deserialize(json, Crusader.class);
            case "Fanatic" -> context.deserialize(json, Fanatic.class);
            case "Griffin" -> context.deserialize(json, Griffin.class);
            case "Halberdier" -> context.deserialize(json, Halberdier.class);
            case "Monk" -> context.deserialize(json, Monk.class);
            case "RoyalGriffin" -> context.deserialize(json, RoyalGriffin.class);
            case "Shooter" -> context.deserialize(json, Shooter.class);
            case "Spearman" -> context.deserialize(json, Spearman.class);
            case "Swordsman" -> context.deserialize(json, Swordsman.class);
            default -> throw new JsonParseException("Unknown unit type: " + type);
        };*/
    }
}
