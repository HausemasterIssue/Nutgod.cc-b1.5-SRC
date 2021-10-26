package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.module.modules.render.Pathfind;
import net.minecraft.pathfinding.PathPoint;

public class PathCommand extends Command {

    int x = Integer.MIN_VALUE;
    int y = Integer.MIN_VALUE;
    int z = Integer.MIN_VALUE;

    public PathCommand() {
        super("path", (new ChunkBuilder()).append("x").append("y").append("z").build());
    }

    public void call(String[] args) {
        PathPoint e;

        if (args[0] != null && args[0].equalsIgnoreCase("retry")) {
            if (this.x != Integer.MIN_VALUE) {
                e = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(e);
                if (!Pathfind.points.isEmpty()) {
                    Command.sendChatMessage("Path created!");
                }

            } else {
                Command.sendChatMessage("No location to retry pathfinding to.");
            }
        } else if (args.length <= 3) {
            Command.sendChatMessage("&cMissing arguments: x, y, z");
        } else {
            try {
                this.x = Integer.parseInt(args[0]);
                this.y = Integer.parseInt(args[1]);
                this.z = Integer.parseInt(args[2]);
                e = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(e);
                if (!Pathfind.points.isEmpty()) {
                    Command.sendChatMessage("Path created!");
                }

            } catch (NumberFormatException numberformatexception) {
                Command.sendChatMessage("Error: input must be numerical");
            }
        }
    }
}
