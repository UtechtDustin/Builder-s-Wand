package de.False.BuildersWand.Inventories;

import de.False.BuildersWand.utilities.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class EditorMenu implements Listener {

    private InventoryBuilder invBuilder;

    public EditorMenu(InventoryBuilder invBuilder) {
        this.invBuilder = invBuilder;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryName = event.getView().getTopInventory().getTitle();
        int slot = event.getRawSlot();

        if (inventoryName.equals("§9Editing a Wand") && event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;
            event.setCancelled(true);
            if (slot == 1) {
                invBuilder.cratingMenu(player);
            }
            if (slot == 3) {
                invBuilder.durabilityMenu(player);
            }
            if (slot == 4) {
                invBuilder.particleMenu(player);
            }
            if (slot == 5) {
                invBuilder.test(player);
            }
        }
    }

}
