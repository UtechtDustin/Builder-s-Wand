package de.False.BuildersWand.Inventories;

import de.False.BuildersWand.utilities.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MainMenu implements Listener {

    private InventoryBuilder invBuilder;

    public MainMenu(InventoryBuilder invBuilder) {
        this.invBuilder = invBuilder;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryName = event.getView().getTopInventory().getTitle();
        int slot = event.getRawSlot();

        if (inventoryName.equals("§9Wands Settings") && event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;
            event.setCancelled(true);
            invBuilder.editorMenu(player);
        }
    }
}
