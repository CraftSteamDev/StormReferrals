package xyz.pokepalooza;


import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import xyz.pokepalooza.commands.ReferPlayer;
import xyz.pokepalooza.commands.ReferRewards;
import xyz.pokepalooza.commands.ReferralAdmin;
import xyz.pokepalooza.config.ReferralConfig;
import xyz.pokepalooza.events.LogInListener;
import xyz.pokepalooza.tier.TierObject;
import xyz.pokepalooza.utils.TierUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Plugin(
        id = "stormreferrals",
        name = "StormReferrals",
        description = "Rewards players for referring other players, with a customizable reward system.",
        authors = {
                "CraftSteamG"
        },
        version = "1.0.1"
)
public class StormReferrals {

    public static StormReferrals instance;
    //KEY - REFERRED PLAYER / VALUE - PLAYER THAT REFERRED
    public static Map<String, UUID> awaitingPlayers = new HashMap<>();
    public List<TierObject> tiers;
    @Inject
    private Logger logger;

    @Inject
    public StormReferrals(PluginContainer container) {
        StormReferrals.instance = this;
    }

    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        ReferralConfig.load();
        ReferralConfig.storage.getNode("points").getChildrenMap();
        Sponge.getEventManager().registerListeners(this, new LogInListener());
        tiers = TierUtils.registerTiers();
        registerCommands();
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        ReferralConfig.load();
        tiers = TierUtils.registerTiers();
    }

    private void registerCommands() {
        CommandSpec referPlayerCommand = CommandSpec.builder()
                .arguments(GenericArguments.string(Text.of("PlayerToRefer")))
                .executor(new ReferPlayer())
                .permission("stormreferrals.refer.base")
                .build();
        Sponge.getCommandManager().register(this, referPlayerCommand, "refer");
        CommandSpec referRewards = CommandSpec.builder()
                .executor(new ReferRewards())
                .arguments(GenericArguments.string(Text.of("[List/TierName/Points]")))
                .permission("stormreferrals.referrewards.base")
                .build();
        Sponge.getCommandManager().register(this, referRewards, "referrewards");
        CommandSpec adminCommand = CommandSpec.builder()
                .executor(new ReferralAdmin())
                .arguments(GenericArguments.string(Text.of("Add/Remove")),
                        GenericArguments.player(Text.of("Player")),
                        GenericArguments.integer(Text.of("Amount")))
                .permission("stormreferrals.referadmin.base")
                .build();
        Sponge.getCommandManager().register(this, adminCommand, "referAdmin");
    }
}
