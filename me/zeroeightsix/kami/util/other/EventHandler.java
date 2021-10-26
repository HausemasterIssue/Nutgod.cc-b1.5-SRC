package me.zeroeightsix.kami.util.other;

public interface EventHandler {

    void handle(Object object);

    Object getListener();

    Iterable getFilters();
}
