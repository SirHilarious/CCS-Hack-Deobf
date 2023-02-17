//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin;

import net.minecraftforge.fml.relauncher.*;
import me.alpha432.oyvey.*;
import org.spongepowered.asm.launch.*;
import org.spongepowered.asm.mixin.*;
import java.util.*;
import me.alpha432.oyvey.util.*;

public class OyVeyLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public OyVeyLoader() {
        OyVey.LOGGER.info("Loading mixins...");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.oyvey.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        OyVey.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        OyVeyLoader.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return Transformer.class.getName();
    }
    
    static {
        OyVeyLoader.isObfuscatedEnvironment = false;
    }
}
