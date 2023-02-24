package com.github.longboyy.energy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.players.settings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.players.settings.impl.DoubleSetting;
import vg.civcraft.mc.civmodcore.players.settings.impl.LongSetting;

public class EnergyManager {

    private final DoubleSetting energyStoreSetting;
    private final LongSetting loginTimeSetting;

    public EnergyManager(EnergyPlugin plugin){
        energyStoreSetting = new DoubleSetting(plugin, plugin.getConfigManager().getDefaultEnergyAmount(), "Player energy store", "energyPlayerStore", new ItemStack(Material.STONE), null);
        PlayerSettingAPI.registerSetting(energyStoreSetting, null);

        loginTimeSetting = new LongSetting(plugin, 0L, "Player login reward", "energyLoginTime");
        PlayerSettingAPI.registerSetting(loginTimeSetting, null);
    }

    public boolean hasEnergy(Player player, double amount){
        return getEnergy(player) - amount >= 0;
    }

    public void addEnergy(Player player, double amount){
        changeEnergyStore(player, amount);
    }

    public void subtractEnergy(Player player, double amount){
        changeEnergyStore(player, -amount);
    }

    public double getEnergy(Player player){
        return energyStoreSetting.getValue(player);
    }

    public void setEnergy(Player player, double amount){
        energyStoreSetting.setValue(player, amount);
    }

    private void changeEnergyStore(Player player, double amount){
        setEnergy(player, getEnergy(player) + amount);
    }

    public void setLoginTime(Player player, long time){
        loginTimeSetting.setValue(player, time);
    }

    public long getLoginTime(Player player){
        return loginTimeSetting.getValue(player);
    }



}
