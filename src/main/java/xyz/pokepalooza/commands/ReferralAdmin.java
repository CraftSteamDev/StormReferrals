package xyz.pokepalooza.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import xyz.pokepalooza.config.ConfigUtils;
import xyz.pokepalooza.config.ReferralConfig;

public class ReferralAdmin implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = (Player) args.getOne("Player").get();
        int amount = (int) args.getOne("Amount").get();
        if (amount > 0) {
            if (((String) args.getOne("Add/Remove").get()).toLowerCase().equals("add")) {
                if (ConfigUtils.givePoints(player.getUniqueId(), amount)) {
                    src.sendMessage(Text.of(TextColors.GREEN, "You gave " + player.getName() + " " + amount + " points."));
                } else {
                    src.sendMessage(Text.of(TextColors.RED, "The players balance cannot exceed " + ReferralConfig.getMaxPoints() + " points."));
                }
            } else if (((String) args.getOne("Add/Remove").get()).toLowerCase().equals("remove")) {
                if (ConfigUtils.removePoints(player.getUniqueId(), amount)) {
                    src.sendMessage(Text.of(TextColors.GREEN, "You removed " + amount + " points from " + player.getName() + "."));
                } else {
                    src.sendMessage(Text.of(TextColors.RED, "That player does not have that many points!"));
                }
            }
        } else {
            src.sendMessage(Text.of(TextColors.RED, "Amount must be positive."));
        }

        return CommandResult.success();
    }

}
