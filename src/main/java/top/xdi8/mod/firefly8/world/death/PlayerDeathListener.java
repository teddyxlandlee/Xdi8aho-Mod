package top.xdi8.mod.firefly8.world.death;

import com.google.common.base.Suppliers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import top.xdi8.mod.firefly8.advancement.criteria.FireflyCriteria;
import top.xdi8.mod.firefly8.ext.IServerPlayerWithHiddenInventory;
import top.xdi8.mod.firefly8.network.FireflyNetwork;
import top.xdi8.mod.firefly8.stats.FireflyStats;
import top.xdi8.mod.firefly8.world.Xdi8DimensionUtils;

@Mod.EventBusSubscriber(modid = "firefly8")
public class PlayerDeathListener {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof Player player))
            return;
        if (!Xdi8DimensionUtils.canRedirectRespawn(player.getLevel())) return;
        if (!(player instanceof ServerPlayer oldPlayer)) {
            event.setCanceled(true);
            return;
        }
        oldPlayer.unRide();
        oldPlayer.removeEntitiesOnShoulder();
        oldPlayer.setHealth(oldPlayer.getMaxHealth());
        oldPlayer.foodData = new FoodData();
        final IServerPlayerWithHiddenInventory ext = IServerPlayerWithHiddenInventory.xdi8$extend(oldPlayer);
        if (!ext.xdi8$moveItemsToPortal()) {
            FireflyNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(Suppliers.ofInstance(oldPlayer)), FireflyNetwork.S2CDieIndeed.getInstance());
            return;
        }
        event.setCanceled(true);
        if (oldPlayer.level.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            oldPlayer.tellNeutralMobsThatIDied();
        }
        oldPlayer.getLevel().removePlayerImmediately(oldPlayer, Entity.RemovalReason.CHANGED_DIMENSION);

        final ServerPlayer newPlayer =
                oldPlayer.server.getPlayerList().respawn(oldPlayer, true);
        final IServerPlayerWithHiddenInventory newPlayerExt =
                IServerPlayerWithHiddenInventory.xdi8$extend(newPlayer);
        newPlayerExt.xdi8$resetCooldown();
        newPlayerExt.xdi8$passPortalInv(IServerPlayerWithHiddenInventory.xdi8$extend(oldPlayer));
        oldPlayer.connection.player = newPlayer;
        FireflyCriteria.DIE_IN_XDI8AHO.trigger(newPlayer);
        newPlayer.awardStat(FireflyStats.FAKE_DEAD.get());
    }
}
