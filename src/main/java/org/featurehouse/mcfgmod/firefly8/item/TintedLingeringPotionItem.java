package org.featurehouse.mcfgmod.firefly8.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TintedLingeringPotionItem extends LingeringPotionItem {
    public TintedLingeringPotionItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        // NO-OP: YOU CANNOT SEE ANYTHING INSIDE
    }
}
