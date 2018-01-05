package de.False.BuildersWand.items;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.Main;
import de.False.BuildersWand.enums.ParticleShapeHidden;
import de.False.BuildersWand.utilities.ParticleUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Wand implements Listener
{
    private Main plugin;
    private Config config;
    private static String ITEM_NAME = "§3Builders Wand";
    private static Material ITEM_MATERIAL = Material.BLAZE_ROD;
    private HashMap<Block, List<Block>> blockSelection = new HashMap<Block, List<Block>>();
    private HashMap<Block, List<Block>> replacements = new HashMap<Block, List<Block>>();
    private HashMap<Block, List<Block>> tmpReplacements = new HashMap<Block, List<Block>>();

    public Wand(Main plugin, Config config)
    {
        this.plugin = plugin;
        this.config = config;
        startScheduler();

        ITEM_NAME = config.getName();
        ITEM_MATERIAL = config.getMaterial();
    }

    private void startScheduler()
    {
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                blockSelection.clear();
                tmpReplacements.clear();
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    ItemStack mainHand = player.getInventory().getItemInMainHand();
                    Material mainHandMaterial = mainHand.getType();
                    if (mainHandMaterial != ITEM_MATERIAL)
                    {
                        continue;
                    }

                    ItemMeta mainHandItemMeta = mainHand.getItemMeta();
                    String mainHandDisplayName = mainHandItemMeta.getDisplayName();

                    if (
                                    mainHandDisplayName == null
                                    || !mainHandDisplayName.equals(ITEM_NAME)
                                    || player.getLocation().add(0,1,0).getBlock().getType() != Material.AIR
                            )
                    {
                        continue;
                    }
                    Block block = player.getTargetBlock(null, 5);

                    if (block.getType().equals(Material.AIR))
                    {
                        continue;
                    }

                    List<Block> lastBlocks = player.getLastTwoTargetBlocks(null, 5);
                    BlockFace blockFace = lastBlocks.get(1).getFace(lastBlocks.get(0));
                    Block blockNext = block.getRelative(blockFace);

                    if (blockNext == null)
                    {
                        continue;
                    }

                    int itemCount = getItemCount(player, block);

                    blockSelection.put(block, new ArrayList<>());
                    tmpReplacements.put(block, new ArrayList<>());

                    setBlockSelection(blockFace, itemCount, block, block);
                    replacements = tmpReplacements;
                    List<Block> selection = blockSelection.get(block);

                    if(config.isParticleEnabled())
                    {
                        for (Block selectionBlock : selection)
                        {
                            renderBlockOutlines(blockFace, selectionBlock, selection);
                        }
                    }
                }
            }
        }, 0L, 2L);
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Material mainHandMaterial = mainHand.getType();
        ItemMeta mainHandItemMeta = mainHand.getItemMeta();
        String mainHandDisplayName = mainHandItemMeta.getDisplayName();
        if (!mainHandMaterial.equals(ITEM_MATERIAL) || !mainHandDisplayName.equals(ITEM_NAME))
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Material mainHandMaterial = mainHand.getType();
        ItemMeta mainHandItemMeta = mainHand.getItemMeta();

        if (mainHandItemMeta == null || event.getHand() == EquipmentSlot.HAND)
        {
            return;
        }

        String mainHandDisplayName = mainHandItemMeta.getDisplayName();
        if (!mainHandMaterial.equals(ITEM_MATERIAL) || !mainHandDisplayName.equals(ITEM_NAME) || event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        Block against = event.getClickedBlock();
        List<Block> selection = replacements.get(against);

        if(selection == null)
        {
            return;
        }

        Material blockType = against.getType();
        byte blockSubId = against.getData();
        ItemStack itemStack = new ItemStack(against.getType());
        MaterialData materialData = itemStack.getData();
        materialData.setData(blockSubId);
        itemStack.setData(materialData);
        event.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            for (Block selectionBlock : selection)
            {
                selectionBlock.setType(blockType);
                selectionBlock.setData(blockSubId);
            }

        }, 1L);

        if(config.isConsumeItems())
        {
            removeItemStack(itemStack, selection.size(), player);
        }
    }

    private int getItemCount(Player player, Block block)
    {
        int count = 0;
        Inventory inventory = player.getInventory();
        Material blockMaterial = block.getType();
        ItemStack[] itemStacks = inventory.getContents();

        if(player.getGameMode() == GameMode.CREATIVE)
        {
            return Integer.MAX_VALUE;
        }

        for (ItemStack itemStack : itemStacks)
        {
            if (itemStack == null)
            {
                continue;
            }
            Material itemMaterial = itemStack.getType();

            if (!itemMaterial.equals(blockMaterial) || block.getData() != itemStack.getDurability())
            {
                continue;
            }

            count += itemStack.getAmount();
        }

        return count;
    }

    private void removeItemStack(ItemStack itemStack, int amount, Player player)
    {
        Inventory inventory = player.getInventory();
        Material material = itemStack.getType();
        ItemStack[] itemStacks = inventory.getContents();

        if (player.getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        for (ItemStack inventoryItemStack : itemStacks)
        {
            if (inventoryItemStack == null)
            {
                continue;
            }
            Material itemMaterial = inventoryItemStack.getType();
            if (!itemMaterial.equals(material) || itemStack.getData().getData() != inventoryItemStack.getData().getData())
            {
                continue;
            }

            int itemAmount = inventoryItemStack.getAmount();
            if (amount > itemAmount)
            {
                amount -= itemAmount;
                inventoryItemStack.setAmount(0);
            } else
            {
                inventoryItemStack.setAmount(itemAmount - amount);
                return;
            }
        }
    }

    private void setBlockSelection(BlockFace blockFace, int maxLocations, Block startBlock, Block blockToCheck)
    {
        int blockToCheckData = blockToCheck.getData();
        int startBlockData = startBlock.getData();
        Location startLocation = startBlock.getLocation();
        Location checkLocation = blockToCheck.getLocation();
        Material startMaterial = startBlock.getType();
        Material blockToCheckMaterial = blockToCheck.getType();
        Material relativeBlock = blockToCheck.getRelative(blockFace).getType();
        List<Block> selection = blockSelection.get(startBlock);
        List<Block> replacementsList = tmpReplacements.get(startBlock);

        if (startLocation.distance(checkLocation) >= config.getMaxSize() || !(startMaterial.equals(blockToCheckMaterial)) || maxLocations <= selection.size() || blockToCheckData != startBlockData || selection.contains(blockToCheck) || !relativeBlock.equals(Material.AIR))
        {
            return;
        }

        selection.add(blockToCheck);
        replacementsList.add(blockToCheck.getRelative(blockFace));
        Block blockEast = blockToCheck.getRelative(BlockFace.EAST);
        Block blockWest = blockToCheck.getRelative(BlockFace.WEST);
        Block blockNorth = blockToCheck.getRelative(BlockFace.NORTH);
        Block blockSouth = blockToCheck.getRelative(BlockFace.SOUTH);
        Block blockUp = blockToCheck.getRelative(BlockFace.UP);
        Block blockDown = blockToCheck.getRelative(BlockFace.DOWN);
        switch (blockFace)
        {
            case UP:
            case DOWN:
                setBlockSelection(blockFace, maxLocations, startBlock, blockEast);
                setBlockSelection(blockFace, maxLocations, startBlock, blockWest);
                setBlockSelection(blockFace, maxLocations, startBlock, blockNorth);
                setBlockSelection(blockFace, maxLocations, startBlock, blockSouth);
            case EAST:
            case WEST:
                setBlockSelection(blockFace, maxLocations, startBlock, blockNorth);
                setBlockSelection(blockFace, maxLocations, startBlock, blockSouth);
                setBlockSelection(blockFace, maxLocations, startBlock, blockDown);
                setBlockSelection(blockFace, maxLocations, startBlock, blockUp);
            case SOUTH:
            case NORTH:
                setBlockSelection(blockFace, maxLocations, startBlock, blockWest);
                setBlockSelection(blockFace, maxLocations, startBlock, blockEast);
                setBlockSelection(blockFace, maxLocations, startBlock, blockDown);
                setBlockSelection(blockFace, maxLocations, startBlock, blockUp);
        }
    }

    private void renderBlockOutlines(BlockFace blockFace, Block selectionBlock, List<Block> selection)
    {
        List<ParticleShapeHidden> shapes = new ArrayList<>();

        Block blockEast = selectionBlock.getRelative(BlockFace.EAST);
        Block blockWest = selectionBlock.getRelative(BlockFace.WEST);
        Block blockNorth = selectionBlock.getRelative(BlockFace.NORTH);
        Block blockSouth = selectionBlock.getRelative(BlockFace.SOUTH);
        Block blockUp = selectionBlock.getRelative(BlockFace.UP);
        Block blockDown = selectionBlock.getRelative(BlockFace.DOWN);
        Block blockNorthWest = selectionBlock.getRelative(BlockFace.NORTH_WEST);
        Block blockNorthEast = selectionBlock.getRelative(BlockFace.NORTH_EAST);
        Block blockSouthEast = selectionBlock.getRelative(BlockFace.SOUTH_EAST);
        Block blockSouthWest = selectionBlock.getRelative(BlockFace.SOUTH_WEST);
        Block blockDownEast = selectionBlock.getRelative(1, -1, 0);
        Block blockUpEast = selectionBlock.getRelative(1, 1, 0);
        Block blockDownWest = selectionBlock.getRelative(-1, -1, 0);
        Block blockUpWest = selectionBlock.getRelative(-1, 1, 0);
        Block blockDownSouth = selectionBlock.getRelative(0, -1, 1);
        Block blockUpSouth = selectionBlock.getRelative(0, 1, 1);
        Block blockDownNorth = selectionBlock.getRelative(0, -1, -1);
        Block blockUpNorth = selectionBlock.getRelative(0, 1, -1);

        Boolean blockEastContains = selection.contains(blockEast);
        Boolean blockWestContains = selection.contains(blockWest);
        Boolean blockNorthContains = selection.contains(blockNorth);
        Boolean blockSouthContains = selection.contains(blockSouth);
        Boolean blockUpContains = selection.contains(blockUp);
        Boolean blockDownContains = selection.contains(blockDown);
        Boolean blockNorthWestContains = selection.contains(blockNorthWest);
        Boolean blockNorthEastContains = selection.contains(blockNorthEast);
        Boolean blockSouthEastContains = selection.contains(blockSouthEast);
        Boolean blockSouthWestContains = selection.contains(blockSouthWest);
        Boolean blockDownEastContains = selection.contains(blockDownEast);
        Boolean blockUpEastContains = selection.contains(blockUpEast);
        Boolean blockDownWestContains = selection.contains(blockDownWest);
        Boolean blockUpWestContains = selection.contains(blockUpWest);
        Boolean blockDownSouthContains = selection.contains(blockDownSouth);
        Boolean blockUpSouthContains = selection.contains(blockUpSouth);
        Boolean blockDownNorthContains = selection.contains(blockDownNorth);
        Boolean blockUpNorthContains = selection.contains(blockUpNorth);

        if (blockEastContains)
        {
            shapes.add(ParticleShapeHidden.EAST);
        }
        if (blockWestContains)
        {
            shapes.add(ParticleShapeHidden.WEST);
        }
        if (blockNorthContains)
        {
            shapes.add(ParticleShapeHidden.NORTH);
        }
        if (blockSouthContains)
        {
            shapes.add(ParticleShapeHidden.SOUTH);
        }
        if (blockUpContains)
        {
            shapes.add(ParticleShapeHidden.UP);
        }
        if (blockDownContains)
        {
            shapes.add(ParticleShapeHidden.DOWN);
        }
        if (blockNorthWestContains)
        {
            shapes.add(ParticleShapeHidden.NORTH_WEST);
        }
        if (blockNorthEastContains)
        {
            shapes.add(ParticleShapeHidden.NORTH_EAST);
        }
        if (blockSouthEastContains)
        {
            shapes.add(ParticleShapeHidden.SOUTH_EAST);
        }
        if (blockSouthWestContains)
        {
            shapes.add(ParticleShapeHidden.SOUTH_WEST);
        }
        if (blockDownEastContains)
        {
            shapes.add(ParticleShapeHidden.DOWN_EAST);
        }
        if (blockUpEastContains)
        {
            shapes.add(ParticleShapeHidden.UP_EAST);
        }
        if (blockDownWestContains)
        {
            shapes.add(ParticleShapeHidden.DOWN_WEST);
        }
        if (blockUpWestContains)
        {
            shapes.add(ParticleShapeHidden.UP_WEST);
        }
        if (blockDownSouthContains)
        {
            shapes.add(ParticleShapeHidden.DOWN_SOUTH);
        }
        if (blockUpSouthContains)
        {
            shapes.add(ParticleShapeHidden.UP_SOUTH);
        }
        if (blockDownNorthContains)
        {
            shapes.add(ParticleShapeHidden.DOWN_NORTH);
        }
        if (blockUpNorthContains)
        {
            shapes.add(ParticleShapeHidden.UP_NORTH);
        }

        Particle particle = config.getParticle();
        int particleAmount = config.getParticleCount();
        ParticleUtil.drawBlockOutlines(blockFace, shapes, selectionBlock.getRelative(blockFace).getLocation(), particle, particleAmount);
    }

    public static ItemStack getRecipeResult()
    {
        ItemStack buildersWand = new ItemStack(ITEM_MATERIAL);
        ItemMeta itemMeta = buildersWand.getItemMeta();
        itemMeta.setDisplayName(ITEM_NAME);
        buildersWand.setItemMeta(itemMeta);

        return buildersWand;
    }
}