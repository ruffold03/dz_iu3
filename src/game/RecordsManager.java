package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecordsManager {
    private static String filePath = "records";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RecordsManager(String filePath) {
        this.filePath = filePath;
    }

    public static void saveRecord(Records newRecord) {
        List<Records> records = loadRecords();
        records.add(newRecord);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(records, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Records> loadRecords() {
        try (FileReader reader = new FileReader(filePath)) {
            Type recordListType = new TypeToken<ArrayList<Records>>() {}.getType();
            return gson.fromJson(reader, recordListType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}

