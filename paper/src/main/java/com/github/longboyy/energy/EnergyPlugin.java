package com.github.longboyy.energy;

import com.github.longboyy.energy.listeners.EnergyListener;
import com.github.longboyy.energy.listeners.VoteListener;
import com.github.longboyy.energy.vote.VoteManager;
import org.bukkit.Bukkit;
import vg.civcraft.mc.civmodcore.ACivMod;

public class EnergyPlugin extends ACivMod {

	public static boolean isBanstickEnabled() {
		return banstickEnabled;
	}

	private static boolean banstickEnabled = false;

    private static EnergyPlugin instance;
	public static EnergyPlugin getInstance(){
		return instance;
	}

	private EnergyConfigManager configManager;
	private EnergyManager energyManager;

	private EnergyCommandManager commandManager;
	private VoteManager voteManager;

	private VoteListener voteListener;
	private EnergyListener energyListener;


    @Override
    public void onEnable() {
        // Call CivModCore's enable process, this should be called first before you've done anything else
        super.onEnable();
        instance = this;
        configManager = new EnergyConfigManager(this);
        if(!configManager.parse()){
            this.getLogger().severe("Failed to read config, disabling");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

		if(Bukkit.getPluginManager().isPluginEnabled("BanStick")){
			banstickEnabled = true;
		}

        energyManager = new EnergyManager(this);

        if(Bukkit.getPluginManager().isPluginEnabled("Votifier")){
            // votifier is enabled, create the manager
            voteManager = new VoteManager(this, configManager.getVotingSites());
            voteListener = new VoteListener(voteManager);
            Bukkit.getPluginManager().registerEvents(voteListener, this);
        }else{
            this.getLogger().info("Votifier is not enabled, disabling voting support.");
        }

        energyListener = new EnergyListener(this);
		commandManager = new EnergyCommandManager(this);

        Bukkit.getPluginManager().registerEvents(energyListener, this);
    }

    @Override
    public void onDisable() {
        // Call CivModCore's disable process, this should be called last after you've done everything else
        super.onDisable();
    }

    public EnergyConfigManager getConfigManager(){
        return configManager;
    }

    public EnergyManager getEnergyManager(){
        return energyManager;
    }

    public VoteManager getVoteManager(){
        return voteManager;
    }
}
