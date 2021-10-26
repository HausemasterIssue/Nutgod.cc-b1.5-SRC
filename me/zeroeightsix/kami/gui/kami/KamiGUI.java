package me.zeroeightsix.kami.gui.kami;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.gui.kami.component.ActiveModules;
import me.zeroeightsix.kami.gui.kami.component.Radar;
import me.zeroeightsix.kami.gui.kami.component.SettingsPanel;
import me.zeroeightsix.kami.gui.kami.theme.kami.KamiTheme;
import me.zeroeightsix.kami.gui.rgui.GUI;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Scrollpane;
import me.zeroeightsix.kami.gui.rgui.component.listen.MouseListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.TickListener;
import me.zeroeightsix.kami.gui.rgui.component.use.CheckButton;
import me.zeroeightsix.kami.gui.rgui.component.use.Label;
import me.zeroeightsix.kami.gui.rgui.render.theme.Theme;
import me.zeroeightsix.kami.gui.rgui.util.ContainerHelper;
import me.zeroeightsix.kami.gui.rgui.util.Docking;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.ColourHolder;
import me.zeroeightsix.kami.util.LagCompensator;
import me.zeroeightsix.kami.util.Pair;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.text.TextFormatting;

public class KamiGUI extends GUI {

    public static final RootFontRenderer fontRenderer = new RootFontRenderer(1.0F);
    public Theme theme = this.getTheme();
    public static ColourHolder primaryColour = new ColourHolder(29, 29, 29);
    private static final int DOCK_OFFSET = 0;

    public KamiGUI() {
        super(new KamiTheme());
    }

    public void drawGUI() {
        super.drawGUI();
    }

    public void initializeGUI() {
        HashMap categoryScrollpaneHashMap = new HashMap();
        Iterator x = ModuleManager.getModules().iterator();

        while (x.hasNext()) {
            final Module y = (Module) x.next();

            if (!y.getCategory().isHidden()) {
                Module.Category nexty = y.getCategory();
                Scrollpane frame;

                if (!categoryScrollpaneHashMap.containsKey(nexty)) {
                    Stretcherlayout frames = new Stretcherlayout(1);

                    frames.setComponentOffsetWidth(0);
                    frame = new Scrollpane(this.getTheme(), frames, 300, 260);
                    frame.setMaximumHeight(180);
                    categoryScrollpaneHashMap.put(nexty, new Pair(frame, new SettingsPanel(this.getTheme(), (Module) null)));
                }

                final Pair frames1 = (Pair) categoryScrollpaneHashMap.get(nexty);

                frame = (Scrollpane) frames1.getKey();
                final CheckButton information = new CheckButton(y.getName());

                information.setToggled(y.isEnabled());
                information.addTickListener(onTick<invokedynamic>(information, y));
                information.addMouseListener(new MouseListener() {
                    public void onMouseDown(MouseListener.MouseButtonEvent event) {
                        if (event.getButton() == 1) {
                            ((SettingsPanel) frames1.getValue()).setModule(y);
                            ((SettingsPanel) frames1.getValue()).setX(event.getX() + information.getX());
                            ((SettingsPanel) frames1.getValue()).setY(event.getY() + information.getY());
                        }

                    }

                    public void onMouseRelease(MouseListener.MouseButtonEvent event) {}

                    public void onMouseDrag(MouseListener.MouseButtonEvent event) {}

                    public void onMouseMove(MouseListener.MouseMoveEvent event) {}

                    public void onScroll(MouseListener.MouseScrollEvent event) {}
                });
                information.addPoof(new CheckButton.CheckButtonPoof() {
                    public void execute(CheckButton component, CheckButton.CheckButtonPoof.CheckButtonPoofInfo info) {
                        if (info.getAction().equals(CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE)) {
                            y.setEnabled(information.isToggled());
                        }

                    }
                });
                frame.addChild(new Component[] { information});
            }
        }

        int x1 = 10;
        int y1 = 10;
        int nexty1 = y1;
        Iterator frames2 = categoryScrollpaneHashMap.entrySet().iterator();

        while (frames2.hasNext()) {
            Entry frame1 = (Entry) frames2.next();
            Stretcherlayout information1 = new Stretcherlayout(1);

            information1.COMPONENT_OFFSET_Y = 1;
            Frame watermark = new Frame(this.getTheme(), information1, ((Module.Category) frame1.getKey()).getName());
            Scrollpane welcomer = (Scrollpane) ((Pair) frame1.getValue()).getKey();

            watermark.addChild(new Component[] { welcomer});
            watermark.addChild(new Component[] { (Component) ((Pair) frame1.getValue()).getValue()});
            welcomer.setOriginOffsetY(0);
            welcomer.setOriginOffsetX(0);
            watermark.setCloseable(false);
            watermark.setX(x1);
            watermark.setY(y1);
            this.addChild(new Component[] { watermark});
            nexty1 = Math.max(y1 + watermark.getHeight() + 10, nexty1);
            x1 += watermark.getWidth() + 10;
            if ((float) x1 > (float) Wrapper.getMinecraft().displayWidth / 1.2F) {
                y1 = nexty1;
                nexty1 = nexty1;
            }
        }

        this.addMouseListener(new MouseListener() {
            private boolean isBetween(int min, int val, int max) {
                return val <= max && val >= min;
            }

            public void onMouseDown(MouseListener.MouseButtonEvent event) {
                List panels = ContainerHelper.getAllChildren(SettingsPanel.class, KamiGUI.this);
                Iterator iterator = panels.iterator();

                while (iterator.hasNext()) {
                    SettingsPanel settingsPanel = (SettingsPanel) iterator.next();

                    if (settingsPanel.isVisible()) {
                        int[] real = GUI.calculateRealPosition(settingsPanel);
                        int pX = event.getX() - real[0];
                        int pY = event.getY() - real[1];

                        if (!this.isBetween(0, pX, settingsPanel.getWidth()) || !this.isBetween(0, pY, settingsPanel.getHeight())) {
                            settingsPanel.setVisible(false);
                        }
                    }
                }

            }

            public void onMouseRelease(MouseListener.MouseButtonEvent event) {}

            public void onMouseDrag(MouseListener.MouseButtonEvent event) {}

            public void onMouseMove(MouseListener.MouseMoveEvent event) {}

            public void onScroll(MouseListener.MouseScrollEvent event) {}
        });
        ArrayList frames3 = new ArrayList();
        Frame frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Active modules");

        frame2.setCloseable(false);
        frame2.addChild(new Component[] { new ActiveModules()});
        frame2.setPinneable(true);
        frames3.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Info");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        Label information2 = new Label("");

        information2.setShadow(true);
        information2.addTickListener(onTick<invokedynamic>(information2));
        frame2.addChild(new Component[] { information2});
        information2.setFontRenderer(KamiGUI.fontRenderer);
        frames3.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Watermark");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        Label watermark1 = new Label("");

        watermark1.addTickListener(onTick<invokedynamic>(watermark1));
        frame2.addChild(new Component[] { watermark1});
        information2.setFontRenderer(KamiGUI.fontRenderer);
        frames3.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Welcomer");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        Label welcomer1 = new Label("");

        welcomer1.addTickListener(onTick<invokedynamic>(welcomer1));
        frame2.addChild(new Component[] { welcomer1});
        information2.setFontRenderer(KamiGUI.fontRenderer);
        frames3.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Text Radar");
        Label list = new Label("");
        DecimalFormat dfHealth = new DecimalFormat("#.#");

        dfHealth.setRoundingMode(RoundingMode.HALF_UP);
        StringBuilder healthSB = new StringBuilder();

        list.addTickListener(onTick<invokedynamic>(list, dfHealth, healthSB));
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        frame2.setMinimumWidth(75);
        list.setShadow(true);
        frame2.addChild(new Component[] { list});
        list.setFontRenderer(KamiGUI.fontRenderer);
        frames3.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Entities");
        final Label entityLabel = new Label("");

        frame2.setCloseable(false);
        entityLabel.addTickListener(new TickListener() {
            Minecraft mc = Wrapper.getMinecraft();

            public void onTick() {
                if (this.mc.player != null && entityLabel.isVisible()) {
                    ArrayList entityList = new ArrayList(this.mc.world.loadedEntityList);

                    if (entityList.size() <= 1) {
                        entityLabel.setText("");
                    } else {
                        Map entityCounts = (Map) entityList.stream().filter(Objects::nonNull).filter((e) -> {
                            return !(e instanceof EntityPlayer);
                        }).collect(Collectors.groupingBy((x$0) -> {
                            return KamiGUI.getEntityName(x$0);
                        }, Collectors.reducing(Integer.valueOf(0), (ent) -> {
                            return ent instanceof EntityItem ? Integer.valueOf(((EntityItem) ent).getItem().getCount()) : Integer.valueOf(1);
                        }, Integer::sum)));

                        entityLabel.setText("");
                        Stream stream = entityCounts.entrySet().stream().sorted(Entry.comparingByValue()).map((entry) -> {
                            return TextFormatting.GRAY + (String) entry.getKey() + " " + TextFormatting.DARK_GRAY + "x" + entry.getValue();
                        });
                        Label label = entityLabel;

                        entityLabel.getClass();
                        stream.forEach(label::addLine);
                    }
                }
            }
        });
        frame2.addChild(new Component[] { entityLabel});
        frame2.setPinneable(true);
        entityLabel.setShadow(true);
        entityLabel.setFontRenderer(KamiGUI.fontRenderer);
        frames3.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Coordinates");
        frame2.setCloseable(false);
        frame2.setPinneable(true);
        final Label coordsLabel = new Label("");

        coordsLabel.addTickListener(new TickListener() {
            Minecraft mc = Minecraft.getMinecraft();

            public void onTick() {
                boolean inHell = this.mc.world.getBiome(this.mc.player.getPosition()).getBiomeName().equals("Hell");
                int posX = (int) this.mc.player.posX;
                int posY = (int) this.mc.player.posY;
                int posZ = (int) this.mc.player.posZ;
                float f = !inHell ? 0.125F : 8.0F;
                int hposX = (int) (this.mc.player.posX * (double) f);
                int hposZ = (int) (this.mc.player.posZ * (double) f);

                coordsLabel.setText(String.format(" %sf%,d%s7, %sf%,d%s7, %sf%,d %s7(%sf%,d%s7, %sf%,d%s7, %sf%,d%s7)", new Object[] { Character.valueOf(Command.SECTIONSIGN()), Integer.valueOf(posX), Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), Integer.valueOf(posY), Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), Integer.valueOf(posZ), Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), Integer.valueOf(hposX), Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), Integer.valueOf(posY), Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), Integer.valueOf(hposZ), Character.valueOf(Command.SECTIONSIGN())}));
            }
        });
        frame2.addChild(new Component[] { coordsLabel});
        coordsLabel.setFontRenderer(KamiGUI.fontRenderer);
        coordsLabel.setShadow(true);
        frame2.setHeight(20);
        frames3.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Radar");
        frame2.setCloseable(false);
        frame2.setMinimizeable(true);
        frame2.setPinneable(true);
        frame2.addChild(new Component[] { new Radar()});
        frame2.setWidth(100);
        frame2.setHeight(100);
        frames3.add(frame2);

        Frame frame1;

        for (Iterator iterator = frames3.iterator(); iterator.hasNext(); this.addChild(new Component[] { frame1})) {
            frame1 = (Frame) iterator.next();
            frame1.setX(x1);
            frame1.setY(y1);
            nexty1 = Math.max(y1 + frame1.getHeight() + 10, nexty1);
            x1 += frame1.getWidth() + 10;
            if ((float) (x1 * DisplayGuiScreen.getScale()) > (float) Wrapper.getMinecraft().displayWidth / 1.2F) {
                y1 = nexty1;
                nexty1 = nexty1;
                x1 = 10;
            }
        }

    }

    private static String getEntityName(@Nonnull Entity entity) {
        return entity instanceof EntityItem ? TextFormatting.DARK_AQUA + ((EntityItem) entity).getItem().getItem().getItemStackDisplayName(((EntityItem) entity).getItem()) : (entity instanceof EntityWitherSkull ? TextFormatting.DARK_GRAY + "Wither skull" : (entity instanceof EntityEnderCrystal ? TextFormatting.LIGHT_PURPLE + "End crystal" : (entity instanceof EntityEnderPearl ? "Thrown ender pearl" : (entity instanceof EntityMinecart ? "Minecart" : (entity instanceof EntityItemFrame ? "Item frame" : (entity instanceof EntityEgg ? "Thrown egg" : (entity instanceof EntitySnowball ? "Thrown snowball" : entity.getName())))))));
    }

    public static Map sortByValue(Map map) {
        LinkedList list = new LinkedList(map.entrySet());

        Collections.sort(list, Comparator.comparing(apply<invokedynamic>()));
        LinkedHashMap result = new LinkedHashMap();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public void destroyGUI() {
        this.kill();
    }

    public static void dock(Frame component) {
        Docking docking = component.getDocking();

        if (docking.isTop()) {
            component.setY(0);
        }

        if (docking.isBottom()) {
            component.setY(Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale() - component.getHeight() - 0);
        }

        if (docking.isLeft()) {
            component.setX(0);
        }

        if (docking.isRight()) {
            component.setX(Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale() - component.getWidth() - 0);
        }

        if (docking.isCenterHorizontal()) {
            component.setX(Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2);
        }

        if (docking.isCenterVertical()) {
            component.setY(Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2);
        }

    }

    private static Comparable lambda$sortByValue$5(Entry o) {
        return (Comparable) o.getValue();
    }

    private static void lambda$initializeGUI$4(Label list, DecimalFormat dfHealth, StringBuilder healthSB) {
        if (list.isVisible()) {
            list.setText("");
            Minecraft mc = Wrapper.getMinecraft();

            if (mc.player != null) {
                List entityList = mc.world.playerEntities;
                HashMap players = new HashMap();
                Iterator iterator = entityList.iterator();

                while (iterator.hasNext()) {
                    Entity player = (Entity) iterator.next();

                    if (!player.getName().equals(mc.player.getName())) {
                        String posString = player.posY > mc.player.posY ? ChatFormatting.DARK_GREEN + "+" : (player.posY == mc.player.posY ? " " : ChatFormatting.DARK_RED + "-");
                        float hpRaw = ((EntityLivingBase) player).getHealth() + ((EntityLivingBase) player).getAbsorptionAmount();
                        String hp = dfHealth.format((double) hpRaw);

                        healthSB.append(Command.SECTIONSIGN());
                        if (hpRaw >= 20.0F) {
                            healthSB.append("a");
                        } else if (hpRaw >= 10.0F) {
                            healthSB.append("e");
                        } else if (hpRaw >= 5.0F) {
                            healthSB.append("6");
                        } else {
                            healthSB.append("c");
                        }

                        healthSB.append(hp);
                        players.put(ChatFormatting.GRAY + posString + " " + healthSB.toString() + " " + ChatFormatting.GRAY + player.getName(), Integer.valueOf((int) mc.player.getDistance(player)));
                        healthSB.setLength(0);
                    }
                }

                if (players.isEmpty()) {
                    list.setText("");
                } else {
                    Map players1 = sortByValue(players);

                    iterator = players1.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Entry player1 = (Entry) iterator.next();

                        list.addLine(Command.SECTIONSIGN() + "7" + (String) player1.getKey() + " " + Command.SECTIONSIGN() + "8" + player1.getValue());
                    }

                }
            }
        }
    }

    private static void lambda$initializeGUI$3(Label welcomer) {
        welcomer.setText("");
        welcomer.addLine("§dWelcome " + Wrapper.getPlayer().getDisplayNameString() + " §d࿉");
    }

    private static void lambda$initializeGUI$2(Label watermark) {
        watermark.setText("");
        watermark.addLine("§dNutgod.cc B1.5");
    }

    private static void lambda$initializeGUI$1(Label information) {
        information.setText("");
        information.addLine("§d " + Math.round(LagCompensator.INSTANCE.getTickRate()) + Command.SECTIONSIGN() + "8 tps");
        Wrapper.getMinecraft();
        information.addLine("§d " + Minecraft.debugFPS + Command.SECTIONSIGN() + "8 fps");
        int ms = Minecraft.getMinecraft().getCurrentServerData() == null ? 0 : (int) Minecraft.getMinecraft().getCurrentServerData().pingToServer;

        information.addLine("§d " + ms + Command.SECTIONSIGN() + "8 Ping");
    }

    private static void lambda$initializeGUI$0(CheckButton checkButton, Module module) {
        checkButton.setToggled(module.isEnabled());
        checkButton.setName(module.getName());
    }
}
