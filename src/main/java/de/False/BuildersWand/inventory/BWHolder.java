package de.False.BuildersWand.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BWHolder implements InventoryHolder {

	private Inventory inventory;

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

}
