//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import me.alpha432.oyvey.util.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import net.minecraft.util.text.*;
import me.alpha432.oyvey.features.modules.misc.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat extends Gui
{
    @Shadow
    private boolean isScrolled;
    private float percentComplete;
    private int newLines;
    private long prevMillis;
    private boolean configuring;
    private float animationPercent;
    private int lineBeingDrawn;
    
    public MixinGuiNewChat() {
        this.prevMillis = System.currentTimeMillis();
    }
    
    @Shadow
    public abstract float getChatScale();
    
    private void updatePercentage(final long diff) {
        if (this.percentComplete < 1.0f) {
            this.percentComplete += 0.004f * diff;
        }
        this.percentComplete = MathUtil.clamp(this.percentComplete, 0.0f, 1.0f);
    }
    
    @Inject(method = { "drawChat" }, at = { @At("HEAD") }, cancellable = true)
    private void modifyChatRendering(final CallbackInfo ci) {
        if (this.configuring) {
            ci.cancel();
            return;
        }
        final long current = System.currentTimeMillis();
        final long diff = current - this.prevMillis;
        this.prevMillis = current;
        this.updatePercentage(diff);
        float t = this.percentComplete;
        this.animationPercent = MathUtil.clamp(1.0f - --t * t * t * t, 0.0f, 1.0f);
    }
    
    @Inject(method = { "drawChat" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal = 0, shift = At.Shift.AFTER) })
    private void translate(final CallbackInfo ci) {
        float y = 1.0f;
        if (!this.isScrolled) {
            y += (9.0f - 9.0f * this.animationPercent) * this.getChatScale();
        }
        GlStateManager.translate(0.0f, y, 0.0f);
    }
    
    @ModifyArg(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, remap = false), index = 0)
    private int getLineBeingDrawn(final int line) {
        return this.lineBeingDrawn = line;
    }
    
    @ModifyArg(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"), index = 3)
    private int modifyTextOpacity(final int original) {
        if (this.lineBeingDrawn <= this.newLines) {
            int opacity = original >> 24 & 0xFF;
            opacity *= (int)this.animationPercent;
            return (original & 0xFFFFFF) | opacity << 24;
        }
        return original;
    }
    
    @Inject(method = { "printChatMessageWithOptionalDeletion" }, at = { @At("HEAD") })
    private void resetPercentage(final CallbackInfo ci) {
        this.percentComplete = 0.0f;
    }
    
    @ModifyVariable(method = { "setChatLine" }, at = @At("STORE"), ordinal = 0)
    private List<ITextComponent> setNewLines(final List<ITextComponent> original) {
        this.newLines = original.size() - 1;
        return original;
    }
    
    @ModifyVariable(method = { "getChatComponent" }, at = @At(value = "STORE", ordinal = 0), ordinal = 3)
    private int modifyX(final int original) {
        return original - 0;
    }
    
    @ModifyVariable(method = { "getChatComponent" }, at = @At(value = "STORE", ordinal = 0), ordinal = 4)
    private int modifyY(final int original) {
        return original + 1;
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.drawRect(left, top, right, bottom, (ChatModifier.getInstance().isOn() && (boolean)ChatModifier.getInstance().clean.getValue()) ? 0 : color);
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
    public int drawnChatLinesSize(final List<ChatLine> list) {
        return (ChatModifier.getInstance().isOn() && (boolean)ChatModifier.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2))
    public int chatLinesSize(final List<ChatLine> list) {
        return (ChatModifier.getInstance().isOn() && (boolean)ChatModifier.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
}
