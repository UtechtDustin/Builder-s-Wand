package de.False.BuildersWand.Inventories;

import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.manager.WandManager;
import de.False.BuildersWand.utilities.InventoryBuilder;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.Map;

public class MainMenu implements Listener {

    private InventoryBuilder inventoryBuilder;
    private WandManager wandManager;
    public static Map<Player, Wand> playerWandMap = new HashMap<>();

    public MainMenu(InventoryBuilder inventoryBuilder, WandManager wandManager) {
        this.inventoryBuilder = inventoryBuilder;
        this.wandManager = wandManager;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryName = event.getView().getTopInventory().getTitle();

        if (inventoryName.equals(MessageUtil.colorize("&9Wands Settings")) && event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;
            event.setCancelled(true);
            playerWandMap.put(player, wandManager.getWand(event.getCurrentItem()));
            player.sendMessage(playerWandMap.toString());
            inventoryBuilder.editorMenu(player);
        }
    }
}
