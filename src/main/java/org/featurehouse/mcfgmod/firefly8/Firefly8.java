package org.featurehouse.mcfgmod.firefly8;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.featurehouse.mcfgmod.firefly8.item.FireflyItems;
import org.featurehouse.mcfgmod.firefly8.item.potion.vanilla.TintedPotionBrewing;

@Mod("firefly8")
public class Firefly8 {
    public Firefly8() {
        FireflyItems.REGISTRY.register(modBus());
        TintedPotionBrewing.register();
    }

    private static IEventBus modBus() {
        return FMLJavaModLoadingContext.get().getModEventBus();
    }
}
