package com.github.longboyy.energy;

import com.github.longboyy.energy.EnergyConfigManager;
import com.github.longboyy.energy.commands.EnergyCommand;
import com.github.longboyy.energy.commands.VoteCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import vg.civcraft.mc.civmodcore.commands.CommandManager;

public class EnergyCommandManager extends CommandManager {
	/**
	 * Creates a new command manager for Aikar based commands and tab completions.
	 *
	 * @param plugin The plugin to bind this manager to.
	 */
	public EnergyCommandManager(@NotNull Plugin plugin) {
		super(plugin);
		init();
	}

	public void registerCommands(){
		registerCommand(new VoteCommand());
		registerCommand(new EnergyCommand());
	}
}
