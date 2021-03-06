package me.zeroeightsix.kami.util.other;

public class EventCancellable extends EventStageable {

    private boolean canceled;

    public EventCancellable() {}

    public EventCancellable(EventStageable.EventStage stage) {
        super(stage);
    }

    public EventCancellable(EventStageable.EventStage stage, boolean canceled) {
        super(stage);
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
