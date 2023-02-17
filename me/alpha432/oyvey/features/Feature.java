//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features;

import me.alpha432.oyvey.util.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.gui.*;
import java.util.*;

public class Feature implements Util
{
    public List<Setting> settings;
    public TextManager renderer;
    private String name;
    
    public Feature() {
        this.settings = new ArrayList<Setting>();
        this.renderer = OyVey.textManager;
    }
    
    public Feature(final String name) {
        this.settings = new ArrayList<Setting>();
        this.renderer = OyVey.textManager;
        this.name = name;
    }
    
    public static boolean nullCheck() {
        return Feature.mc.player == null;
    }
    
    public static boolean fullNullCheck() {
        return Feature.mc.player == null || Feature.mc.world == null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
    
    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }
    
    public boolean isEnabled() {
        return this instanceof Module && ((Module)this).isOn();
    }
    
    public boolean isDisabled() {
        return !this.isEnabled();
    }
    
    public Setting register(final Setting setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && Feature.mc.currentScreen instanceof OyVeyGui) {
            OyVeyGui.getInstance().updateModule((Module)this);
        }
        return setting;
    }
    
    public void unregister(final Setting settingIn) {
        final ArrayList<Setting> removeList = new ArrayList<Setting>();
        for (final Setting setting : this.settings) {
            if (!setting.equals(settingIn)) {
                continue;
            }
            removeList.add(setting);
        }
        if (!removeList.isEmpty()) {
            this.settings.removeAll(removeList);
        }
        if (this instanceof Module && Feature.mc.currentScreen instanceof OyVeyGui) {
            OyVeyGui.getInstance().updateModule((Module)this);
        }
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return setting;
        }
        return null;
    }
    
    public void reset() {
        for (final Setting setting : this.settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }
    
    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }
}
