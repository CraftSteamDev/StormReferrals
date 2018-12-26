package xyz.pokepalooza.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import xyz.pokepalooza.StormReferrals;
import xyz.pokepalooza.config.ConfigUtils;
import xyz.pokepalooza.tier.TierObject;

import java.util.HashMap;
import java.util.Map;

public class ReferRewards implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {

            if (((String) args.getOne(Text.of("[List/TierName/Points]")).get()).toLowerCase().equals("list")) {
                for (TierObject tierObject : StormReferrals.instance.tiers) {
                    String tierRewards = "";
                    for (ItemType type : tierObject.rewards.keySet()) {
                        tierRewards = tierRewards + type.getTranslation().get() + ", ";
                    }
                    tierRewards = tierRewards.trim().substring(0, tierRewards.length() - 2);
                    src.sendMessage(Text.of(
                            TextColors.GREEN, "Tier Name: " + tierObject.tierName + "\n",
                            TextColors.GOLD, "Required Points: " + tierObject.pointsNeeded + "\n",
                            TextColors.GOLD, "Number of Rewards Given: " + tierObject.numberOfRewardsMin + "-" + tierObject.numberOfRewardsMax + "\n",
                            TextColors.AQUA, "Possible Rewards: " + tierRewards
                    ));
                }
            } else if (((String) args.getOne(Text.of("[List/TierName/Points]")).get()).toLowerCase().equals("points")) {
                src.sendMessage(Text.of(TextColors.GREEN, "You have " + ConfigUtils.getPoints(((Player) src).getUniqueId()) + " points."));
            } else {
                for (TierObject tierObject : StormReferrals.instance.tiers) {
                    if (tierObject.tierName.toLowerCase().equals(((String) args.getOne(Text.of("[List/TierName/Points]")).get()).toLowerCase())) {
                        if (ConfigUtils.removePoints(((Player) src).getUniqueId(), tierObject.pointsNeeded)) {
                            Map<String, Integer> map = new HashMap<>();
                            for (ItemType type : tierObject.getAllRewards()) {
                                ItemStack stack = ItemStack.of(type, 1);
                                ((Player) src).getInventory().offer(stack);
                                if (map.containsKey(type.getTranslation().get())) {
                                    int times = map.get(type.getTranslation().get());
                                    map.remove(type.getTranslation().get());
                                    map.put(type.getTranslation().get(), times + 1);
                                } else {
                                    map.put(type.getTranslation().get(), 1);
                                }
                            }
                            StringBuilder builder = new StringBuilder();
                            for (String s : map.keySet()) {
                                builder.append(s).append("(").append(map.get(s)).append(")").append(", ");
                            }
                            String itemString = builder.substring(0, builder.length() - 2);
                            src.sendMessage(Text.of(TextColors.GREEN, "Items Received from tier '" + tierObject.tierName + "'\n", TextColors.AQUA, itemString));
                            return CommandResult.success();
                        } else {
                            src.sendMessage(Text.of(TextColors.RED, "You do not have the required amount of points to get rewards in this tier."));
                            return CommandResult.success();
                        }
                    }
                }
                src.sendMessage(Text.of(TextColors.RED, "No tiers exist with that name!"));
            }
        } else {
            src.sendMessage(Text.of(TextColors.RED, "This command must be executed by players."));
        }
        return CommandResult.success();
    }
}

