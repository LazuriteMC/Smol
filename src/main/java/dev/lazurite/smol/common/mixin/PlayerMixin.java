package dev.lazurite.smol.common.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    // idk why ImmutableMap.builder didn't work
    @Unique private static final Map<Pose, EntityDimensions> POSES_2 = new HashMap<>();
    @Unique private static final EntityDimensions STANDING_DIMENSIONS_2 = EntityDimensions.scalable(0.6f * 0.125f, 1.8f * 0.125f);

    static {
        POSES_2.put(Pose.STANDING, STANDING_DIMENSIONS_2);
        POSES_2.put(Pose.SLEEPING, EntityDimensions.fixed(0.2f * 0.125f, 0.2f * 0.125f));
        POSES_2.put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6f * 0.125f, 0.6f * 0.125f));
        POSES_2.put(Pose.SWIMMING, EntityDimensions.scalable(0.6f * 0.125f, 0.6f * 0.125f));
        POSES_2.put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6f * 0.125f, 0.6f * 0.125f));
        POSES_2.put(Pose.CROUCHING, EntityDimensions.scalable(0.6f * 0.125f, 1.5f * 0.125f));
        POSES_2.put(Pose.DYING, EntityDimensions.fixed(0.2f * 0.125f, 0.2f * 0.125f));
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    public void getDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> info) {
        info.setReturnValue(POSES_2.getOrDefault(pose, STANDING_DIMENSIONS_2));
    }

    @Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
    public void getStandingEyeHeight(Pose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> info) {
        switch (pose) {
            case SWIMMING:
            case FALL_FLYING:
            case SPIN_ATTACK:
                info.setReturnValue(0.4f * 0.125f);
            case CROUCHING:
                info.setReturnValue(1.27f * 0.125f);
            default:
                info.setReturnValue(1.62f * 0.125f);
        }
    }

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
}