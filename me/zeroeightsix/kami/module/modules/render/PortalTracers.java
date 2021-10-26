package me.zeroeightsix.kami.module.modules.render;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.ChunkEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class PortalTracers extends Module {

    private Setting range = this.register(Settings.i("Range", 5000));
    private ArrayList portals = new ArrayList();
    @EventHandler
    private Listener loadListener = new Listener((event) -> {
        Chunk chunk = event.getChunk();

        this.portals.removeIf((blockPos) -> {
            return blockPos.getX() / 16 == chunk.x && blockPos.getZ() / 16 == chunk.z;
        });
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        int i = aextendedblockstorage.length;

        for (int j = 0; j < i; ++j) {
            ExtendedBlockStorage storage = aextendedblockstorage[j];

            if (storage != null) {
                for (int x = 0; x < 16; ++x) {
                    for (int y = 0; y < 16; ++y) {
                        for (int z = 0; z < 16; ++z) {
                            if (storage.get(x, y, z).getBlock() instanceof BlockPortal) {
                                int px = chunk.x * 16 + x;
                                int py = storage.yBase + y;
                                int pz = chunk.z * 16 + z;

                                this.portals.add(new BlockPos(px, py, pz));
                                y += 6;
                            }
                        }
                    }
                }
            }
        }

    }, new Predicate[0]);

    public void onWorldRender(RenderEvent event) {
        this.portals.stream().filter((blockPos) -> {
            return PortalTracers.mc.player.getDistance((double) blockPos.x, (double) blockPos.y, (double) blockPos.z) <= (double) ((Integer) this.range.getValue()).intValue();
        }).forEach((blockPos) -> {
            Tracers.drawLine((double) blockPos.x - PortalTracers.mc.getRenderManager().renderPosX, (double) blockPos.y - PortalTracers.mc.getRenderManager().renderPosY, (double) blockPos.z - PortalTracers.mc.getRenderManager().renderPosZ, 0.0D, 0.6F, 0.3F, 0.8F, 1.0F);
        });
    }
}
