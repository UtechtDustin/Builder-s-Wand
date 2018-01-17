package de.False.BuildersWand.items;

import de.False.BuildersWand.NMS.NMS;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Wand
{
    private String name;
    private Material material;

    private boolean craftingEnabled;
    private boolean craftingShapeless;
    private List<String> craftingRecipe;
    private HashMap<String, Material> ingredient = new HashMap<>();

    private boolean particleEnabled;
    private String particle;
    private int particleCount;

    private boolean consumeItems;
    private int maxSize;

    private boolean durabilityEnabled;
    private int durability;
    private String durabilityText;

    private boolean inventoryEnabled;
    private int inventorySize;

    private String key;

    private NMS nms;

    public Wand(NMS nms){
        this.nms = nms;
    }

    public ItemStack getRecipeResult()
    {
        ItemStack buildersWand = new ItemStack(getMaterial());
        ItemMeta itemMeta = buildersWand.getItemMeta();
        itemMeta.setDisplayName(getName());

        if(isDurabilityEnabled())
        {
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtil.colorize(getDurabilityText().replace("{durability}", getDurability() + "")));
            itemMeta.setLore(lore);
        }

        buildersWand.setItemMeta(itemMeta);

        return buildersWand;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public boolean isCraftingEnabled()
    {
        return craftingEnabled;
    }

    public void setCraftingEnabled(boolean craftingEnabled)
    {
        this.craftingEnabled = craftingEnabled;
    }

    public boolean isCraftingShapeless()
    {
        return craftingShapeless;
    }

    public void setCraftingShapeless(boolean craftingShapeless)
    {
        this.craftingShapeless = craftingShapeless;
    }

    public List<String> getCraftingRecipe()
    {
        return craftingRecipe;
    }

    public void setCraftingRecipe(List<String> craftingRecipe)
    {
        this.craftingRecipe = craftingRecipe;
    }

    public HashMap<String, Material> getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(HashMap<String, Material> ingredient)
    {
        this.ingredient = ingredient;
    }

    public boolean isParticleEnabled()
    {
        return particleEnabled;
    }

    public void setParticleEnabled(boolean particleEnabled)
    {
        this.particleEnabled = particleEnabled;
    }

    public String getParticle()
    {
        return particle;
    }

    public void setParticle(String particle)
    {
        this.particle = particle;
    }

    public int getParticleCount()
    {
        return particleCount;
    }

    public void setParticleCount(int particleCount)
    {
        this.particleCount = particleCount;
    }

    public boolean isConsumeItems()
    {
        return consumeItems;
    }

    public void setConsumeItems(boolean consumeItems)
    {
        this.consumeItems = consumeItems;
    }

    public int getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

    public boolean isDurabilityEnabled()
    {
        return durabilityEnabled;
    }

    public void setDurabilityEnabled(boolean durabilityEnabled)
    {
        this.durabilityEnabled = durabilityEnabled;
    }

    public int getDurability()
    {
        return durability;
    }

    public void setDurability(int durability)
    {
        this.durability = durability;
    }

    public String getDurabilityText()
    {
        return durabilityText;
    }

    public void setDurabilityText(String durabilityText)
    {
        this.durabilityText = durabilityText;
    }

    public boolean isInventoryEnabled()
    {
        return inventoryEnabled;
    }

    public void setInventoryEnabled(boolean inventoryEnabled)
    {
        this.inventoryEnabled = inventoryEnabled;
    }

    public int getInventorySize()
    {
        return inventorySize;
    }

    public void setInventorySize(int inventorySize)
    {
        this.inventorySize = inventorySize;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
