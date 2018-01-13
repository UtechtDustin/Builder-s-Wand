package de.False.BuildersWand.events;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.Main;
import de.False.BuildersWand.NMS.NMS;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.manager.InventoryManager;
import de.False.BuildersWand.manager.WandManager;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class WandStorageEvents implements Listener
{
    private Main plugin;
    private Config config;
    private NMS nms;
    private WandManager wandManager;
    private InventoryManager inventoryManager;
    private String INVENTORY_NAME;
    public WandStorageEvents(Main plugin, Config config, NMS nms, WandManager wandManager, InventoryManager inventoryManager)
    {
        this.plugin = plugin;
        this.config = config;
        this.nms = nms;
        this.wandManager = wandManager;
        this.inventoryManager = inventoryManager;
        INVENTORY_NAME = MessageUtil.colorize("&1Builders Wand Storage");
    }

    @EventHandler
    public void openInventory(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack mainHand = nms.getItemInHand(player);
        Wand wand = wandManager.getWand(mainHand);
        String uuid = nms.getTag(mainHand, "uuid");
        Action action = event.getAction();
        if (
                wand == null
                || (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK)
                || !nms.isMainHand(event)
                || !player.isSneaking()
        )
        {
            return;
        }

        if(!player.hasPermission("buildersWand.storage"))
        {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }

        ItemStack[] inventoryContent = inventoryManager.getInventory(uuid);
        Inventory storage = Bukkit.createInventory(player, wand.getInventorySize(), INVENTORY_NAME);
        storage.setContents(inventoryContent);
        player.openInventory(storage);
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        InventoryView openInventory = player.getOpenInventory();
        Inventory storage = openInventory.getTopInventory();
        if(storage == null || !storage.getName().equals(INVENTORY_NAME))
        {
            return;
        }

        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        if(!wandManager.isWand(itemStack))
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        Inventory storage = event.getInventory();
        if(storage == null || !storage.getName().equals(INVENTORY_NAME))
        {
            return;
        }

        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || !wandManager.isWand(itemStack))
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void moveBlocksOnly(InventoryClickEvent event)
    {
        Inventory storage = event.getInventory();
        ItemStack itemStack = event.getCurrentItem();
        InventoryAction action = event.getAction();
        if(action == InventoryAction.HOTBAR_SWAP || action == InventoryAction.HOTBAR_MOVE_AND_READD)
        {
            event.setCancelled(true);
        }

        if(
                storage == null
                || !storage.getName().equals(INVENTORY_NAME)
                || itemStack == null
                || itemStack.getType().isBlock()
        )
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event)
    {
        Player player = (Player) event.getPlayer();
        Inventory storage = event.getInventory();
        ItemStack mainHand = nms.getItemInHand(player);
        if(mainHand == null | storage == null || !storage.getName().equals(INVENTORY_NAME))
        {
            return;
        }

        ItemStack[] content = storage.getContents();
        String uuid = nms.getTag(mainHand, "uuid");
        inventoryManager.setInventory(uuid, content);
    }
}