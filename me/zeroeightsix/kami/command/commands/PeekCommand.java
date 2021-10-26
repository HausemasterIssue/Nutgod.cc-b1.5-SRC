package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class PeekCommand extends Command {

    public static TileEntityShulkerBox sb;

    public PeekCommand() {
        super("peek", SyntaxChunk.EMPTY);
    }

    public void call(String[] args) {
        ItemStack is = Wrapper.getPlayer().inventory.getCurrentItem();

        if (is.getItem() instanceof ItemShulkerBox) {
            TileEntityShulkerBox entityBox = new TileEntityShulkerBox();

            entityBox.blockType = ((ItemShulkerBox) is.getItem()).getBlock();
            entityBox.setWorld(Wrapper.getWorld());
            entityBox.readFromNBT(is.getTagCompound().getCompoundTag("BlockEntityTag"));
            PeekCommand.sb = entityBox;
        } else {
            Command.sendChatMessage("You aren\'t carrying a shulker box.");
        }

    }
}
