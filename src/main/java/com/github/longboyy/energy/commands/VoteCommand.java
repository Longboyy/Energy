package com.github.longboyy.energy.commands;

import com.github.longboyy.energy.EnergyPlugin;
import com.github.longboyy.energy.vote.VoteManager;
import com.github.longboyy.energy.vote.VotingSite;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vg.civcraft.mc.civmodcore.command.CivCommand;
import vg.civcraft.mc.civmodcore.command.StandaloneCommand;
import vg.civcraft.mc.civmodcore.util.TextUtil;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@CivCommand(id = "vote")
public class VoteCommand extends StandaloneCommand {
    @Override
    public boolean execute(CommandSender sender, String[] strings) {
        VoteManager voteMan = EnergyPlugin.getInstance().getVoteManager();
        if (voteMan == null) {
            sender.sendMessage(ChatColor.RED + "Voting is not enabled");
            return true;
        }
        Player p = (Player) sender;
        for (VotingSite site : EnergyPlugin.getInstance().getConfigManager().getVotingSites().values()) {
            long lastVote = voteMan.getLastVote(site.getInternalKey(), p);
            boolean canVote = (System.currentTimeMillis() - lastVote) > site.getVotingCooldown();
            if (canVote) {
                TextComponent text = new TextComponent(
                        ChatColor.GREEN + "Receive rewards for voting on " + site.getName()
                                + ". Click this message to open the link!");
                text.setClickEvent(
                        new ClickEvent(ClickEvent.Action.OPEN_URL, site.getVotingUrl().replace("%PLAYER%", p.getName())));
                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new Text("Click to open the voting link for " + site.getName())));
                p.spigot().sendMessage(text);
            } else {
                long remaining = site.getVotingCooldown() - (System.currentTimeMillis() - lastVote);
                p.sendMessage(
                        ChatColor.YELLOW + "You already voted on " + site.getName() + " and may vote there again in "
                                + TextUtil.formatDuration(remaining));
            }
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String[] strings) {
        return Collections.emptyList();
    }
}
