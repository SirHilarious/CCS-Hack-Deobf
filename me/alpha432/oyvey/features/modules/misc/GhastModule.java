//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.features.setting.*;
import java.awt.*;
import net.minecraft.entity.monster.*;
import java.util.*;

public class GhastModule extends Module
{
    private Set<Entity> entities;
    private SystemTray tray;
    private TrayIcon trayIcon;
    private Setting<Boolean> notifications;
    private Setting<Boolean> packetFly;
    
    public GhastModule() {
        super("GhastModule", "Modifies behaviour in presence of a Ghast", Category.MISC, true, false, false);
        this.entities = new HashSet<Entity>();
        this.notifications = (Setting<Boolean>)this.register(new Setting("Notifications", (T)true));
        this.packetFly = (Setting<Boolean>)this.register(new Setting("Disable PFly", (T)false));
        try {
            final SystemTray tray = SystemTray.getSystemTray();
            final Image image = Toolkit.getDefaultToolkit().createImage("");
            (this.trayIcon = new TrayIcon(image, "CCSHack Notifier")).setImageAutoSize(true);
            this.trayIcon.setToolTip("CCSHack Notifcations");
            tray.add(this.trayIcon);
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void onUpdate() {
        for (final Entity entity : GhastModule.mc.world.loadedEntityList) {
            if (entity instanceof EntityGhast) {
                if (this.entities.contains(entity)) {
                    continue;
                }
                if (this.notifications.getValue()) {
                    this.sendNotification();
                }
                if (this.packetFly.getValue()) {
                    GhastModule.mc.player.sendChatMessage(".toggle packetfly");
                }
                this.entities.add(entity);
            }
        }
    }
    
    private void sendNotification() {
        try {
            this.trayIcon.displayMessage("Ghast Sighted", "A ghast has spawned", TrayIcon.MessageType.INFO);
        }
        catch (Exception ex) {
            System.err.print(ex);
        }
    }
}
