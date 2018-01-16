package de.False.BuildersWand.utilities;

import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.manager.WandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryBuilder {

    private WandManager wandManager;

    public InventoryBuilder(WandManager wandManager) {
        this.wandManager = wandManager;
    }

    private void addItem(Inventory inventory, String displayName, Material material, int data, int slot, List<String> lore) {
        ItemStack itemStack = new ItemStack(material, 1, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> displayLore = new ArrayList<>();
        for (String addToLore : lore) {
            displayLore.add(ChatColor.translateAlternateColorCodes('&', addToLore));
        }
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7" + displayName));
        itemMeta.setLore(displayLore);
        itemMeta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }

    public void availableWands(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§9Wands Settings");
        int slot = -1;
        for (Wand wand : wandManager.getWands()) {
            slot++;
            addItem(inventory, wand.getName(), wand.getMaterial(), 0, slot,
                    Arrays.asList("&7Click to edit the", "&7settings of this wand."));
        }
        player.openInventory(inventory);
    }

    public void editorMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§9Editing a Wand");

        addItem(inventory, "&aDisplay Name", Material.BOOK_AND_QUILL, 0, 0,
                Arrays.asList("&7Click to edit the current", "&7name of the wand."));
        addItem(inventory, "&aChange Crafting", Material.WORKBENCH, 0, 1,
                Arrays.asList("&7Click to edit the current", "&7crafting of the wand."));
        addItem(inventory, "&aConsume Items", Material.HOPPER, 0, 2,
                Arrays.asList("&7Click to toggle the Consume", "&7Items Option."));
        addItem(inventory, "&aDurability", Material.IRON_SWORD, 0, 3,
                Arrays.asList("&7Click to edit the durability", "&7of the wand."));
        addItem(inventory, "&aParticles", Material.GLOWSTONE_DUST, 0, 4,
                Arrays.asList("&7Click to change the particles", "&7displayed of the wand."));
        addItem(inventory, "&aStorage", Material.CHEST, 0, 5,
                Arrays.asList("&7Click to toggle/change the storage", "&7size of the wand."));
        addItem(inventory, "&aMax Size", Material.BUCKET, 0, 6,
                Arrays.asList("&7Click to change the max size of ", "&7the selection of the wand."));

        player.openInventory(inventory);
    }

    public void cratingMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, "§9Change Crafting");
        List<Integer> slots = Arrays.asList(12, 13, 14, 21, 22, 23, 25, 30, 31, 32);

        for (int i = 0; i < 45; i++) {
            if (slots.contains(i)) continue;
            addItem(inventory, "", Material.STAINED_GLASS_PANE, 15, i, Arrays.asList(" "));
        }

        // if enabled add this
        addItem(inventory, "&aEnabled", Material.INK_SACK, 10, 10, Arrays.asList("&7Click to Toggle the crafting."));
        //else add this: addItem(inventory, "&aEnabled", Material.INK_SACK, 8, 10, Arrays.asList("&7Click to Toggle the crafting."));
        addItem(inventory, "&eShapeless: "/* BOOLEAN */, Material.WORKBENCH, 10, 28, Arrays.asList("&7Click to Toggle if the recipe.", "&7is shapeless or with shape."));

        player.openInventory(inventory);
    }

    public void durabilityMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, "§9Durability");

        addItem(inventory, "&eAmount", Material.BOOK, 0, 0, Arrays.asList("&7Click to change the amount.", "", "&eCurrent Amount: &f"/* TEXT FROM WAND */));
        addItem(inventory, "&eText", Material.NAME_TAG, 0, 2, Arrays.asList("&7Click to change the lore text.", "", "&eCurrent Text: &f"/* TEXT FROM WAND */));
        // if enabled add this:
        addItem(inventory, "&aEnabled", Material.INK_SACK, 10, 4, Arrays.asList("&7Click to Toggle the durability."));
        //else add this: addItem(inventory, "&aEnabled", Material.INK_SACK, 8, 10, Arrays.asList("&7Click to Toggle the crafting."));

        player.openInventory(inventory);
    }

    public void particleMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, "§9Durability");

        addItem(inventory, "&eCount", Material.NAME_TAG, 0, 0, Arrays.asList("&7Click to change the count.", "", "&eCurrent Count: &f"/* TEXT FROM WAND */));
        addItem(inventory, "&eParticle", Material.GLOWSTONE_DUST, 0, 2, Arrays.asList("&7Click to change the particle.", "", "&eCurrent Particle: &f"/* TEXT FROM WAND */));
        // if enabled add this:
        addItem(inventory, "&aEnabled", Material.INK_SACK, 10, 4, Arrays.asList("&7Click to Toggle the particles."));
        //else add this: addItem(inventory, "&aEnabled", Material.INK_SACK, 8, 10, Arrays.asList("&7Click to Toggle the crafting."));

        player.openInventory(inventory);
    }
}
