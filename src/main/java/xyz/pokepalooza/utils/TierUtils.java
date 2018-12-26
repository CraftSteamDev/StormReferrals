package xyz.pokepalooza.utils;

import ninja.leaping.configurate.ConfigurationNode;
import xyz.pokepalooza.config.ConfigUtils;
import xyz.pokepalooza.tier.TierObject;

import java.util.ArrayList;
import java.util.List;

public class TierUtils {


    public static List<TierObject> registerTiers() {
        List<TierObject> tiers = new ArrayList<>();
        for (ConfigurationNode node : ConfigUtils.getTierNodes().values()) {
            TierObject object = new TierObject(node.getNode("pointsNeeded").getInt(), node.getNode("numberOfRewardsMin").getInt(), node.getNode("numberOfRewardsMax").getInt(), ConfigUtils.getItemsForTier(node.getNode("items")), (String) node.getKey());
            tiers.add(object);
        }
        return tiers;
    }
}
