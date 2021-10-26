package me.zeroeightsix.kami.gui.kami.component;

import java.util.Arrays;
import java.util.Iterator;
import me.zeroeightsix.kami.gui.kami.Stretcherlayout;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.OrganisedContainer;
import me.zeroeightsix.kami.gui.rgui.component.use.CheckButton;
import me.zeroeightsix.kami.gui.rgui.component.use.Slider;
import me.zeroeightsix.kami.gui.rgui.render.theme.Theme;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.impl.BooleanSetting;
import me.zeroeightsix.kami.setting.impl.EnumSetting;
import me.zeroeightsix.kami.setting.impl.numerical.DoubleSetting;
import me.zeroeightsix.kami.setting.impl.numerical.FloatSetting;
import me.zeroeightsix.kami.setting.impl.numerical.IntegerSetting;
import me.zeroeightsix.kami.setting.impl.numerical.NumberSetting;
import me.zeroeightsix.kami.util.Bind;

public class SettingsPanel extends OrganisedContainer {

    Module module;

    public SettingsPanel(Theme theme, Module module) {
        super(theme, new Stretcherlayout(1));
        this.setAffectLayout(false);
        this.module = module;
        this.prepare();
    }

    public void renderChildren() {
        super.renderChildren();
    }

    public Module getModule() {
        return this.module;
    }

    private void prepare() {
        this.getChildren().clear();
        if (this.module == null) {
            this.setVisible(false);
        } else {
            if (!this.module.settingList.isEmpty()) {
                Iterator iterator = this.module.settingList.iterator();

                while (iterator.hasNext()) {
                    final Setting setting = (Setting) iterator.next();

                    if (setting.isVisible()) {
                        String name = setting.getName();
                        boolean isNumber = setting instanceof NumberSetting;
                        boolean isBoolean = setting instanceof BooleanSetting;
                        boolean isEnum = setting instanceof EnumSetting;

                        if (setting.getValue() instanceof Bind) {
                            this.addChild(new Component[] { new BindButton("Bind", this.module)});
                        }

                        if (isNumber) {
                            NumberSetting type = (NumberSetting) setting;
                            boolean con = type.isBound();

                            if (!con) {
                                UnboundSlider modes = new UnboundSlider(type.getValue().doubleValue(), name, setting instanceof IntegerSetting);

                                modes.addPoof(new Slider.SliderPoof() {
                                    public void execute(UnboundSlider component, Slider.SliderPoof.SliderPoofInfo info) {
                                        if (setting instanceof IntegerSetting) {
                                            setting.setValue(new Integer((int) info.getNewValue()));
                                        } else if (setting instanceof FloatSetting) {
                                            setting.setValue(new Float(info.getNewValue()));
                                        } else if (setting instanceof DoubleSetting) {
                                            setting.setValue(Double.valueOf(info.getNewValue()));
                                        }

                                        SettingsPanel.this.setModule(SettingsPanel.this.module);
                                    }
                                });
                                if (type.getMax() != null) {
                                    modes.setMax(type.getMax().doubleValue());
                                }

                                if (type.getMin() != null) {
                                    modes.setMin(type.getMin().doubleValue());
                                }

                                this.addChild(new Component[] { modes});
                            } else {
                                Slider modes1 = new Slider(type.getValue().doubleValue(), type.getMin().doubleValue(), type.getMax().doubleValue(), Slider.getDefaultStep(type.getMin().doubleValue(), type.getMax().doubleValue()), name, setting instanceof IntegerSetting);

                                modes1.addPoof(new Slider.SliderPoof() {
                                    public void execute(Slider component, Slider.SliderPoof.SliderPoofInfo info) {
                                        if (setting instanceof IntegerSetting) {
                                            setting.setValue(new Integer((int) info.getNewValue()));
                                        } else if (setting instanceof FloatSetting) {
                                            setting.setValue(new Float(info.getNewValue()));
                                        } else if (setting instanceof DoubleSetting) {
                                            setting.setValue(Double.valueOf(info.getNewValue()));
                                        }

                                        SettingsPanel.this.setModule(SettingsPanel.this.module);
                                    }
                                });
                                this.addChild(new Component[] { modes1});
                            }
                        } else if (isBoolean) {
                            final CheckButton type1 = new CheckButton(name);

                            type1.setToggled(((Boolean) ((BooleanSetting) setting).getValue()).booleanValue());
                            type1.addPoof(new CheckButton.CheckButtonPoof() {
                                public void execute(CheckButton checkButton1, CheckButton.CheckButtonPoof.CheckButtonPoofInfo info) {
                                    if (info.getAction() == CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE) {
                                        setting.setValue(Boolean.valueOf(type1.isToggled()));
                                        SettingsPanel.this.setModule(SettingsPanel.this.module);
                                    }

                                }
                            });
                            this.addChild(new Component[] { type1});
                        } else if (isEnum) {
                            Class type2 = ((EnumSetting) setting).clazz;
                            final Object[] con1 = type2.getEnumConstants();
                            String[] modes2 = (String[]) Arrays.stream(con1).map(apply<invokedynamic>()).toArray(apply<invokedynamic>());
                            EnumButton enumbutton = new EnumButton(name, modes2);

                            enumbutton.addPoof(new EnumButton.EnumbuttonIndexPoof() {
                                public void execute(EnumButton component, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo info) {
                                    setting.setValue(con1[info.getNewIndex()]);
                                    SettingsPanel.this.setModule(SettingsPanel.this.module);
                                }
                            });
                            enumbutton.setIndex(Arrays.asList(con1).indexOf(setting.getValue()));
                            this.addChild(new Component[] { enumbutton});
                        }
                    }
                }
            }

            if (this.children.isEmpty()) {
                this.setVisible(false);
            } else {
                this.setVisible(true);
            }
        }
    }

    public void setModule(Module module) {
        this.module = module;
        this.setMinimumWidth((int) ((float) this.getParent().getWidth() * 0.9F));
        this.prepare();
        this.setAffectLayout(false);
        Iterator iterator = this.children.iterator();

        while (iterator.hasNext()) {
            Component component = (Component) iterator.next();

            component.setWidth(this.getWidth() - 10);
            component.setX(5);
        }

    }

    private static String[] lambda$prepare$1(int x$0) {
        return new String[x$0];
    }

    private static String lambda$prepare$0(Object o) {
        return o.toString().toUpperCase();
    }
}
