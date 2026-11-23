package amphitheremod.mixin;

import amphitheremod.config.AmphiConfig;
import amphitheremod.menu.AmphithereMenu;
import amphitheremod.registry.AmphiItems;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Right click handling added in front of Ice and Fire's: the owner's shift-right-click opens the inventory (Ice and
 * Fire used it to cycle the command; the inventory has a button for that), the two cookies, cocoa beans doing nothing
 * at full health, and cocoa beans healing a share of max health plus some stamina.
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphiInteractMixin {
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void amphiMod$interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        ItemStack stack = player.getItemInHand(hand);
        boolean client = amphi.level().isClientSide;

        if (AmphiConfig.ENABLE_INVENTORY.get() && amphi.isTame() && amphi.isOwnedBy(player) && player.isShiftKeyDown()) {
            if (!client && player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                                (id, inv, p) -> new AmphithereMenu(id, inv, amphi), amphi.getDisplayName()),
                        buf -> buf.writeVarInt(amphi.getId()));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(client));
            return;
        }

        // XXL Chocolate Cookie: puts it in love and gives a long Health Boost
        if (stack.is(AmphiItems.XXL_CHOCOLATE_COOKIE.get()) && AmphiConfig.ENABLE_XXL_COOKIE.get() && !amphi.isInLove()) {
            if (!client) {
                amphi.setInLove(player);
                amphi.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                amphi.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, AmphiConfig.XXL_COOKIE_DURATION.get() * 20, AmphiConfig.XXL_COOKIE_LEVEL.get()));
                amphiMod$consume(player, stack);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(client));
            return;
        }

        // Bloody Cookie: permanent COTH immunity (only once, and only when a mod adds COTH)
        if (stack.is(AmphiItems.ANTI_COTH_COOKIE.get()) && AmphiConfig.ENABLE_ANTI_COTH_COOKIE.get() && ModList.get().isLoaded("epca")) {
            IAmphithereData data = (IAmphithereData) amphi;
            if (data.amphiMod$isCothImmune()) {
                if (!client) {
                    player.displayClientMessage(Component.translatable("amphitheremod.msg.already_coth_immune"), true);
                }
            } else if (!client) {
                amphi.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                amphi.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.5F);
                data.amphiMod$makeCothImmune();
                amphiMod$consume(player, stack);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(client));
            return;
        }

        // Don't waste cocoa beans on a full health amphithere (Ice and Fire eats one anyway)
        if (stack.is(IafItemTags.HEAL_AMPITHERE) && amphi.getHealth() >= amphi.getMaxHealth()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    // Cocoa beans: heal max health / divisor (10%) instead of a flat 5, and restore some stamina
    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;heal(F)V"))
    private void amphiMod$beanHeal(EntityAmphithere amphi, float amount) {
        if (!AmphiConfig.CHANGE_COCOA_HEAL.get()) {
            amphi.heal(amount);
            return;
        }
        IAmphithereData data = (IAmphithereData) amphi;
        data.amphiMod$setStamina(data.amphiMod$getStamina() + data.amphiMod$getMaxStamina() * AmphiConfig.BEAN_STAMINA.get().floatValue());
        amphi.heal(amphi.getMaxHealth() / AmphiConfig.HEAL_DIVISOR.get());
    }

    @Unique
    private static void amphiMod$consume(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }
}
