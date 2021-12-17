package com.github.longboyy.energy.vote;

public class VotingSite {

    private final String name;
    private final String votingUrl;
    private final String internalKey;
    private final long votingCooldown;
    private final double rewardMultiplier;

    public VotingSite(String name, String votingUrl, String internalKey, long votingCooldown, double rewardMultiplier) {
        this.name = name;
        this.votingUrl = votingUrl;
        this.internalKey = internalKey;
        this.votingCooldown = votingCooldown;
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getVotingUrl() {
        return votingUrl;
    }

    public String getName() {
        return name;
    }

    public String getInternalKey() {
        return internalKey;
    }

    public long getVotingCooldown() {
        return votingCooldown;
    }

    public double getRewardMultiplier(){
        return rewardMultiplier;
    }

}
