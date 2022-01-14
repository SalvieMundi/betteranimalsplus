package dev.itsmeow.betteranimalsplus.common.entity.util;

import java.util.Random;

import dev.itsmeow.betteranimalsplus.mixin.AgeableMobGroupDataAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.AgeableMob.AgeableMobGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;

public class EntityUtil {

    public static SpawnGroupData childChance(AgeableMob e, MobSpawnType reason, SpawnGroupData livingdata, float chance) {
        if(livingdata instanceof AgeableMobGroupData) {
            ((AgeableMobGroupDataAccessor) livingdata).setBabySpawnChance(chance);
        }
        return livingdata;
    }

    public static boolean canSpawn(EntityType<?> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        return world.dimensionType().bedWorks();
    }

    public static boolean canMonsterSpawn(EntityType<? extends Monster> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        return canSpawn(type, world, reason, pos, rand) && Monster.checkMonsterSpawnRules(type, world, reason, pos, rand);
    }

    public static boolean canMobSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        return canSpawn(type, (ServerLevelAccessor)world, reason, pos, rand) && Mob.checkMobSpawnRules(type, world, reason, pos, rand);
    }
}
