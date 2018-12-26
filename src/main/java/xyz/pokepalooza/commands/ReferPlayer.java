package xyz.pokepalooza.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import xyz.pokepalooza.StormReferrals;
import xyz.pokepalooza.config.ConfigUtils;
import xyz.pokepalooza.config.ReferralConfig;

public class ReferPlayer implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            String personToRefer = (String) args.getOne(Text.of("PlayerToRefer")).get();
            if (ConfigUtils.hasPlayerLoggedIn(personToRefer)) {
                src.sendMessage(Text.of(TextColors.RED, "That player has already logged into the server."));
            } else if (ConfigUtils.getPoints(((Player) src).getUniqueId()) >= ReferralConfig.getMaxPoints()) {
                src.sendMessage(Text.of(TextColors.RED, "You have reached the limit of referral points! You must spend some before you refer another player."));
            } else {
                for (String s : StormReferrals.awaitingPlayers.keySet()) {
                    if (StormReferrals.awaitingPlayers.get(s).equals(((Player) src).getUniqueId())) {
                        src.sendMessage(Text.of(TextColors.RED, "You've overwritten your pending referral. Now referring " + personToRefer));
                        StormReferrals.awaitingPlayers.remove(s);
                        StormReferrals.awaitingPlayers.put(s, ((Player) src).getUniqueId());
                        return CommandResult.success();
                    }
                }
                src.sendMessage(Text.of(TextColors.GREEN, "You have referred " + personToRefer + ". When that player logs in, you both will receive rewards."));
                StormReferrals.awaitingPlayers.put(personToRefer, ((Player) src).getUniqueId());
            }
        } else {
            src.sendMessage(Text.of(TextColors.RED, "This command can only be run by players."));
        }
        return CommandResult.success();
    }
}
