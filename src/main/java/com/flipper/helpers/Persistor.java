package com.flipper.helpers;

import com.flipper.api.Api;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.responses.LoginResponse;

import net.runelite.client.RuneLite;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Read/Writes information to json file for storage
 */
public class Persistor {
    public static Gson gson = new Gson();
    public static final File PARENT_DIRECTORY = new File(RuneLite.RUNELITE_DIR, "flipper");
    public static File directory;
    public static final String SELLS_JSON_FILE = "flipper-sells.json";
    public static final String BUYS_JSON_FILE = "flipper-buys.json";
    public static final String MARGINS_JSON_FILE = "flipper-margins.json";
    public static final String LOGIN_RESPONSE_JSON_FILE = "flipper-login-response.json";

    public static void setUp(String directoryPath) throws IOException {
        directory = new File(directoryPath);
        createDirectory(directory);
        createRequiredFiles();
    }

    public static void setUp() throws IOException {
        directory = PARENT_DIRECTORY;
        createDirectory(PARENT_DIRECTORY);
        createRequiredFiles();
    }

    /**
     * Creates 3 required json files: sells, buys, flips
     */
    private static void createRequiredFiles() throws IOException {
        generateFileIfDoesNotExist(SELLS_JSON_FILE);
        generateFileIfDoesNotExist(BUYS_JSON_FILE);
        generateFileIfDoesNotExist(MARGINS_JSON_FILE);
        generateFileIfDoesNotExist(LOGIN_RESPONSE_JSON_FILE);
    }

    private static void generateFileIfDoesNotExist(String filename) throws IOException {
        File file = new File(directory, filename);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                Log.info("Failed to generate file " + file.getPath());
            }
        }
    }

    private static void createDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            Log.info("Creating flipper directory");
            if (!directory.mkdir()) {
                throw new IOException("unable to create parent directory!");
            }
        }
    }

    public static LoginResponse loadLoginResponse() throws IOException {
        String jsonString = getFileContent(LOGIN_RESPONSE_JSON_FILE);
        LoginResponse loadedLoginResponse = gson.fromJson(jsonString, LoginResponse.class);

        if (loadedLoginResponse != null) {
            Api.setLoginResponse(loadedLoginResponse);
        }

        return loadedLoginResponse;
    }

    public static void saveLoginResponse(LoginResponse loginResponse) throws IOException {
        File file = new File(directory, LOGIN_RESPONSE_JSON_FILE);
        String json = gson.toJson(loginResponse);
        Files.write(file.toPath(), json.getBytes());
    }

    public static void deleteLoginResponse() throws IOException {
        Files.delete(Path.of(directory.getAbsolutePath(), LOGIN_RESPONSE_JSON_FILE));
    }

    public static void saveJson(List<?> list, String filename) throws IOException {
        File file = new File(directory, filename);
        final String json = gson.toJson(list);
        Files.write(file.toPath(), json.getBytes());
    }

    private static String getFileContent(String filename) throws IOException {
        Path filePath = Paths.get(directory + "/" + filename);
        byte[] fileBytes = Files.readAllBytes(filePath);
        return new String(fileBytes);
    }

    public static boolean saveBuys(List<Transaction> buys) {
        try {
            saveJson(buys, BUYS_JSON_FILE);
            return true;
        } catch (Exception error) {
            Log.info("Failed to save buys " + error.toString());
            return false;
        }
    }

    public static boolean saveSells(List<Transaction> sells) {
        try {
            saveJson(sells, SELLS_JSON_FILE);
            return true;
        } catch (Exception error) {
            Log.info("Failed to save sells " + error.toString());
            return false;
        }
    }

    public static boolean saveMargins(List<Flip> margins) {
        try {
            saveJson(margins, MARGINS_JSON_FILE);
            return true;
        } catch (Exception error) {
            Log.info("Failed to save flips " + error.toString());
            return false;
        }
    }

    public static List<Flip> loadMargins() throws IOException {
        String jsonString = getFileContent(MARGINS_JSON_FILE);
        Type type = new TypeToken<List<Flip>>() {
        }.getType();
        List<Flip> margins = gson.fromJson(jsonString, type);
        if (margins == null) {
            return new ArrayList<Flip>();
        }
        return margins;
    }

    public static List<Transaction> loadBuys() throws IOException {
        String jsonString = getFileContent(BUYS_JSON_FILE);
        Type type = new TypeToken<List<Transaction>>() {
        }.getType();
        List<Transaction> buys = gson.fromJson(jsonString, type);
        if (buys == null) {
            return new ArrayList<Transaction>();
        }
        return buys;
    }

    public static List<Transaction> loadSells() throws IOException {
        String jsonString = getFileContent(SELLS_JSON_FILE);
        Type type = new TypeToken<List<Transaction>>() {
        }.getType();
        List<Transaction> sells = gson.fromJson(jsonString, type);
        if (sells == null) {
            return new ArrayList<Transaction>();
        }
        return sells;
    }
}
