package de.False.BuildersWand.utilities;
import de.False.BuildersWand.NMS.NMS;
import de.False.BuildersWand.enums.ParticleShapeHidden;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.List;

public class ParticleUtil
{
    private NMS nms;

    public ParticleUtil(NMS nms)
    {
        this.nms = nms;
    }

    private void drawLine(String particle, Location loc1, Location loc2, int particleAmount)
    {
        Vector loc1Vector = loc1.toVector();
        Vector loc2Vector = loc2.toVector();

        double x = loc1Vector.getX();
        double y = loc1Vector.getY();
        double z = loc1Vector.getZ();
        int distance = (int) loc1.distance(loc2);
        Vector difference = loc1Vector.subtract(loc2Vector);
        int amount =  distance <= 1 ? particleAmount : distance * particleAmount;
        double xIncrement = (difference.getX() / amount);
        double yIncrement = (difference.getY() / amount);
        double zIncrement = (difference.getZ() / amount);
        for (int i = 0; i < amount; i++)
        {
            Location location = new Location(loc1.getWorld(), x -= xIncrement, y -= yIncrement, z -= zIncrement);
            nms.spawnParticle(particle, location);
        }
    }

    public void drawBlockOutlines(BlockFace blockFace, List<ParticleShapeHidden> shapes, Location location, String particle, int particleAmount)
    {
        Location loc1Clone = location.clone();
        Location loc2Clone = location.clone();

        loc2Clone.add(0.2, 0,0.2);
        loc1Clone.add(0.95, 0.75, 0.95);
        if(blockFace == BlockFace.UP || blockFace == BlockFace.DOWN)
        {
            drawBlockOutlinesHorizontal(shapes, particle, particleAmount, loc1Clone, loc2Clone);
        }
        else if(blockFace == BlockFace.SOUTH || blockFace == BlockFace.NORTH)
        {
            drawBlockOutlinesVerticalSouthNorth(shapes, particle, particleAmount, loc1Clone, loc2Clone);
        }
        else
        {
            drawBlockOutlinesVerticalEastWest(shapes, particle, particleAmount, loc1Clone, loc2Clone);
        }
    }

    private void drawBlockOutlinesVerticalSouthNorth(List<ParticleShapeHidden> shapes, String particle, int particleAmount, Location location1, Location location2)
    {
        double loc1X = location1.getX();
        double loc1Y = location1.getY();
        double loc1Z = location1.getZ();

        double loc2X = location2.getX();
        double loc2Y = location2.getY();
        double loc2Z = location2.getZ();

        World world = location1.getWorld();

        if(!shapes.contains(ParticleShapeHidden.WEST) && !shapes.contains(ParticleShapeHidden.DOWN))
        {
            drawLine(particle, new Location(world, loc2X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.WEST) && shapes.contains(ParticleShapeHidden.DOWN) && !shapes.contains(ParticleShapeHidden.DOWN_WEST))
        {
            drawLine(particle, new Location(world, loc2X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.WEST) && !shapes.contains(ParticleShapeHidden.UP))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc2Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.WEST) && shapes.contains(ParticleShapeHidden.UP) && !shapes.contains(ParticleShapeHidden.UP_WEST))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.EAST) && !shapes.contains(ParticleShapeHidden.DOWN))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc1X, loc2Y, loc1Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.EAST) && shapes.contains(ParticleShapeHidden.DOWN) && !shapes.contains(ParticleShapeHidden.DOWN_EAST))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc1Z), new Location(world, loc1X, loc2Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.EAST) && !shapes.contains(ParticleShapeHidden.UP))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.EAST) && shapes.contains(ParticleShapeHidden.UP) && !shapes.contains(ParticleShapeHidden.UP_EAST))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }

        loc2Y -= 0.1;
        loc1Y += 0.1;
        if(!shapes.contains(ParticleShapeHidden.WEST))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc2X, loc1Y, loc2Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.EAST))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc2Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }

        loc2Y += 0.1;
        loc1Y -= 0.1;
        loc2X -= 0.1;
        loc1X += 0.1;
        if(!shapes.contains(ParticleShapeHidden.UP))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc2X, loc1Y, loc2Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.DOWN))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc2X, loc2Y, loc2Z), new Location(world, loc1X, loc2Y, loc2Z), particleAmount);
        }
    }

    private void drawBlockOutlinesVerticalEastWest(List<ParticleShapeHidden> shapes, String particle, int particleAmount, Location location1, Location location2)
    {
        double loc1X = location1.getX();
        double loc1Y = location1.getY();
        double loc1Z = location1.getZ();

        double loc2X = location2.getX();
        double loc2Y = location2.getY();
        double loc2Z = location2.getZ();

        World world = location1.getWorld();

        if(!shapes.contains(ParticleShapeHidden.NORTH) && !shapes.contains(ParticleShapeHidden.DOWN))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.NORTH) && shapes.contains(ParticleShapeHidden.DOWN) && !shapes.contains(ParticleShapeHidden.DOWN_NORTH))
        {
            drawLine(particle, new Location(world, loc2X, loc2Y, loc2Z), new Location(world, loc1X, loc2Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.NORTH) && !shapes.contains(ParticleShapeHidden.UP))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc2Z), new Location(world, loc2X, loc1Y, loc2Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.NORTH) && shapes.contains(ParticleShapeHidden.UP) && !shapes.contains(ParticleShapeHidden.UP_NORTH))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc2Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.SOUTH) && !shapes.contains(ParticleShapeHidden.DOWN))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.SOUTH) && shapes.contains(ParticleShapeHidden.DOWN) && !shapes.contains(ParticleShapeHidden.DOWN_SOUTH))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.SOUTH) && !shapes.contains(ParticleShapeHidden.UP))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc1Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.SOUTH) && shapes.contains(ParticleShapeHidden.UP) && !shapes.contains(ParticleShapeHidden.UP_SOUTH))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc1Z), particleAmount);
        }

        loc2Y -= 0.1;
        loc1Y += 0.1;
        if(!shapes.contains(ParticleShapeHidden.NORTH))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
            drawLine(particle, new Location(world, loc2X, loc1Y, loc2Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.SOUTH))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc2Y, loc1Z), particleAmount);
        }

        loc2Y += 0.1;
        loc1Y -= 0.1;
        loc2Z -= 0.1;
        loc1Z += 0.1;
        if(!shapes.contains(ParticleShapeHidden.DOWN))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc1X, loc2Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc2X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.UP))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc2Z), particleAmount);
        }
    }

    private void drawBlockOutlinesHorizontal(List<ParticleShapeHidden> shapes, String particle, int particleAmount, Location location1, Location location2)
    {
        double loc1X = location1.getX();
        double loc1Y = location1.getY();
        double loc1Z = location1.getZ();

        double loc2X = location2.getX();
        double loc2Y = location2.getY();
        double loc2Z = location2.getZ();

        World world = location1.getWorld();

        if(!shapes.contains(ParticleShapeHidden.SOUTH) && !shapes.contains(ParticleShapeHidden.EAST))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc2Y, loc1Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.SOUTH) && shapes.contains(ParticleShapeHidden.EAST) && !shapes.contains(ParticleShapeHidden.SOUTH_EAST))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc2Y, loc1Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.SOUTH) && !shapes.contains(ParticleShapeHidden.WEST))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.SOUTH) && shapes.contains(ParticleShapeHidden.WEST) && !shapes.contains(ParticleShapeHidden.SOUTH_WEST))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.WEST) && !shapes.contains(ParticleShapeHidden.NORTH))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc2Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.WEST) && shapes.contains(ParticleShapeHidden.NORTH) && !shapes.contains(ParticleShapeHidden.NORTH_WEST))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc2Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.EAST) && !shapes.contains(ParticleShapeHidden.NORTH))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }
        if(shapes.contains(ParticleShapeHidden.EAST) && shapes.contains(ParticleShapeHidden.NORTH) && !shapes.contains(ParticleShapeHidden.NORTH_EAST))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }

        loc2X -= 0.1;
        loc1X += 0.1;
        if(!shapes.contains(ParticleShapeHidden.SOUTH))
        {
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc1X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc1Z), particleAmount);
        }
        if(!shapes.contains(ParticleShapeHidden.NORTH))
        {
            drawLine(particle, new Location(world, loc2X, loc1Y, loc2Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
        }

        loc2X += 0.1;
        loc1X -= 0.1;
        loc2Z -= 0.1;
        loc1Z += 0.1;
        if(!shapes.contains(ParticleShapeHidden.EAST))
        {
            drawLine(particle, new Location(world, loc1X, loc2Y, loc2Z), new Location(world, loc1X, loc2Y, loc1Z), particleAmount);
            drawLine(particle, new Location(world, loc1X, loc1Y, loc1Z), new Location(world, loc1X, loc1Y, loc2Z), particleAmount);
        }

        if(!shapes.contains(ParticleShapeHidden.WEST))
        {
            drawLine(particle, new Location(world, loc2X, loc2Y, loc1Z), new Location(world, loc2X, loc2Y, loc2Z), particleAmount);
            drawLine(particle, new Location(world, loc2X, loc1Y, loc1Z), new Location(world, loc2X, loc1Y, loc2Z), particleAmount);
        }
    }
}
