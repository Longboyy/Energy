package com.github.longboyy.energy.vote;

import com.github.longboyy.energy.EnergyPlugin;
import com.vexsoftware.votifier.model.Vote;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.playersettings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.playersettings.impl.LongSetting;
import vg.civcraft.mc.civmodcore.util.TextUtil;

import java.util.HashMap;
import java.util.Map;

public class VoteManager {

    private final EnergyPlugin plugin;

    private final Map<String, VotingSite> sites;
    private final Map<String, LongSetting> siteSettings;

    public VoteManager(EnergyPlugin plugin, Map<String, VotingSite> sites){
        this.plugin = plugin;

        this.sites = new HashMap<>(plugin.getConfigManager().getVotingSites());
        this.siteSettings = new HashMap<>();
        for(String s : sites.keySet()){
            LongSetting setting = new LongSetting(plugin, 0L, "Energy vote site " + s, "energyVoteTime"+s);
            PlayerSettingAPI.registerSetting(setting, null);
            siteSettings.put(s, setting);
        }
    }

    public long getLastVote(String serviceName, Player player){
        return siteSettings.get(serviceName).getValue(player);
    }

    public void handleVote(Player player, Vote vote){
        VotingSite site = sites.get(vote.getServiceName());

        //ty essenceglue
        if (site == null) {
            plugin.getLogger()
                    .warning("Received vote from unknown service " + vote.getServiceName());
            player.sendMessage(ChatColor.RED + "You voted on a website not properly supported or setup right now, "
                    + vote.getServiceName());
            return;
        }

        LongSetting cooldownSetting = siteSettings.get(vote.getServiceName());
        long lastVoted = cooldownSetting.getValue(player);
        long now = System.currentTimeMillis();
        long timePassed = now - lastVoted;
        long coolDown = site.getVotingCooldown();
        if (timePassed < coolDown) {
            long remaining = coolDown - timePassed;
            player.sendMessage(
                    ChatColor.RED + "You already voted on this site today, you can receive rewards for it again in "
                            + ChatColor.AQUA + TextUtil.formatDuration(remaining));
            return;
        }
        cooldownSetting.setValue(player, now);
        ItemStack energyItem = plugin.getConfigManager().getEnergyItemStack();
        plugin.getEnergyManager().addEnergy(player, energyItem.getAmount() * site.getRewardMultiplier());
    }


}
