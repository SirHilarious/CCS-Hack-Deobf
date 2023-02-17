//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package org.spongepowered.tools.agent;

import org.spongepowered.asm.mixin.transformer.ext.*;
import org.spongepowered.asm.mixin.transformer.*;
import org.apache.logging.log4j.*;
import java.util.*;
import java.security.*;
import org.spongepowered.asm.mixin.transformer.throwables.*;
import java.lang.instrument.*;
import org.spongepowered.asm.service.*;

public class MixinAgent implements IHotSwap
{
    public static final byte[] ERROR_BYTECODE;
    static final MixinAgentClassLoader classLoader;
    static final Logger logger;
    static Instrumentation instrumentation;
    private static List<MixinAgent> agents;
    final MixinTransformer classTransformer;
    
    public MixinAgent(final MixinTransformer classTransformer) {
        this.classTransformer = classTransformer;
        MixinAgent.agents.add(this);
        if (MixinAgent.instrumentation != null) {
            this.initTransformer();
        }
    }
    
    private void initTransformer() {
        MixinAgent.instrumentation.addTransformer(new Transformer(), true);
    }
    
    public void registerMixinClass(final String name) {
        MixinAgent.classLoader.addMixinClass(name);
    }
    
    public void registerTargetClass(final String name, final byte[] bytecode) {
        MixinAgent.classLoader.addTargetClass(name, bytecode);
    }
    
    public static void init(final Instrumentation instrumentation) {
        MixinAgent.instrumentation = instrumentation;
        if (!MixinAgent.instrumentation.isRedefineClassesSupported()) {
            MixinAgent.logger.error("The instrumentation doesn't support re-definition of classes");
        }
        for (final MixinAgent agent : MixinAgent.agents) {
            agent.initTransformer();
        }
    }
    
    public static void premain(final String arg, final Instrumentation instrumentation) {
        System.setProperty("mixin.hotSwap", "true");
        init(instrumentation);
    }
    
    public static void agentmain(final String arg, final Instrumentation instrumentation) {
        init(instrumentation);
    }
    
    static {
        ERROR_BYTECODE = new byte[] { 1 };
        classLoader = new MixinAgentClassLoader();
        logger = LogManager.getLogger("mixin.agent");
        MixinAgent.instrumentation = null;
        MixinAgent.agents = new ArrayList<MixinAgent>();
    }
    
    class Transformer implements ClassFileTransformer
    {
        @Override
        public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain domain, final byte[] classfileBuffer) throws IllegalClassFormatException {
            if (classBeingRedefined == null) {
                return null;
            }
            final byte[] mixinBytecode = MixinAgent.classLoader.getFakeMixinBytecode(classBeingRedefined);
            if (mixinBytecode != null) {
                final List<String> targets = this.reloadMixin(className, classfileBuffer);
                if (targets == null || !this.reApplyMixins(targets)) {
                    return MixinAgent.ERROR_BYTECODE;
                }
                return mixinBytecode;
            }
            else {
                try {
                    MixinAgent.logger.info("Redefining class " + className);
                    return MixinAgent.this.classTransformer.transformClassBytes((String)null, className, classfileBuffer);
                }
                catch (Throwable th) {
                    MixinAgent.logger.error("Error while re-transforming class " + className, th);
                    return MixinAgent.ERROR_BYTECODE;
                }
            }
        }
        
        private List<String> reloadMixin(final String className, final byte[] classfileBuffer) {
            MixinAgent.logger.info("Redefining mixin {}", new Object[] { className });
            try {
                return (List<String>)MixinAgent.this.classTransformer.reload(className.replace('/', '.'), classfileBuffer);
            }
            catch (MixinReloadException e) {
                MixinAgent.logger.error("Mixin {} cannot be reloaded, needs a restart to be applied: {} ", new Object[] { e.getMixinInfo(), e.getMessage() });
            }
            catch (Throwable th) {
                MixinAgent.logger.error("Error while finding targets for mixin " + className, th);
            }
            return null;
        }
        
        private boolean reApplyMixins(final List<String> targets) {
            final IMixinService service = MixinService.getService();
            for (final String target : targets) {
                final String targetName = target.replace('/', '.');
                MixinAgent.logger.debug("Re-transforming target class {}", new Object[] { target });
                try {
                    final Class<?> targetClass = (Class<?>)service.getClassProvider().findClass(targetName);
                    byte[] targetBytecode = MixinAgent.classLoader.getOriginalTargetBytecode(targetName);
                    if (targetBytecode == null) {
                        MixinAgent.logger.error("Target class {} bytecode is not registered", new Object[] { targetName });
                        return false;
                    }
                    targetBytecode = MixinAgent.this.classTransformer.transformClassBytes((String)null, targetName, targetBytecode);
                    MixinAgent.instrumentation.redefineClasses(new ClassDefinition(targetClass, targetBytecode));
                }
                catch (Throwable th) {
                    MixinAgent.logger.error("Error while re-transforming target class " + target, th);
                    return false;
                }
            }
            return true;
        }
    }
}
