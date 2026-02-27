package com.zhangsiihanqwq.allayplus;

import com.zhangsiihanqwq.allayplus.util.AllayPlusConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import com.mojang.brigadier.arguments.BoolArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllayPlus implements ModInitializer {
	public static final String MOD_ID = "allay-plus";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("AllayPlus 正在初始化...");

		AllayPlusConfig.load();
		LOGGER.info("AllayPlus 规则: silentResonanceEnabled = " + AllayPlusConfig.silentResonanceEnabled);

			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
				dispatcher.register(CommandManager.literal("allayplus")
						.requires(source -> source.hasPermissionLevel(2)) // OP权限
						.then(CommandManager.literal("silentResonanceEnabled")
								.then(CommandManager.argument("enabled", BoolArgumentType.bool())
										.executes(context -> {
											boolean enabled = BoolArgumentType.getBool(context, "enabled");
											AllayPlusConfig.silentResonanceEnabled = enabled;

											AllayPlusConfig.save();

											Text message = Text.literal("规则 “静音音符盒可与悦灵共振” 已设为 " + (enabled ? "True" : "False"))
													.formatted(enabled ? Formatting.GREEN : Formatting.RED);

											context.getSource().sendFeedback(() -> message, true);
											return 1;
										})
								)
						)
				);
			});
	}
}