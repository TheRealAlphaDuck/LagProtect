package com.lagprotect;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LagProtect implements ModInitializer {
	public static final String MOD_ID = "lag_protect";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final GameRules.Key<GameRules.IntegerValue> LAG_PROTECT = GameRuleRegistry.register("lagProtect", GameRules.Category.MISC, GameRuleFactory.createIntRule(30, 1, 200));


	@Override
	public void onInitialize() {

	}



}