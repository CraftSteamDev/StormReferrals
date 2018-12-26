package xyz.pokepalooza.config;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ConfigUtils {

    public static boolean givePoints(UUID uuid, int number) {
        int currentPoint = getPoints(uuid);
        if (currentPoint + number > ReferralConfig.getMaxPoints()) return false;
        ReferralConfig.storage.getNode("points", uuid).setValue(currentPoint + number);
        ReferralConfig.storage.save();
        ReferralConfig.load();
        return true;
    }

    public static int getPoints(UUID uuid) {
        for (ConfigurationNode node : ReferralConfig.storage.getNode("points").getChildrenMap().values()) {
            if (node.getKey().equals(uuid.toString())) {
                return node.getInt();
            }
        }
        return -1;
    }

    public static boolean removePoints(UUID uuid, int points) {
        int currentPoints = getPoints(uuid);
        if (currentPoints < points) return false;
        ReferralConfig.storage.getNode("points", uuid).setValue(currentPoints - points);
        ReferralConfig.storage.save();
        ReferralConfig.load();
        return true;
    }

    public static boolean hasPlayerLoggedIn(String string) {
        try {
            Optional<UserStorageService> userStorageService = Sponge.getServiceManager().provide(UserStorageService.class);
            return userStorageService.get().get(string).isPresent();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Map<Object, ? extends ConfigurationNode> getTierNodes() {
        return ReferralConfig.config.getNode("tiers").getChildrenMap();
    }

    public static Map<ItemType, Integer> getItemsForTier(ConfigurationNode node) {
        Map<ItemType, Integer> itemMap = new HashMap<>();
        for (Object object : node.getChildrenMap().keySet()) {
            if (object instanceof String) {
                ItemType item = (Sponge.getRegistry().getType(ItemType.class, (String) object).orElse(ItemTypes.AIR));
                int weight = node.getChildrenMap().get(object).getInt();
                itemMap.put(item, weight);
            }
        }
        return itemMap;
    }

    public static String getNameFromUUID(UUID uuid) {
        UserStorageService userStorage = Sponge.getServiceManager().provide(UserStorageService.class).get();
        Optional<User> userop = userStorage.get(uuid);
        return userop.map(User::getName).orElse("INVALID USER");
    }
}
