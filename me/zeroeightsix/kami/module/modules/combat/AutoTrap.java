package me.zeroeightsix.kami.module.modules.combat;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.util.Friends;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Module.Info(
    name = "AutoTrap ",
    category = Module.Category.COMBAT
)
public class AutoTrap extends Module {

    BlockPos abovehead;
    BlockPos aboveheadpartner;
    BlockPos aboveheadpartner2;
    BlockPos aboveheadpartner3;
    BlockPos aboveheadpartner4;
    BlockPos side1;
    BlockPos side2;
    BlockPos side3;
    BlockPos side4;
    BlockPos side11;
    BlockPos side22;
    BlockPos side33;
    BlockPos side44;
    int delay;
    public static EntityPlayer target;
    public static List targets;
    public static float yaw;
    public static float pitch;

    public boolean isInBlockRange(Entity target) {
        return target.getDistance(AutoTrap.mc.player) <= 4.0F;
    }

    public static boolean canBeClicked(BlockPos pos) {
        return AutoTrap.mc.world.getBlockState(pos).getBlock().canCollideCheck(AutoTrap.mc.world.getBlockState(pos), false);
    }

    private static void faceVectorPacket(Vec3d vec) {
        double diffX = vec.x - AutoTrap.mc.player.posX;
        double diffY = vec.y - AutoTrap.mc.player.posY + (double) AutoTrap.mc.player.getEyeHeight();
        double diffZ = vec.z - AutoTrap.mc.player.posZ;
        double dist = (double) MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));

        AutoTrap.mc.getConnection().sendPacket(new Rotation(AutoTrap.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - AutoTrap.mc.player.rotationYaw), AutoTrap.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - AutoTrap.mc.player.rotationPitch), AutoTrap.mc.player.onGround));
    }

    public boolean isValid(EntityPlayer entity) {
        return entity instanceof EntityPlayer && this.isInBlockRange(entity) && entity.getHealth() > 0.0F && !entity.isDead && !entity.getName().startsWith("Body #") && !Friends.isFriend(entity.getName());
    }

    public void loadTargets() {
        Iterator iterator = AutoTrap.mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer) iterator.next();

            if (!(player instanceof EntityPlayerSP)) {
                if (this.isValid(player)) {
                    AutoTrap.targets.add(player);
                } else if (AutoTrap.targets.contains(player)) {
                    AutoTrap.targets.remove(player);
                }
            }
        }

    }

    private boolean isStackObby(ItemStack stack) {
        return stack != null && stack.getItem() == Item.getItemById(49);
    }

    private boolean doesHotbarHaveObby() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = AutoTrap.mc.player.inventoryContainer.getSlot(i).getStack();

            if (stack != null && this.isStackObby(stack)) {
                return true;
            }
        }

        return false;
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static IBlockState getState(BlockPos pos) {
        return AutoTrap.mc.world.getBlockState(pos);
    }

    public static boolean placeBlockLegit(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(AutoTrap.mc.player.posX, AutoTrap.mc.player.posY + (double) AutoTrap.mc.player.getEyeHeight(), AutoTrap.mc.player.posZ);
        Vec3d posVec = (new Vec3d(pos)).add(0.5D, 0.5D, 0.5D);
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing side = aenumfacing[j];
            BlockPos neighbor = pos.offset(side);

            if (canBeClicked(neighbor)) {
                Vec3d hitVec = posVec.add((new Vec3d(side.getDirectionVec())).scale(0.5D));

                if (eyesPos.squareDistanceTo(hitVec) <= 36.0D) {
                    AutoTrap.mc.playerController.processRightClickBlock(AutoTrap.mc.player, AutoTrap.mc.world, neighbor, side.getOpposite(), hitVec, EnumHand.MAIN_HAND);
                    AutoTrap.mc.player.swingArm(EnumHand.MAIN_HAND);

                    try {
                        TimeUnit.MILLISECONDS.sleep(10L);
                    } catch (InterruptedException interruptedexception) {
                        interruptedexception.printStackTrace();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void onUpdate() {
        if (!AutoTrap.mc.player.isHandActive()) {
            if (!this.isValid(AutoTrap.target) || AutoTrap.target == null) {
                this.updateTarget();
            }

            Iterator iterator = AutoTrap.mc.world.playerEntities.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                if (!(player instanceof EntityPlayerSP) && this.isValid(player) && player.getDistance(AutoTrap.mc.player) < AutoTrap.target.getDistance(AutoTrap.mc.player)) {
                    AutoTrap.target = player;
                    return;
                }
            }

            if (this.isValid(AutoTrap.target) && AutoTrap.mc.player.getDistance(AutoTrap.target) < 4.0F) {
                this.trap(AutoTrap.target);
            } else {
                this.delay = 0;
            }

        }
    }

    public static double roundToHalf(double d) {
        return (double) Math.round(d * 2.0D) / 2.0D;
    }

    public void onEnable() {
        this.delay = 0;
    }

    private void trap(EntityPlayer player) {
        if ((double) player.moveForward == 0.0D && (double) player.moveStrafing == 0.0D && (double) player.moveVertical == 0.0D) {
            ++this.delay;
        }

        if ((double) player.moveForward != 0.0D || (double) player.moveStrafing != 0.0D || (double) player.moveVertical != 0.0D) {
            this.delay = 0;
        }

        if (!this.doesHotbarHaveObby()) {
            this.delay = 0;
        }

        if (this.delay == 20 && this.doesHotbarHaveObby()) {
            this.abovehead = new BlockPos(player.posX, player.posY + 2.0D, player.posZ);
            this.aboveheadpartner = new BlockPos(player.posX + 1.0D, player.posY + 2.0D, player.posZ);
            this.aboveheadpartner2 = new BlockPos(player.posX - 1.0D, player.posY + 2.0D, player.posZ);
            this.aboveheadpartner3 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ + 1.0D);
            this.aboveheadpartner4 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ - 1.0D);
            this.side1 = new BlockPos(player.posX + 1.0D, player.posY, player.posZ);
            this.side2 = new BlockPos(player.posX, player.posY, player.posZ + 1.0D);
            this.side3 = new BlockPos(player.posX - 1.0D, player.posY, player.posZ);
            this.side4 = new BlockPos(player.posX, player.posY, player.posZ - 1.0D);
            this.side11 = new BlockPos(player.posX + 1.0D, player.posY + 1.0D, player.posZ);
            this.side22 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ + 1.0D);
            this.side33 = new BlockPos(player.posX - 1.0D, player.posY + 1.0D, player.posZ);
            this.side44 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ - 1.0D);

            for (int i = 36; i < 45; ++i) {
                ItemStack stack = AutoTrap.mc.player.inventoryContainer.getSlot(i).getStack();

                if (stack != null && this.isStackObby(stack)) {
                    int oldSlot = AutoTrap.mc.player.inventory.currentItem;

                    if (AutoTrap.mc.world.getBlockState(this.abovehead).getMaterial().isReplaceable() || AutoTrap.mc.world.getBlockState(this.side1).getMaterial().isReplaceable() || AutoTrap.mc.world.getBlockState(this.side2).getMaterial().isReplaceable() || AutoTrap.mc.world.getBlockState(this.side3).getMaterial().isReplaceable() || AutoTrap.mc.world.getBlockState(this.side4).getMaterial().isReplaceable()) {
                        AutoTrap.mc.player.inventory.currentItem = i - 36;
                        if (AutoTrap.mc.world.getBlockState(this.side1).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side1);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.side2).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side2);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.side3).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side3);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.side4).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side4);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.side11).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side11);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.side22).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side22);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.side33).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side33);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.side44).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.side44);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.aboveheadpartner).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.aboveheadpartner);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.abovehead).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.abovehead);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.aboveheadpartner2).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.aboveheadpartner2);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.aboveheadpartner3).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.aboveheadpartner3);
                        }

                        if (AutoTrap.mc.world.getBlockState(this.aboveheadpartner4).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.aboveheadpartner4);
                        }

                        AutoTrap.mc.player.inventory.currentItem = oldSlot;
                        this.delay = 0;
                        break;
                    }

                    this.delay = 0;
                }

                this.delay = 0;
            }
        }

    }

    public void onDisable() {
        this.delay = 0;
        AutoTrap.yaw = AutoTrap.mc.player.rotationYaw;
        AutoTrap.pitch = AutoTrap.mc.player.rotationPitch;
        AutoTrap.target = null;
    }

    public void updateTarget() {
        Iterator iterator = AutoTrap.mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer) iterator.next();

            if (!(player instanceof EntityPlayerSP) && !(player instanceof EntityPlayerSP) && this.isValid(player)) {
                AutoTrap.target = player;
            }
        }

    }

    public EnumFacing getEnumFacing(float posX, float posY, float posZ) {
        return EnumFacing.getFacingFromVector(posX, posY, posZ);
    }

    public BlockPos getBlockPos(double x, double y, double z) {
        return new BlockPos(x, y, z);
    }
}
