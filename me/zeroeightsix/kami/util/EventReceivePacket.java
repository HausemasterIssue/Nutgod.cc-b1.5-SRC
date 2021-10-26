package me.zeroeightsix.kami.util;

import net.minecraft.network.Packet;

public class EventReceivePacket extends EventCancellable {

    private Packet packet;

    public EventReceivePacket(EventStageable.EventStage stage, Packet packet) {
        super(stage);
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
