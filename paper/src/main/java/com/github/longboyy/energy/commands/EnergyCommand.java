package com.github.longboyy.energy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import com.github.longboyy.energy.EnergyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.inventory.items.ItemMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EnergyCommand extends BaseCommand {

    @CommandAlias("energy")
	@Description("View how much energy you have stored")
    public boolean execute(CommandSender sender, String[] args){
        Player player = (Player)sender;

        if(args.length > 0){
            int withdrawAmount = parseInt(args[0]);
            if(withdrawAmount <= 0){
                return false;
            }

            if(!EnergyPlugin.getInstance().getEnergyManager().hasEnergy(player, withdrawAmount)){
                player.sendMessage(ChatColor.GREEN + "You don't have enough Energy to do that!");
                return true;
            }

			if((EnergyPlugin.getInstance().getEnergyManager().getEnergy(player) - withdrawAmount) < EnergyPlugin.getInstance().getConfigManager().getWithdrawLimitThreshold()){
				player.sendMessage(ChatColor.GREEN + "You can't go below the withdraw threshold!");
				return true;
			}

            ItemMap energyMap = EnergyPlugin.getInstance().getConfigManager().getEnergyItem().clone();
            energyMap.multiplyContent(withdrawAmount);

            if(energyMap.fitsIn(player.getInventory())){
                for(ItemStack itemStack : energyMap.getItemStackRepresentation()){
                    HashMap<Integer, ItemStack> notAdded = player.getInventory().addItem(itemStack);
                    for (ItemStack toDrop : notAdded.values()) {
                        player.sendMessage(ChatColor.GREEN + "Your inventory was too full, dropping Energy on the ground.");
                        player.getWorld().dropItemNaturally(player.getLocation(), toDrop);
                    }
                }
                EnergyPlugin.getInstance().getEnergyManager().subtractEnergy(player, withdrawAmount);
                double newEnergy = EnergyPlugin.getInstance().getEnergyManager().getEnergy(player);
                player.sendMessage(ChatColor.GREEN + "Your new Energy balance is " + ChatColor.AQUA +  String.format("%.1f", newEnergy) +
                        ChatColor.GREEN + " Energy");
                return true;
            }else{
                player.sendMessage(ChatColor.GREEN + "Your inventory is too full to do that, empty a few slots first.");
                return true;
            }

        }else{
            double energy = EnergyPlugin.getInstance().getEnergyManager().getEnergy(player);
            player.sendMessage(ChatColor.GREEN + "You currently have " + ChatColor.AQUA + String.format("%.1f", energy) +
                    ChatColor.GREEN + " Energy");
        }

        return true;
    }

    private int parseInt(String input){
        try{
            int number = Integer.parseInt(input);
            return number;
        }catch(NumberFormatException e){
            return -1;
        }
    }

}
