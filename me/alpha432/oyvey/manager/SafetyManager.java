//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.*;
import java.util.concurrent.atomic.*;
import me.alpha432.oyvey.features.modules.combat.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import java.util.*;
import java.util.concurrent.*;

public class SafetyManager extends Feature implements Runnable
{
    private final Timer syncTimer;
    private final AtomicBoolean SAFE;
    private ScheduledExecutorService service;
    
    public SafetyManager() {
        this.syncTimer = new Timer();
        this.SAFE = new AtomicBoolean(false);
    }
    
    public void run() {
        if (PhobosCA.getInstance().isOff() || PhobosCA.getInstance().threadMode.getValue() == PhobosCA.ThreadMode.NONE) {
            this.doSafetyCheck();
        }
    }
    
    public void doSafetyCheck() {
        if (!fullNullCheck()) {
            boolean safe = true;
            final EntityPlayer entityPlayer;
            final EntityPlayer closest = entityPlayer = EntityUtil.getClosestEnemy(18.0);
            if (closest == null) {
                this.SAFE.set(true);
                return;
            }
            final ArrayList<Entity> crystals = new ArrayList<Entity>(SafetyManager.mc.world.loadedEntityList);
            for (final Entity crystal : crystals) {
                if (crystal instanceof EntityEnderCrystal && DamageUtil.calculateDamage(crystal, (Entity)SafetyManager.mc.player) > 4.0) {
                    if (closest != null && closest.getDistanceSq(crystal) >= 40.0) {
                        continue;
                    }
                    safe = false;
                    break;
                }
            }
            if (safe) {
                for (final BlockPos pos : BlockUtil.possiblePlacePositions(4.0f, false)) {
                    if (DamageUtil.calculateDamage(pos, (Entity)SafetyManager.mc.player) > 4.0) {
                        if (closest != null && closest.getDistanceSq(pos) >= 40.0) {
                            continue;
                        }
                        safe = false;
                        break;
                    }
                }
            }
            this.SAFE.set(safe);
        }
    }
    
    public void onUpdate() {
        this.run();
    }
    
    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "§aSecure";
        }
        return "§cUnsafe";
    }
    
    public boolean isSafe() {
        return this.SAFE.get();
    }
    
    public ScheduledExecutorService getService() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, 0L, TimeUnit.MILLISECONDS);
        return service;
    }
}
