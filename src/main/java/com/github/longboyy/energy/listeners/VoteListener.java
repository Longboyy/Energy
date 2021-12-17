package com.github.longboyy.energy.listeners;

import com.github.longboyy.energy.EnergyPlugin;
import com.github.longboyy.energy.vote.VoteManager;
import com.github.longboyy.energy.vote.VotingSite;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import vg.civcraft.mc.civmodcore.playersettings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.playersettings.impl.LongSetting;
import vg.civcraft.mc.namelayer.NameAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoteListener implements Listener {

    private final VoteManager voteManager;

    public VoteListener(VoteManager voteManager){
        this.voteManager = voteManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVoteReceive(VotifierEvent event){
        Vote vote = event.getVote();
        UUID playerUUID = NameAPI.getUUID(vote.getUsername());
        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null){
            return;
        }

        voteManager.handleVote(player, vote);
    }

}
