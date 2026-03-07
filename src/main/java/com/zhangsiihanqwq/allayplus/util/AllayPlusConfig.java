package com.zhangsiihanqwq.allayplus.util;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class AllayPlusConfig {
    //静音音符盒可与悦灵共振
    public static boolean silentResonanceEnabled = false;
    //悦灵投掷冷却时间
    public static int throwCooldownTime = -1;
    //悦灵最大听觉距离
    public static int maxHearingDistance = -1;

    // 配置文件路径：config/allay-plus.properties
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("allay-plus.properties");

    public static void save() {
        Properties props = new Properties();
        props.setProperty("silentResonanceEnabled", String.valueOf(silentResonanceEnabled));
        props.setProperty("throwCooldownTime", String.valueOf(throwCooldownTime));
        props.setProperty("maxHearingDistance", String.valueOf(maxHearingDistance));
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
            throwCooldownTime = Integer.parseInt(props.getProperty("throwCooldownTime", "-1"));
            maxHearingDistance = Integer.parseInt(props.getProperty("maxHearingDistance", "-1"));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}