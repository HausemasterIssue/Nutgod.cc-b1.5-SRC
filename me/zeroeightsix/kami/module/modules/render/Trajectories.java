package me.zeroeightsix.kami.module.modules.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.util.GeometryMasks;
import me.zeroeightsix.kami.util.HueCycler;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.util.TrajectoryCalculator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Module.Info(
    name = "Trajectories",
    category = Module.Category.RENDER
)
public class Trajectories extends Module {

    ArrayList positions = new ArrayList();
    HueCycler cycler = new HueCycler(100);

    public void onWorldRender(RenderEvent event) {
        try {
            Trajectories.mc.world.loadedEntityList.stream().filter((entity) -> {
                return entity instanceof EntityLivingBase;
            }).map((entity) -> {
                return (EntityLivingBase) entity;
            }).forEach((entity) -> {
                this.positions.clear();
                TrajectoryCalculator.ThrowingType tt = TrajectoryCalculator.getThrowType(entity);

                if (tt != TrajectoryCalculator.ThrowingType.NONE) {
                    TrajectoryCalculator.FlightPath flightPath = new TrajectoryCalculator.FlightPath(entity, tt);

                    while (!flightPath.isCollided()) {
                        flightPath.onUpdate();
                        this.positions.add(flightPath.position);
                    }

                    BlockPos hit = null;

                    if (flightPath.getCollidingTarget() != null) {
                        hit = flightPath.getCollidingTarget().getBlockPos();
                    }

                    GL11.glEnable(3042);
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glDisable(2929);
                    if (hit != null) {
                        KamiTessellator.prepare(7);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
                        KamiTessellator.drawBox(hit, 872415231, ((Integer) GeometryMasks.FACEMAP.get(flightPath.getCollidingTarget().sideHit)).intValue());
                        KamiTessellator.release();
                    }

                    if (!this.positions.isEmpty()) {
                        GL11.glDisable(3042);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glLineWidth(2.0F);
                        if (hit != null) {
                            GL11.glColor3f(1.0F, 1.0F, 1.0F);
                        } else {
                            this.cycler.setNext();
                        }

                        GL11.glBegin(1);
                        Vec3d a = (Vec3d) this.positions.get(0);

                        GL11.glVertex3d(a.x - Trajectories.mc.getRenderManager().renderPosX, a.y - Trajectories.mc.getRenderManager().renderPosY, a.z - Trajectories.mc.getRenderManager().renderPosZ);
                        Iterator iterator = this.positions.iterator();

                        while (iterator.hasNext()) {
                            Vec3d v = (Vec3d) iterator.next();

                            GL11.glVertex3d(v.x - Trajectories.mc.getRenderManager().renderPosX, v.y - Trajectories.mc.getRenderManager().renderPosY, v.z - Trajectories.mc.getRenderManager().renderPosZ);
                            GL11.glVertex3d(v.x - Trajectories.mc.getRenderManager().renderPosX, v.y - Trajectories.mc.getRenderManager().renderPosY, v.z - Trajectories.mc.getRenderManager().renderPosZ);
                            if (hit == null) {
                                this.cycler.setNext();
                            }
                        }

                        GL11.glEnd();
                        GL11.glEnable(3042);
                        GL11.glEnable(3553);
                        this.cycler.reset();
                    }
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
