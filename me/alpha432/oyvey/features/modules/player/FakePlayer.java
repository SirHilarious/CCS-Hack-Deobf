//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import java.util.*;
import com.mojang.authlib.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;

public class FakePlayer extends Module
{
    private final String name = "popbob_the_fakeplayer";
    private Setting<Boolean> clone;
    private int entityID;
    
    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Module.Category.PLAYER, false, false, false);
        this.clone = (Setting<Boolean>)this.register(new Setting("CloneLocal", (T)true));
    }
    
    public void onEnable() {
        final GameProfile profile;
        final EntityOtherPlayerMP fakePlayer;
        new Thread(() -> {
            profile = new GameProfile(UUID.fromString("0f75a81d-70e5-43c5-b892-f33c524284f2"), "popbob_the_fakeplayer");
            fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, profile);
            fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
            fakePlayer.setHealth(FakePlayer.mc.player.getHealth() + FakePlayer.mc.player.getAbsorptionAmount());
            if (this.clone.getValue()) {
                fakePlayer.inventory = FakePlayer.mc.player.inventory;
            }
            FakePlayer.mc.world.addEntityToWorld(-133742069, (Entity)fakePlayer);
            this.entityID = -133742069;
        }).start();
    }
    
    public void onDisable() {
        if (this.entityID == -133742069) {
            FakePlayer.mc.world.removeEntityFromWorld(this.entityID);
            this.entityID = 0;
        }
    }
}
