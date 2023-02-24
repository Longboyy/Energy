package com.github.longboyy.energy;

import com.github.longboyy.energy.vote.VotingSite;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.civmodcore.config.ConfigHelper;
import vg.civcraft.mc.civmodcore.config.ConfigParser;
import vg.civcraft.mc.civmodcore.inventory.items.ItemMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnergyConfigManager extends ConfigParser {

    private ItemMap energyItem;
    private ItemStack energyItemStack;
    private boolean loginRewardEnabled;
    private long loginRewardDelay;
    private double loginMultiplier;
    private boolean votingEnabled;
    private Map<String, VotingSite> votingSites;
    private boolean rewardExiles;
    private double exileMultiplier;
    private boolean rewardPearled;
    private double pearledMultiplier;
    private boolean deathLossEnabled;
    private long deathBanTime;
    private double deathLossMultiplier;
    private String deathBanMessage;
	private double withdrawLimitThreshold;
	private double defaultEnergyAmount;

    public EnergyConfigManager(ACivMod plugin) {
        super(plugin);
    }

    @Override
    protected boolean parseInternal(ConfigurationSection config){
        energyItem = ConfigHelper.parseItemMap(config.getConfigurationSection("energy_item"));
        /*
            I don't know why you can't grab an ItemStack from an item map, but this is literally the easiest
            and cleanest method I could come up with to get the one and only item in the ItemMap parsed from config
         */
        /*
        for(Map.Entry<ItemStack, Integer> entry : energyItem.getEntrySet()){
            energyItemStack = entry.getKey();
            break;
        }
         */

        energyItemStack = energyItem.getItemStackRepresentation().get(0);

        deathLossEnabled = config.isConfigurationSection("death_loss");
        if(deathLossEnabled){
            ConfigurationSection deathSection = config.getConfigurationSection("death_loss");
            deathLossMultiplier = deathSection.getDouble("loss_multiplier", 0.1D);
            deathBanTime = deathSection.isString("ban_below_zero") ? ConfigHelper.parseTime(deathSection.getString("ban_below_zero")) : 0L;
            deathBanMessage = deathSection.getString("ban_message", "");
        }

        loginRewardEnabled = config.isConfigurationSection("login");
        if(loginRewardEnabled) {
            ConfigurationSection loginSection = config.getConfigurationSection("login");
            loginRewardDelay = ConfigHelper.parseTime(loginSection.getString("cooldown_period"));
            loginMultiplier = loginSection.getDouble("multiplier");
        }

        votingSites = new HashMap<String, VotingSite>();
        votingEnabled = config.isConfigurationSection("voting");
        if(votingEnabled){
            ConfigurationSection votingSection = config.getConfigurationSection("voting");
            for(String key : votingSection.getKeys(false)){
                if(votingSection.isConfigurationSection(key)){
                    ConfigurationSection current = votingSection.getConfigurationSection(key);
                    long votingCooldown = ConfigHelper.parseTime(current.getString("cooldown", "20h"));
                    String votingUrl = current.getString("url");
                    String name = current.getString("name");
                    double multiplier = current.getDouble("multiplier", 1.0d);
                    votingSites.put(key, new VotingSite(name, votingUrl, key, votingCooldown, multiplier));
                }
            }
        }

        ConfigurationSection exileSection = config.getConfigurationSection("exiled");
        rewardExiles = exileSection.getBoolean("reward");
        exileMultiplier = exileSection.getDouble("multiplier");
        ConfigurationSection pearlSection = config.getConfigurationSection("pearled");
        rewardPearled = pearlSection.getBoolean("reward");
        pearledMultiplier = pearlSection.getDouble("multiplier");

		ConfigurationSection miscSection = config.getConfigurationSection("misc");
		withdrawLimitThreshold = miscSection.getDouble("withdrawLimitThreshold");
		defaultEnergyAmount = miscSection.getDouble("defaultEnergyAmount");

        return true;
    }

    public ItemMap getEnergyItem(){
        return energyItem;
    }

    public ItemStack getEnergyItemStack(){
        return energyItemStack;
    }

    public boolean isLoginRewardEnabled(){
        return loginRewardEnabled;
    }

    public long getLoginRewardDelay(){
        return loginRewardDelay;
    }

    public double getLoginMultiplier(){
        return loginMultiplier;
    }

    public boolean isVotingEnabled(){
        return votingEnabled;
    }

    public Map<String, VotingSite> getVotingSites(){
        return votingSites;
    }

    public boolean isRewardExiles(){
        return rewardExiles;
    }

    public double getExileMultiplier(){
        return exileMultiplier;
    }

    public boolean isRewardPearled(){
        return rewardPearled;
    }

    public double getPearledMultiplier(){
        return pearledMultiplier;
    }

    public boolean isDeathLossEnabled(){
        return deathLossEnabled;
    }

    public boolean isDeathBanEnabled(){
        return deathLossEnabled && deathBanTime != 0L;
    }

    public double getDeathLossMultiplier(){
        return deathLossMultiplier;
    }

    public long getDeathBanTime(){
        return deathBanTime;
    }

    public String getDeathBanMessage(){
        return deathBanMessage;
    }


	public double getWithdrawLimitThreshold() {
		return withdrawLimitThreshold;
	}

	public double getDefaultEnergyAmount() {
		return defaultEnergyAmount;
	}
}
