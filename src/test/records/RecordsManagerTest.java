package test.records;

import game.Records;
import game.RecordsManager;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordsManagerTest {

    private static final String TEST_FILE = "test_records.json";
    private RecordsManager recordsManager;

    @BeforeEach
    void setUp() {
        recordsManager = new RecordsManager(TEST_FILE);
        new File(TEST_FILE).delete();
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testSaveAndLoadSingleRecord() {
        System.out.println("== ТЕСТ: Сохранение и загрузка одного рекорда ==");

        Records record = new Records("Alice", 123.45);
        recordsManager.saveRecord(record);

        List<Records> loaded = recordsManager.loadRecords();
        assertEquals(1, loaded.size());
        assertEquals("Alice", loaded.get(0).getPlayerName());
        assertEquals(123.45, loaded.get(0).getScore(), 0.001);
    }

    @Test
    void testSaveMultipleRecords() {
        System.out.println("== ТЕСТ: Сохранение и загрузка нескольких рекордов ==");

        recordsManager.saveRecord(new Records("Bob", 100));
        recordsManager.saveRecord(new Records("Carol", 200));

        List<Records> records = recordsManager.loadRecords();
        assertEquals(2, records.size());

        assertTrue(records.stream().anyMatch(r -> r.getPlayerName().equals("Bob") && r.getScore() == 100));
        assertTrue(records.stream().anyMatch(r -> r.getPlayerName().equals("Carol") && r.getScore() == 200));
    }

    @Test
    void testLoadFromEmptyFile() {
        System.out.println("== ТЕСТ: Загрузка из отсутствующего файла ==");

        List<Records> records = recordsManager.loadRecords();
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }
}
