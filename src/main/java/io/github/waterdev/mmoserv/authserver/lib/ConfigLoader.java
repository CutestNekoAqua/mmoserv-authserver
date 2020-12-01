package io.github.waterdev.mmoserv.authserver.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class ConfigLoader {

    /*
    Instances of / for GSON
     */
    private static final JsonParser parser = new JsonParser();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Checks if config exists at given Filepath.
     * If it exists it gets loaded.
     * If it doesn't exists a new config will be created and it will return null.
     * @param tClass The object type of the class you wish to load. (Should be T.class)
     * @param file The path to the Config
     * @param <T> The Class that should be instanced.
     * @return The object instanced from the File.
     * @throws IOException if there are any Issues with reading or writing to the File
     */
    public static <T> T loadConf(Class<T> tClass, File file) throws IOException, IllegalAccessException, InstantiationException {

        if(file.createNewFile()) { // No file exists. We create a new one with default Stuff and return null
            String json = gson.toJson(parser.parse(gson.toJson(tClass.newInstance())));

            try (PrintWriter out = new PrintWriter(file)) {
                out.println(json);
            }
        } else { // File exists. We read all Data from the file and return an instanced Object c:
            return gson.fromJson(new String(Files.readAllBytes(file.toPath())), tClass);
        }

        return null;
    }

    /**
     * Replaces whole file with new config
     * @param configObject Object instance
     * @param file Filepath to save to
     * @throws IOException if there are any Issues with writing to the File
     */
    public static void updateConf(Object configObject, File file) throws IOException {

        //Save fallback to create a new file if none exists. This should never happen as we only use this for updating the Config. But anyways.
        file.createNewFile();

        String json = gson.toJson(parser.parse(gson.toJson(configObject)));

        try (PrintWriter out = new PrintWriter(file)) {
            //PrintWriter actually replaces the content of a file. If you didn't know ;)
            out.println(json);
        }

    }

}
