package me.zeroeightsix.kami.module.modules.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@Module.Info(
    name = "Pathfind",
    category = Module.Category.MISC
)
public class Pathfind extends Module {

    public static ArrayList points = new ArrayList();
    static PathPoint to = null;

    public static boolean createPath(PathPoint end) {
        Pathfind.to = end;
        Pathfind.AnchoredWalkNodeProcessor walkNodeProcessor = new Pathfind.AnchoredWalkNodeProcessor(new PathPoint((int) Pathfind.mc.player.posX, (int) Pathfind.mc.player.posY, (int) Pathfind.mc.player.posZ));
        EntityZombie zombie = new EntityZombie(Pathfind.mc.world);

        zombie.setPathPriority(PathNodeType.WATER, 16.0F);
        zombie.posX = Pathfind.mc.player.posX;
        zombie.posY = Pathfind.mc.player.posY;
        zombie.posZ = Pathfind.mc.player.posZ;
        PathFinder finder = new PathFinder(walkNodeProcessor);
        Path path = finder.findPath(Pathfind.mc.world, zombie, new BlockPos(end.x, end.y, end.z), Float.MAX_VALUE);

        zombie.setPathPriority(PathNodeType.WATER, 0.0F);
        if (path == null) {
            Command.sendChatMessage("Failed to create path!");
            return false;
        } else {
            Pathfind.points = new ArrayList(Arrays.asList(path.points));
            return ((PathPoint) Pathfind.points.get(Pathfind.points.size() - 1)).distanceTo(end) <= 1.0F;
        }
    }

    public void onWorldRender(RenderEvent event) {
        if (!Pathfind.points.isEmpty()) {
            GL11.glDisable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glLineWidth(1.5F);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GlStateManager.disableDepth();
            GL11.glBegin(1);
            PathPoint first = (PathPoint) Pathfind.points.get(0);

            GL11.glVertex3d((double) first.x - Pathfind.mc.getRenderManager().renderPosX + 0.5D, (double) first.y - Pathfind.mc.getRenderManager().renderPosY, (double) first.z - Pathfind.mc.getRenderManager().renderPosZ + 0.5D);

            for (int i = 0; i < Pathfind.points.size() - 1; ++i) {
                PathPoint pathPoint = (PathPoint) Pathfind.points.get(i);

                GL11.glVertex3d((double) pathPoint.x - Pathfind.mc.getRenderManager().renderPosX + 0.5D, (double) pathPoint.y - Pathfind.mc.getRenderManager().renderPosY, (double) pathPoint.z - Pathfind.mc.getRenderManager().renderPosZ + 0.5D);
                if (i != Pathfind.points.size() - 1) {
                    GL11.glVertex3d((double) pathPoint.x - Pathfind.mc.getRenderManager().renderPosX + 0.5D, (double) pathPoint.y - Pathfind.mc.getRenderManager().renderPosY, (double) pathPoint.z - Pathfind.mc.getRenderManager().renderPosZ + 0.5D);
                }
            }

            GL11.glEnd();
            GlStateManager.enableDepth();
        }
    }

    public void onUpdate() {
        PathPoint closest = (PathPoint) Pathfind.points.stream().min(Comparator.comparing(apply<invokedynamic>())).orElse((Object) null);

        if (closest != null) {
            if (Pathfind.mc.player.getDistance((double) closest.x, (double) closest.y, (double) closest.z) <= 0.8D) {
                Iterator iterator = Pathfind.points.iterator();

                while (iterator.hasNext()) {
                    if (iterator.next() == closest) {
                        iterator.remove();
                        break;
                    }

                    iterator.remove();
                }

                if (Pathfind.points.size() <= 1 && Pathfind.to != null) {
                    boolean b = createPath(Pathfind.to);
                    boolean flag = Pathfind.points.size() <= 4;

                    if (b && flag || flag) {
                        Pathfind.points.clear();
                        Pathfind.to = null;
                        if (b) {
                            Command.sendChatMessage("Arrived!");
                        } else {
                            Command.sendChatMessage("Can\'t go on: pathfinder has hit dead end");
                        }
                    }
                }

            }
        }
    }

    private static Double lambda$onUpdate$0(PathPoint pathPoint) {
        return Double.valueOf(Pathfind.mc.player.getDistance((double) pathPoint.x, (double) pathPoint.y, (double) pathPoint.z));
    }

    private static class AnchoredWalkNodeProcessor extends WalkNodeProcessor {

        PathPoint from;

        public AnchoredWalkNodeProcessor(PathPoint from) {
            this.from = from;
        }

        public PathPoint getStart() {
            return this.from;
        }

        public boolean getCanEnterDoors() {
            return true;
        }

        public boolean getCanSwim() {
            return true;
        }

        public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
            PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);

            if (pathnodetype == PathNodeType.OPEN && y >= 1) {
                Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
                PathNodeType pathnodetype1 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);

                pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
                if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA) {
                    pathnodetype = PathNodeType.DAMAGE_FIRE;
                }

                if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS) {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS;
                }
            }

            pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
            return pathnodetype;
        }

        protected PathNodeType getPathNodeTypeRaw(IBlockAccess p_189553_1_, int p_189553_2_, int p_189553_3_, int p_189553_4_) {
            BlockPos blockpos = new BlockPos(p_189553_2_, p_189553_3_, p_189553_4_);
            IBlockState iblockstate = p_189553_1_.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            Material material = iblockstate.getMaterial();
            PathNodeType type = block.getAiPathNodeType(iblockstate, p_189553_1_, blockpos);

            return type != null ? type : (material == Material.AIR ? PathNodeType.OPEN : (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY ? (block == Blocks.FIRE ? PathNodeType.DAMAGE_FIRE : (block == Blocks.CACTUS ? PathNodeType.DAMAGE_CACTUS : (block instanceof BlockDoor && material == Material.WOOD && !((Boolean) iblockstate.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_WOOD_CLOSED : (block instanceof BlockDoor && material == Material.IRON && !((Boolean) iblockstate.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_IRON_CLOSED : (block instanceof BlockDoor && ((Boolean) iblockstate.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_OPEN : (block instanceof BlockRailBase ? PathNodeType.RAIL : (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || ((Boolean) iblockstate.getValue(BlockFenceGate.OPEN)).booleanValue()) ? (material == Material.WATER ? PathNodeType.WALKABLE : (material == Material.LAVA ? PathNodeType.LAVA : (block.isPassable(p_189553_1_, blockpos) ? PathNodeType.OPEN : PathNodeType.BLOCKED))) : PathNodeType.FENCE))))))) : PathNodeType.TRAPDOOR));
        }
    }
}
