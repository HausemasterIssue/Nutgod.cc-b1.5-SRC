package me.zeroeightsix.kami.event.events;

import me.zeroeightsix.kami.event.KamiEvent;
import net.minecraft.entity.MoverType;

public class PlayerMoveEvent extends KamiEvent {

    private MoverType type;
    private double x;
    private double y;
    private double z;

    public PlayerMoveEvent(MoverType type, double x, double y, double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MoverType getType() {
        return this.type;
    }

    public void setType(MoverType type) {
        this.type = type;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
