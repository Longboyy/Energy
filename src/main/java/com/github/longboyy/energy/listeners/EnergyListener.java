package com.github.longboyy.energy.listeners;

import com.github.longboyy.energy.EnergyManager;
import com.github.longboyy.energy.EnergyPlugin;
import com.programmerdan.minecraft.banstick.handler.BanHandler;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;

import java.util.*;

public class EnergyListener implements Listener {

    private final EnergyPlugin plugin;

    public EnergyListener(EnergyPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnergyConsumed(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        ItemStack eatenItem = event.getItem();
        ItemStack energyItem = plugin.getConfigManager().getEnergyItemStack();
        if(!energyItem.getType().isEdible() || eatenItem.getType() != energyItem.getType()){
            return;
        }

        event.setCancelled(true);

        int depositAmount;

        ItemStack itemStack = plugin.getConfigManager().getEnergyItemStack().clone();
        itemStack.setAmount(1);
        ItemMap energyMap = new ItemMap(itemStack);
        if(player.isSneaking()){
            depositAmount = eatenItem.getAmount();
            energyMap.multiplyContent(depositAmount);
        }else{
            depositAmount = 1;
        }

        for(ItemStack is : energyMap.getItemStackRepresentation()){
            //plugin.getLogger().info(is.toString());
            player.getInventory().removeItem(is);
        }

        plugin.getEnergyManager().addEnergy(player, depositAmount);
        double newEnergy = plugin.getEnergyManager().getEnergy(player);

        player.sendMessage(ChatColor.GREEN + "Your new Energy balance is " + ChatColor.AQUA +  String.format("%.1f", newEnergy) +
                ChatColor.GREEN + " Energy");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(!EnergyPlugin.getInstance().getConfigManager().isDeathLossEnabled()){
            return;
        }

        EnergyManager energyManager = plugin.getEnergyManager();
        Player player = event.getEntity();


        double playerEnergy = energyManager.getEnergy(player);
        double lostEnergy = Math.round(Math.max(1D, playerEnergy * plugin.getConfigManager().getDeathLossMultiplier()));

        plugin.getLogger().info("Player " + player.getName() + " died, dropping " + lostEnergy);

        ItemMap energyDrop = plugin.getConfigManager().getEnergyItem().clone();
        energyDrop.multiplyContent(lostEnergy);

        for(ItemStack is : energyDrop.getItemStackRepresentation()){
            player.getWorld().dropItemNaturally(player.getLocation(), is);
        }

        if(energyManager.hasEnergy(player, lostEnergy)){
            energyManager.subtractEnergy(player, lostEnergy);
            player.sendMessage(ChatColor.GREEN + "You lost " + ChatColor.AQUA + lostEnergy
                    + ChatColor.GREEN + " Energy");
        }else{
            energyManager.setEnergy(player, 0D);
            if(plugin.getConfigManager().isDeathBanEnabled()){
                // ban the player using banstick
                Date date = new Date(System.currentTimeMillis() + plugin.getConfigManager().getDeathBanTime());
                BanHandler.doUUIDBan(player.getUniqueId(), plugin.getConfigManager().getDeathBanMessage(), date, false);
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();
        if(plugin.getEnergyManager().getLoginTime(player) <= System.currentTimeMillis()){
            //give daily reward
            double amount = Math.round(plugin.getConfigManager().getEnergyItemStack().getAmount() * plugin.getConfigManager().getLoginMultiplier());
            plugin.getEnergyManager().addEnergy(player, amount);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage(ChatColor.GREEN + "You received a login bonus of " + ChatColor.AQUA
                        + String.format("%.1f", amount) + ChatColor.GREEN + " Energy");
            }, 20L);

            long endTime = System.currentTimeMillis() + plugin.getConfigManager().getLoginRewardDelay();
            plugin.getEnergyManager().setLoginTime(player, endTime);
        }
    }

}
