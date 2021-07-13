package de.False.BuildersWand.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface canBuildHandler {
    boolean canBuild(Player player, Location location);
}
