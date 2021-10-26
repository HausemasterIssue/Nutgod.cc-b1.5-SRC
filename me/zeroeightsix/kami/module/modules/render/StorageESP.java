package me.zeroeightsix.kami.module.modules.render;

import java.util.ArrayList;
import java.util.Iterator;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.util.ColourUtils;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;

@Module.Info(
    name = "StorageESP",
    description = "Draws nice little lines around storage items",
    category = Module.Category.RENDER
)
public class StorageESP extends Module {

    private int getTileEntityColor(TileEntity tileEntity) {
        return !(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityDispenser) && !(tileEntity instanceof TileEntityShulkerBox) ? (tileEntity instanceof TileEntityEnderChest ? ColourUtils.Colors.PURPLE : (tileEntity instanceof TileEntityFurnace ? ColourUtils.Colors.GRAY : (tileEntity instanceof TileEntityHopper ? ColourUtils.Colors.DARK_RED : -1))) : ColourUtils.Colors.ORANGE;
    }

    private int getEntityColor(Entity entity) {
        return entity instanceof EntityMinecartChest ? ColourUtils.Colors.ORANGE : (entity instanceof EntityItemFrame && ((EntityItemFrame) entity).getDisplayedItem().getItem() instanceof ItemShulkerBox ? ColourUtils.Colors.YELLOW : -1);
    }

    public void onWorldRender(RenderEvent event) {
        ArrayList a = new ArrayList();

        GlStateManager.pushMatrix();
        Iterator iterator = Wrapper.getWorld().loadedTileEntityList.iterator();

        BlockPos pos;
        int color;

        while (iterator.hasNext()) {
            TileEntity pair = (TileEntity) iterator.next();

            pos = pair.getPos();
            color = this.getTileEntityColor(pair);
            int side = 63;

            if (pair instanceof TileEntityChest) {
                TileEntityChest chest = (TileEntityChest) pair;

                if (chest.adjacentChestZNeg != null) {
                    side = ~(side & 4);
                }

                if (chest.adjacentChestXPos != null) {
                    side = ~(side & 32);
                }

                if (chest.adjacentChestZPos != null) {
                    side = ~(side & 8);
                }

                if (chest.adjacentChestXNeg != null) {
                    side = ~(side & 16);
                }
            }

            if (color != -1) {
                a.add(new StorageESP.Triplet(pos, Integer.valueOf(color), Integer.valueOf(side)));
            }
        }

        iterator = Wrapper.getWorld().loadedEntityList.iterator();

        while (iterator.hasNext()) {
            Entity pair1 = (Entity) iterator.next();

            pos = pair1.getPosition();
            color = this.getEntityColor(pair1);
            if (color != -1) {
                a.add(new StorageESP.Triplet(pair1 instanceof EntityItemFrame ? pos.add(0, -1, 0) : pos, Integer.valueOf(color), Integer.valueOf(63)));
            }
        }

        KamiTessellator.prepare(7);
        iterator = a.iterator();

        while (iterator.hasNext()) {
            StorageESP.Triplet pair2 = (StorageESP.Triplet) iterator.next();

            KamiTessellator.drawBox((BlockPos) pair2.getFirst(), this.changeAlpha(((Integer) pair2.getSecond()).intValue(), 100), ((Integer) pair2.getThird()).intValue());
        }

        KamiTessellator.release();
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
    }

    int changeAlpha(int origColor, int userInputedAlpha) {
        origColor &= 16777215;
        return userInputedAlpha << 24 | origColor;
    }

    public class Triplet {

        private final Object first;
        private final Object second;
        private final Object third;

        public Triplet(Object first, Object second, Object third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public Object getFirst() {
            return this.first;
        }

        public Object getSecond() {
            return this.second;
        }

        public Object getThird() {
            return this.third;
        }
    }
}
