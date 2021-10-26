package me.zeroeightsix.kami.module.modules.movement;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.KamiEvent;
import me.zeroeightsix.kami.event.events.AddCollisionBoxToListEvent;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

@Module.Info(
    name = "Jesus",
    description = "Allows you to walk on water",
    category = Module.Category.MOVEMENT
)
public class Jesus extends Module {

    private static final AxisAlignedBB WATER_WALK_AA = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.99D, 1.0D);
    @EventHandler
    Listener addCollisionBoxToListEventListener = new Listener((event) -> {
        if (Jesus.mc.player != null && event.getBlock() instanceof BlockLiquid && (EntityUtil.isDrivenByPlayer(event.getEntity()) || event.getEntity() == Jesus.mc.player) && !(event.getEntity() instanceof EntityBoat) && !Jesus.mc.player.isSneaking() && Jesus.mc.player.fallDistance < 3.0F && !EntityUtil.isInWater(Jesus.mc.player) && (EntityUtil.isAboveWater(Jesus.mc.player, false) || EntityUtil.isAboveWater(Jesus.mc.player.getRidingEntity(), false)) && isAboveBlock(Jesus.mc.player, event.getPos())) {
            AxisAlignedBB axisalignedbb = Jesus.WATER_WALK_AA.offset(event.getPos());

            if (event.getEntityBox().intersects(axisalignedbb)) {
                event.getCollidingBoxes().add(axisalignedbb);
            }

            event.cancel();
        }

    }, new Predicate[0]);
    @EventHandler
    Listener packetEventSendListener = new Listener((event) -> {
        if (event.getEra() == KamiEvent.Era.PRE && event.getPacket() instanceof CPacketPlayer && EntityUtil.isAboveWater(Jesus.mc.player, true) && !EntityUtil.isInWater(Jesus.mc.player) && !isAboveLand(Jesus.mc.player)) {
            int ticks = Jesus.mc.player.ticksExisted % 2;

            if (ticks == 0) {
                CPacketPlayer cpacketplayer = (CPacketPlayer) event.getPacket();

                cpacketplayer.y += 0.02D;
            }
        }

    }, new Predicate[0]);

    public void onUpdate() {
        if (!ModuleManager.isModuleEnabled("Freecam") && EntityUtil.isInWater(Jesus.mc.player) && !Jesus.mc.player.isSneaking()) {
            Jesus.mc.player.motionY = 0.1D;
            if (Jesus.mc.player.getRidingEntity() != null && !(Jesus.mc.player.getRidingEntity() instanceof EntityBoat)) {
                Jesus.mc.player.getRidingEntity().motionY = 0.3D;
            }
        }

    }

    private static boolean isAboveLand(Entity entity) {
        if (entity == null) {
            return false;
        } else {
            double y = entity.posY - 0.01D;

            for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
                for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                    BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);

                    if (Wrapper.getWorld().getBlockState(pos).getBlock().isFullBlock(Wrapper.getWorld().getBlockState(pos))) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private static boolean isAboveBlock(Entity entity, BlockPos pos) {
        return entity.posY >= (double) pos.getY();
    }
}
