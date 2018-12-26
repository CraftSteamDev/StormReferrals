package xyz.pokepalooza.config;

import com.mcsimonflash.sponge.teslalibs.configuration.ConfigHolder;
import com.mcsimonflash.sponge.teslalibs.configuration.ConfigurationException;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import xyz.pokepalooza.StormReferrals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReferralConfig {

    private static final Path directory = Sponge.getConfigManager().getPluginConfig(StormReferrals.instance).getDirectory();
    private static final Path configDirectory = directory.resolve("config");
    private static final Path storageDirectoy = directory.resolve("data");
    public static ConfigHolder config, storage;

    public static void load() {
        try {
            Files.createDirectories(directory);
            Files.createDirectories(storageDirectoy);
            Files.createDirectories(configDirectory);
            config = loadConfig(configDirectory, "configuration.conf", false);
            storage = loadConfig(storageDirectoy, "storage.conf", false);
            loadConfig();
            saveAll();
        } catch (IOException | ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static ConfigHolder loadConfig(Path dir, String name, boolean asset) throws IOException {
        Path path = dir.resolve(name);
        try {
            if (asset) {
                Sponge.getAssetManager().getAsset(StormReferrals.instance, name).get().copyToFile(path);
            } else if (Files.notExists(path)) {
                Files.createFile(path);
            }
            return ConfigHolder.of(HoconConfigurationLoader.builder().setDefaultOptions(ConfigurationOptions.defaults().setShouldCopyDefaults(true)).setPath(path).build());
        } catch (IOException e) {
            StormReferrals.instance.getLogger().error("Error during initializing " + name + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    public static int getMaxPoints() {
        return config.getNode("referral", "maxPoints").getInt();
    }

    public static int getGivenPoint() {
        return config.getNode("referral", "pointsGiven").getInt();
    }

    public static boolean getAnnounce() {
        return config.getNode("referral", "announcePointsOnJoin").getBoolean();
    }

    private static void saveAll() {
        storage.save();
        config.save();
    }

    private static void loadConfig() {
        config.getNode("referral", "pointsGiven").getInt(1);
        config.getNode("referral", "maxPoints").getInt(100);
        config.getNode("referral", "announcePointsOnJoin").getBoolean(true);
        if (config.getNode("tiers").isVirtual()) {
            config.getNode("tiers", "Low", "pointsNeeded").getInt(1);
            config.getNode("tiers", "Low", "numberOfRewardsMin").getInt(1);
            config.getNode("tiers", "Low", "numberOfRewardsMax").getInt(1);
            config.getNode("tiers", "Low", "items", "minecraft:diamond").getInt(1);
        }
    }
}
