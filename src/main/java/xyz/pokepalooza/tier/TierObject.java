package xyz.pokepalooza.tier;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TierObject {

    public int pointsNeeded;
    public Map<ItemType, Integer> rewards;
    public int numberOfRewardsMin;
    public int numberOfRewardsMax;
    public String tierName;

    public TierObject(int pointsNeeded, int numberOfRewardsMin, int numberOfRewardsMax, Map<ItemType, Integer> rewards, String tierNmae) {
        this.pointsNeeded = pointsNeeded;
        this.rewards = rewards;
        this.numberOfRewardsMax = numberOfRewardsMax;
        this.numberOfRewardsMin = numberOfRewardsMin;
        this.tierName = tierNmae;
    }

    public ItemType getReward() {
        List<ItemType> possibleRewards = new ArrayList<>();
        for (ItemType item : rewards.keySet()) {
            int weight = rewards.get(item);
            for (int i = 0; i < weight; i++) {
                possibleRewards.add(item);
            }
        }
        if (possibleRewards.size() > 0) {
            Random random = new Random();
            int randomNumber = random.nextInt(possibleRewards.size());
            return possibleRewards.get(randomNumber);
        }
        return ItemTypes.BAKED_POTATO;
    }

    public List<ItemType> getAllRewards() {
        List<ItemType> types = new ArrayList<>();
        Random random = new Random();
        int number = random.nextInt(this.numberOfRewardsMax - numberOfRewardsMin) + numberOfRewardsMin;
        for (int i = 0; i < number; i++) {
            types.add(getReward());
        }
        return types;
    }
}
