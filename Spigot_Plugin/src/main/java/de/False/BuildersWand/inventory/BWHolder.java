package de.False.BuildersWand.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BWHolder implements InventoryHolder {

	private Inventory inventory;
	private Player player;

	public BWHolder(Player player) {
		this.player = player;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Player getPlayer() {
		return player;
	}

}
