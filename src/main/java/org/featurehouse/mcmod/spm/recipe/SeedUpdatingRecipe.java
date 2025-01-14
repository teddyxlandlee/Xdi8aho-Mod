package org.featurehouse.mcmod.spm.recipe;

import com.google.gson.JsonObject;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.featurehouse.mcmod.spm.SPMMain;
import org.featurehouse.mcmod.spm.platform.api.recipe.SimpleRecipeSerializer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record SeedUpdatingRecipe(ResourceLocation id, Ingredient base,
                                 Ingredient addition,
                                 ItemStack result) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container inv, Level world) {
        return this.base.test(inv.getItem(0)) && this.addition.test(inv.getItem(1)) &&
                !this.base.test(result);
    }

    @Override
    public ItemStack assemble(Container inv) {
        ItemStack itemStack = this.result.copy();
        CompoundTag compoundTag = inv.getItem(0).getTag();
        if (compoundTag != null) {
            itemStack.setTag(compoundTag.copy());
        }

        return itemStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Deprecated
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(SPMMain.SEED_UPDATER.get());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SPMMain.SEED_UPDATING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SPMMain.SEED_UPDATING_RECIPE_TYPE.get();
    }

    public boolean matchesAddition(ItemStack itemStack) {
        return this.addition.test(itemStack);
    }

    public static class Serializer extends SimpleRecipeSerializer<SeedUpdatingRecipe> {
        @Override
        public SeedUpdatingRecipe readJson(ResourceLocation identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "base"));
            Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "addition"));
            ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new SeedUpdatingRecipe(identifier, ingredient, ingredient2, itemStack);
        }

        @Override
        public SeedUpdatingRecipe readPacket(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromNetwork(packetByteBuf);
            Ingredient ingredient2 = Ingredient.fromNetwork(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItem();
            return new SeedUpdatingRecipe(identifier, ingredient, ingredient2, itemStack);
        }

        @Override
        public void writePacket(FriendlyByteBuf buf, @NotNull SeedUpdatingRecipe recipe) {
            recipe.base.toNetwork(buf);
            recipe.addition.toNetwork(buf);
            buf.writeItem(recipe.result);
        }
    }
}