package me.zeroeightsix.kami.module.modules.combat;

import java.util.Collections;
import java.util.List;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.BlockInteractionHelper;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Info(
    name = "Surround",
    category = Module.Category.COMBAT
)
public class Surround extends Module {

    private final Vec3d[] surroundList = new Vec3d[] { new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, -1.0D)};
    private final Vec3d[] surroundListFull = new Vec3d[] { new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 0.0D, -1.0D), new Vec3d(-1.0D, 1.0D, -1.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 0.0D, -1.0D), new Vec3d(1.0D, 1.0D, -1.0D)};
    private final List obsidian;
    private Setting toggleable;
    private Setting slowmode;
    private Setting full;
    private Vec3d[] surroundTargets;
    private int blocksPerTick;
    private BlockPos basePos;
    private boolean slowModeSwitch;
    private int offsetStep;
    private int oldSlot;

    public Surround() {
        this.obsidian = Collections.singletonList(Blocks.OBSIDIAN);
        this.toggleable = this.register(Settings.b("Toggleable", true));
        this.slowmode = this.register(Settings.b("Slower version", false));
        this.full = this.register(Settings.b("Adds extra corners", false));
        this.blocksPerTick = 3;
        this.slowModeSwitch = false;
        this.offsetStep = 0;
        this.oldSlot = 0;
    }

    public void onUpdate() {
        if (!this.isDisabled() && Surround.mc.player != null && !ModuleManager.isModuleEnabled("Freecam")) {
            if (this.slowModeSwitch) {
                this.slowModeSwitch = false;
            } else {
                if (this.offsetStep == 0) {
                    this.init();
                }

                for (int i = 0; i < this.blocksPerTick; ++i) {
                    if (this.offsetStep >= this.surroundTargets.length) {
                        this.end();
                        return;
                    }

                    Vec3d offset = this.surroundTargets[this.offsetStep];

                    this.placeBlock(new BlockPos(this.basePos.add(offset.x, offset.y, offset.z)));
                    ++this.offsetStep;
                }

                this.slowModeSwitch = true;
            }
        }

    }

    private void placeBlock(BlockPos blockPos) {
        if (Wrapper.getWorld().getBlockState(blockPos).getMaterial().isReplaceable()) {
            int newSlot = -1;

            for (int i = 0; i < 9; ++i) {
                ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);

                if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) stack.getItem()).getBlock();

                    if (!BlockInteractionHelper.blackList.contains(block) && !(block instanceof BlockContainer) && Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock() && (!(((ItemBlock) stack.getItem()).getBlock() instanceof BlockFalling) || !Wrapper.getWorld().getBlockState(blockPos.down()).getMaterial().isReplaceable()) && this.obsidian.contains(block)) {
                        newSlot = i;
                        break;
                    }
                }
            }

            if (newSlot == -1) {
                if (!((Boolean) this.toggleable.getValue()).booleanValue()) {
                    Command.sendChatMessage("Surround: Please Put Obsidian in Hotbar");
                }

                this.end();
            } else {
                Wrapper.getPlayer().inventory.currentItem = newSlot;
                if (BlockInteractionHelper.checkForNeighbours(blockPos)) {
                    BlockInteractionHelper.placeBlockScaffold(blockPos);
                }
            }
        }

    }

    private void init() {
        this.basePos = (new BlockPos(Surround.mc.player.getPositionVector())).down();
        if (((Boolean) this.slowmode.getValue()).booleanValue()) {
            this.blocksPerTick = 1;
        }

        if (((Boolean) this.full.getValue()).booleanValue()) {
            this.surroundTargets = this.surroundListFull;
        } else {
            this.surroundTargets = this.surroundList;
        }

    }

    private void end() {
        this.offsetStep = 0;
        if (!((Boolean) this.toggleable.getValue()).booleanValue()) {
            this.disable();
        }

    }

    protected void onEnable() {
        Surround.mc.player.connection.sendPacket(new CPacketEntityAction(Surround.mc.player, Action.START_SNEAKING));
        this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
    }

    protected void onDisable() {
        Surround.mc.player.connection.sendPacket(new CPacketEntityAction(Surround.mc.player, Action.STOP_SNEAKING));
        Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
    }
}
