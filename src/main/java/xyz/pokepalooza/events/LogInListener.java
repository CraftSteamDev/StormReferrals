package xyz.pokepalooza.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import xyz.pokepalooza.StormReferrals;
import xyz.pokepalooza.config.ConfigUtils;
import xyz.pokepalooza.config.ReferralConfig;

import java.util.UUID;

public class LogInListener {

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        if (StormReferrals.awaitingPlayers.keySet().contains(event.getTargetEntity().getName())) {
            Player referred = event.getTargetEntity();
            UUID referee = StormReferrals.awaitingPlayers.get(referred.getName());
            ConfigUtils.givePoints(referred.getUniqueId(), ReferralConfig.getGivenPoint());
            ConfigUtils.givePoints(referee, ReferralConfig.getGivenPoint());
            referred.sendMessage(Text.of(TextColors.GREEN, "You were referred by " + ConfigUtils.getNameFromUUID(referee) + ", and you've received " + ReferralConfig.getGivenPoint() + " referral point(s)!"));
            Sponge.getServer().getPlayer(referee).ifPresent(e -> e.sendMessage(Text.of(TextColors.GREEN, "You referred " + referred.getName() + " and received " + ReferralConfig.getGivenPoint() + " referral point(s)!")));
            StormReferrals.awaitingPlayers.remove(event.getTargetEntity().getName());
        }

        if (ReferralConfig.getAnnounce()) {
            event.getTargetEntity().sendMessage(Text.of(TextColors.GOLD, "You have " + ConfigUtils.getPoints(event.getTargetEntity().getUniqueId()) + " referral points! You can use them by using /referrewards!"));
        }
    }
}
