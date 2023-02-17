//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.text.*;

public class FakeKick extends Module
{
    private final Setting<Boolean> healthDisplay;
    
    public FakeKick() {
        super("FakeKick", "Log with the press of a button", Category.PLAYER, true, false, false);
        this.healthDisplay = (Setting<Boolean>)this.register(new Setting("HealthDisplay", (T)false));
    }
    
    @Override
    public void onEnable() {
        if (this.healthDisplay.getValue()) {
            final float health = FakeKick.mc.player.getAbsorptionAmount() + FakeKick.mc.player.getHealth();
            Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("Logged out with " + health + " health remaining.")));
            this.disable();
        }
        Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("Internal Exception: java.lang.NullPointerException")));
        this.disable();
    }
}
