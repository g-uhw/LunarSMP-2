package light.breeze.items;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static void runCommandAt(Player player, String command) {
        player.getServer().dispatchCommand(player.getServer().getConsoleSender(),"execute at " + player.getName() + " anchored eyes run " + command);
    }
    public static List<LivingEntity> getEntitiesInRadius(Location center, double radius) {
        World world = center.getWorld();
        List<LivingEntity> entitiesInRadius = new ArrayList<>();
        for (LivingEntity entity : world.getLivingEntities()) {
            if (entity.getLocation().distance(center) <= radius) {
                entitiesInRadius.add(entity);
            }
        }
        return entitiesInRadius;
    }

    public static Location parseRelativeLocation(Location location, String relativeCoords) {
        String[] coords = relativeCoords.split(" ");
        double zOffset = parseRelativeCoordinate(location, coords[2]);

        // Apply rotation (yaw and pitch) adjustments
        double yaw = Math.toRadians(location.getYaw());
        double pitch = -Math.toRadians(location.getPitch());

        double xAdjusted = zOffset * (Math.sin(yaw) * -Math.cos(pitch));
        double yAdjusted = zOffset * (Math.sin(pitch));
        double zAdjusted = zOffset * (Math.cos(yaw) * -Math.cos(pitch));

        // Create and return the new location
        return location.clone().add(xAdjusted, yAdjusted, -zAdjusted);
    }

    private static double parseRelativeCoordinate(Location location, String coord) {
        if (coord.equals("^")) {
            return 0;
        } else if (coord.startsWith("^")) {
            double relativeOffset = Double.parseDouble(coord.substring(1));
            return relativeOffset;
        } else {
            return Double.parseDouble(coord);
        }
    }
    public static List<Player> getPlayersInRadius(Location center, double radius) {
        List<Player> playersInRadius = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            double distance = center.distance(playerLocation);
            if (distance <= radius) {
                playersInRadius.add(player);
            }
        }
        return playersInRadius;
    }
}