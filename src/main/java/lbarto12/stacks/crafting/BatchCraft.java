package lbarto12.stacks.crafting;

import java.util.Optional;

import lbarto12.stacks.StackConfig;
import lbarto12.stacks.config.StackLimit;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

public final class BatchCraft {
    private BatchCraft() {
    }

    public static void craft(ServerPlayer player, CraftingContainer craftSlots, ResultContainer resultSlots) {
        ItemStack perCraft = resultSlots.getItem(0);
        if (perCraft.isEmpty()) {
            return;
        }

        Inventory inventory = player.getInventory();
        if (inventory.getSlotWithRemainingSpace(perCraft) == -1 && inventory.getFreeSlot() == -1) {
            return;
        }

        ServerLevel level = player.level();
        CraftingInput.Positioned positioned = craftSlots.asPositionedCraftInput();
        CraftingInput input = positioned.input();
        Optional<RecipeHolder<CraftingRecipe>> recipe = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input,
                level);
        if (recipe.isEmpty()) {
            return;
        }

        NonNullList<ItemStack> remaining = recipe.get().value().getRemainingItems(input);
        int crafts = countCrafts(craftSlots, positioned, remaining, StackLimit.get() / perCraft.getCount());
        ItemStack crafted = perCraft.copyWithCount(perCraft.getCount() * crafts);

        crafted.onCraftedBy(player, crafted.getCount());
        resultSlots.awardUsedRecipes(player, craftSlots.getItems());
        consumeInputs(player, craftSlots, positioned, remaining, crafts);
        inventory.placeItemBackInInventory(crafted, Prediction.PREDICTED);
    }

    private static int gridSlot(CraftingContainer craftSlots, CraftingInput.Positioned positioned, int x, int y) {
        return x + positioned.left() + (y + positioned.top()) * craftSlots.getWidth();
    }

    private static boolean returnsItself(ItemStack stack, ItemStack remainder) {
        return !remainder.isEmpty() && ItemStack.isSameItemSameComponents(stack, remainder);
    }

    private static int countCrafts(CraftingContainer craftSlots, CraftingInput.Positioned positioned,
            NonNullList<ItemStack> remaining, int limit) {
        CraftingInput input = positioned.input();
        int crafts = limit;
        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                ItemStack stack = craftSlots.getItem(gridSlot(craftSlots, positioned, x, y));
                if (!stack.isEmpty() && !returnsItself(stack, remaining.get(x + y * input.width()))) {
                    crafts = Math.min(crafts, stack.getCount());
                }
            }
        }
        return crafts;
    }

    private static void consumeInputs(ServerPlayer player, CraftingContainer craftSlots,
            CraftingInput.Positioned positioned, NonNullList<ItemStack> remaining, int crafts) {
        CraftingInput input = positioned.input();
        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                int slot = gridSlot(craftSlots, positioned, x, y);
                ItemStack stack = craftSlots.getItem(slot);
                ItemStack remainder = remaining.get(x + y * input.width());
                if (stack.isEmpty() || returnsItself(stack, remainder)) {
                    continue;
                }

                craftSlots.removeItem(slot, crafts);
                if (remainder.isEmpty()) {
                    continue;
                }

                int remainderCount = (int) Math.min((long) remainder.getCount() * crafts, StackConfig.MAX_STACK);
                ItemStack remainders = remainder.copyWithCount(remainderCount);
                if (craftSlots.getItem(slot).isEmpty()) {
                    craftSlots.setItem(slot, remainders);
                } else {
                    player.getInventory().placeItemBackInInventory(remainders, Prediction.PREDICTED);
                }
            }
        }
    }
}
