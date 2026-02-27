package com.zhangsiihanqwq.allayplus.util;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class AllayPlusConfig {
    public static boolean silentResonanceEnabled = false;

    // 配置文件路径：config/allay-plus.properties
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("allay-plus.properties");

    public static void save() {
        Properties props = new Properties();
        props.setProperty("silentResonanceEnabled", String.valueOf(silentResonanceEnabled));
        try (OutputStream out = new FileOutputStream(CONFIG_FILE.toFile())) {
            props.store(out, "AllayPlus Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!CONFIG_FILE.toFile().exists()) {
            save();
            return;
        }
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(CONFIG_FILE.toFile())) {
            props.load(in);
            silentResonanceEnabled = Boolean.parseBoolean(props.getProperty("silentResonanceEnabled", "false"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}