package org.featurehouse.mcfgmod.firefly8;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import org.featurehouse.mcfgmod.firefly8.client.FireflyParticle;
import org.featurehouse.mcfgmod.firefly8.entity.FireflyEntityTypes;
import org.featurehouse.mcfgmod.firefly8.particle.FireflyParticles;

public class FireflyClientSetup implements Runnable {
    @Override
    public void run() {
        EntityRenderers.register(FireflyEntityTypes.FIREFLY.get(), NoopRenderer::new);
    }

    static void registerParticles(ParticleFactoryRegisterEvent ignore1) {
        Minecraft.getInstance().particleEngine.register(FireflyParticles.FIREFLY.get(),
                pSprites -> (pType, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed) -> {
                    var particle = new FireflyParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
                    particle.pickSprite(pSprites);
                    return particle;
                });
    }
}
