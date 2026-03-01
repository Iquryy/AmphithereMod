package amphitheremod.mixin.common.beaks;

import amphitheremod.handlers.ModItemRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAmphithere.class)
public class NormalBeakEffects {

    @Inject(method = "attackEntityAsMob", at = @At(value = "HEAD"))
    void aaa(Entity entityIn, CallbackInfoReturnable<Boolean> cir) {
        amphithereMod$onAttack();
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 3))
    void bbb(CallbackInfo ci) {
        amphithereMod$onAttack();
    }

    @Unique
    void amphithereMod$onAttack() {
        EntityAmphithere amphithere = (EntityAmphithere) (Object) this;
        EntityLivingBase victim;
        if (amphithere.isBeingRidden() && amphithere.getRidingEntity() instanceof EntityPlayer && amphithere.isTamed())
            victim = DragonUtils.riderLookingAtEntity(amphithere, ((EntityLivingBase) amphithere.getRidingEntity()), 2.5F);
        else
            victim = DragonUtils.riderLookingAtEntity(amphithere, amphithere, 2.5F);

        if (victim == null) return;

        if (amphithere.getHeldItemMainhand().getItem() == ModItemRegistry.AMPHITHERE_FIRE_DRAGON_BONE_BEAK_ATTACHMENT) {
            if (victim == amphithere.getOwner()) return;
            if (victim instanceof EntityIceDragon)
                victim.attackEntityFrom(DamageSource.IN_FIRE, 13.5F);
            victim.setFire(5);
            victim.knockBack(victim, 1.0F, amphithere.posX - victim.posX, amphithere.posZ - victim.posZ);
        } else if (amphithere.getHeldItemMainhand().getItem() == ModItemRegistry.AMPHITHERE_ICED_DRAGON_BONE_BEAK_ATTACHMENT) {
            if (victim == amphithere.getOwner()) return;
            if (victim instanceof EntityFireDragon)
                victim.attackEntityFrom(DamageSource.DROWN, 13.5F);
            if (!victim.world.isRemote) {
                FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(victim, FrozenEntityProperties.class);
                frozenProps.setFrozenFor(200);
                victim.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
                victim.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100, 2));
                victim.knockBack(victim, 1.0F, amphithere.posX - victim.posX, victim.posZ - victim.posZ);
            }
            victim.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
            victim.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100, 2));
            victim.knockBack(victim, 1.0F, amphithere.posX - victim.posX, amphithere.posZ - victim.posZ);
        } else if (amphithere.getHeldItemMainhand().getItem() == ModItemRegistry.AMPHITHERE_SILVER_BEAK_ATTACHMENT) {
            if (victim.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
                victim.attackEntityFrom(DamageSource.MAGIC, +2.0F);
        }
    }
}