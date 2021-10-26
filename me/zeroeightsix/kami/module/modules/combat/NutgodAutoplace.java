package me.zeroeightsix.kami.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

@Module.Info(
    name = "Autoplace",
    category = Module.Category.COMBAT
)
public class NutgodAutoplace extends Module {

    private Setting autoSwitch = this.register(Settings.b("Auto Switch"));
    private Setting players = this.register(Settings.b("Players"));
    private Setting mobs = this.register(Settings.b("Mobs", false));
    private Setting animals = this.register(Settings.b("Animals", false));
    private Setting place = this.register(Settings.b("Place", false));
    private Setting explode = this.register(Settings.b("Explode", false));
    private Setting alert = this.register(Settings.b("Chat Alert", false));
    private Setting range = this.register(Settings.d("Range", 4.0D));
    private Setting antiWeakness = this.register(Settings.b("Anti Weakness", false));
    private Setting slow = this.register(Settings.b("Slow", false));
    private Setting rotate = this.register(Settings.b("Rotate", true));
    private Setting raytrace = this.register(Settings.b("RayTrace", true));
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime = -1L;
    private static boolean togglePitch = false;
    private boolean switchCooldown = false;
    private boolean isAttacking = false;
    private int oldSlot = -1;
    private int newSlot;
    private int breaks;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener packetListener = new Listener((event) -> {
        Packet packet = event.getPacket();

        if (packet instanceof CPacketPlayer && NutgodAutoplace.isSpoofingAngles) {
            ((CPacketPlayer) packet).yaw = (float) NutgodAutoplace.yaw;
            ((CPacketPlayer) packet).pitch = (float) NutgodAutoplace.pitch;
        }

    }, new Predicate[0]);

    public void onUpdate() {
        EntityEnderCrystal crystal = (EntityEnderCrystal) NutgodAutoplace.mc.world.loadedEntityList.stream().filter((entity) -> {
            return entity instanceof EntityEnderCrystal;
        }).map((entity) -> {
            return (EntityEnderCrystal) entity;
        }).min(Comparator.comparing((c) -> {
            return Float.valueOf(NutgodAutoplace.mc.player.getDistance(c));
        })).orElse((Object) null);
        int crystalSlot;

        if (((Boolean) this.explode.getValue()).booleanValue() && crystal != null && (double) NutgodAutoplace.mc.player.getDistance(crystal) <= ((Double) this.range.getValue()).doubleValue()) {
            if (((Boolean) this.antiWeakness.getValue()).booleanValue() && NutgodAutoplace.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                if (!this.isAttacking) {
                    this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
                    this.isAttacking = true;
                }

                this.newSlot = -1;

                for (crystalSlot = 0; crystalSlot < 9; ++crystalSlot) {
                    ItemStack offhand = Wrapper.getPlayer().inventory.getStackInSlot(crystalSlot);

                    if (offhand != ItemStack.EMPTY) {
                        if (offhand.getItem() instanceof ItemSword) {
                            this.newSlot = crystalSlot;
                            break;
                        }

                        if (offhand.getItem() instanceof ItemTool) {
                            this.newSlot = crystalSlot;
                            break;
                        }
                    }
                }

                if (this.newSlot != -1) {
                    Wrapper.getPlayer().inventory.currentItem = this.newSlot;
                    this.switchCooldown = true;
                }
            }

            this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, NutgodAutoplace.mc.player);
            NutgodAutoplace.mc.playerController.attackEntity(NutgodAutoplace.mc.player, crystal);
            NutgodAutoplace.mc.player.swingArm(EnumHand.MAIN_HAND);
            ++this.breaks;
            if (this.breaks == 2 && !((Boolean) this.slow.getValue()).booleanValue()) {
                if (((Boolean) this.rotate.getValue()).booleanValue()) {
                    resetRotation();
                }

                this.breaks = 0;
                return;
            }

            if (((Boolean) this.slow.getValue()).booleanValue() && this.breaks == 1) {
                if (((Boolean) this.rotate.getValue()).booleanValue()) {
                    resetRotation();
                }

                this.breaks = 0;
                return;
            }
        } else {
            if (((Boolean) this.rotate.getValue()).booleanValue()) {
                resetRotation();
            }

            if (this.oldSlot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }

            this.isAttacking = false;
        }

        crystalSlot = NutgodAutoplace.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? NutgodAutoplace.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int i = 0; i < 9; ++i) {
                if (NutgodAutoplace.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = i;
                    break;
                }
            }
        }

        boolean flag = false;

        if (NutgodAutoplace.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            flag = true;
        } else if (crystalSlot == -1) {
            return;
        }

        List blocks = this.findCrystalBlocks();
        ArrayList entities = new ArrayList();

        if (((Boolean) this.players.getValue()).booleanValue()) {
            entities.addAll((Collection) NutgodAutoplace.mc.world.playerEntities.stream().filter((entityPlayer) -> {
                return !Friends.isFriend(entityPlayer.getName());
            }).collect(Collectors.toList()));
        }

        entities.addAll((Collection) NutgodAutoplace.mc.world.loadedEntityList.stream().filter((entity) -> {
            return EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? (Boolean) this.animals.getValue() : (Boolean) this.mobs.getValue()).booleanValue();
        }).collect(Collectors.toList()));
        BlockPos q = null;
        double damage = 0.5D;
        Iterator f = entities.iterator();

        while (f.hasNext()) {
            Entity result = (Entity) f.next();

            if (result != NutgodAutoplace.mc.player && ((EntityLivingBase) result).getHealth() > 0.0F) {
                Iterator iterator = blocks.iterator();

                while (iterator.hasNext()) {
                    BlockPos blockPos = (BlockPos) iterator.next();
                    double b = result.getDistanceSq(blockPos);

                    if (b < 169.0D) {
                        double d = (double) calculateDamage((double) blockPos.x + 0.5D, (double) (blockPos.y + 1), (double) blockPos.z + 0.5D, result);

                        if (d > damage) {
                            double self = (double) calculateDamage((double) blockPos.x + 0.5D, (double) (blockPos.y + 1), (double) blockPos.z + 0.5D, NutgodAutoplace.mc.player);

                            if ((self <= d || d < (double) ((EntityLivingBase) result).getHealth()) && self - 0.5D <= (double) NutgodAutoplace.mc.player.getHealth()) {
                                damage = d;
                                q = blockPos;
                                this.renderEnt = result;
                            }
                        }
                    }
                }
            }
        }

        if (damage == 0.5D) {
            this.render = null;
            this.renderEnt = null;
            if (((Boolean) this.rotate.getValue()).booleanValue()) {
                resetRotation();
            }

        } else {
            this.render = q;
            if (((Boolean) this.place.getValue()).booleanValue()) {
                if (!flag && NutgodAutoplace.mc.player.inventory.currentItem != crystalSlot) {
                    if (((Boolean) this.autoSwitch.getValue()).booleanValue()) {
                        NutgodAutoplace.mc.player.inventory.currentItem = crystalSlot;
                        if (((Boolean) this.rotate.getValue()).booleanValue()) {
                            resetRotation();
                        }

                        this.switchCooldown = true;
                    }

                    return;
                }

                this.lookAtPacket((double) q.x + 0.5D, (double) q.y - 0.5D, (double) q.z + 0.5D, NutgodAutoplace.mc.player);
                EnumFacing enumfacing;

                if (((Boolean) this.raytrace.getValue()).booleanValue()) {
                    RayTraceResult raytraceresult = NutgodAutoplace.mc.world.rayTraceBlocks(new Vec3d(NutgodAutoplace.mc.player.posX, NutgodAutoplace.mc.player.posY + (double) NutgodAutoplace.mc.player.getEyeHeight(), NutgodAutoplace.mc.player.posZ), new Vec3d((double) q.x + 0.5D, (double) q.y - 0.5D, (double) q.z + 0.5D));

                    if (raytraceresult != null && raytraceresult.sideHit != null) {
                        enumfacing = raytraceresult.sideHit;
                    } else {
                        enumfacing = EnumFacing.UP;
                    }

                    if (this.switchCooldown) {
                        this.switchCooldown = false;
                        return;
                    }
                } else {
                    enumfacing = EnumFacing.UP;
                }

                NutgodAutoplace.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(q, enumfacing, flag ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            }

            if (NutgodAutoplace.isSpoofingAngles) {
                if (NutgodAutoplace.togglePitch) {
                    NutgodAutoplace.mc.player.rotationPitch = (float) ((double) NutgodAutoplace.mc.player.rotationPitch + 4.0E-4D);
                    NutgodAutoplace.togglePitch = false;
                } else {
                    NutgodAutoplace.mc.player.rotationPitch = (float) ((double) NutgodAutoplace.mc.player.rotationPitch - 4.0E-4D);
                    NutgodAutoplace.togglePitch = true;
                }
            }

        }
    }

    public void onWorldRender(RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, 1707017958, 63);
            KamiTessellator.release();
            if (this.renderEnt != null) {
                Vec3d vec3d = EntityUtil.getInterpolatedRenderPos(this.renderEnt, NutgodAutoplace.mc.getRenderPartialTicks());
            }
        }

    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);

        setYawAndPitch((float) v[0], (float) v[1]);
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);

        return (NutgodAutoplace.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || NutgodAutoplace.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && NutgodAutoplace.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && NutgodAutoplace.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && NutgodAutoplace.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(NutgodAutoplace.mc.player.posX), Math.floor(NutgodAutoplace.mc.player.posY), Math.floor(NutgodAutoplace.mc.player.posZ));
    }

    private List findCrystalBlocks() {
        NonNullList positions = NonNullList.create();

        positions.addAll((Collection) this.getSphere(getPlayerPos(), ((Double) this.range.getValue()).floatValue(), ((Double) this.range.getValue()).intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public List getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList circleblocks = new ArrayList();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x) {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z) {
                for (int y = sphere ? cy - (int) r : cy; (float) y < (sphere ? (float) cy + r : (float) (cy + h)); ++y) {
                    double dist = (double) ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));

                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);

                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0F;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = (double) entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedsize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 9.0D * (double) doubleExplosionSize + 1.0D));
        double finald = 1.0D;

        if (entity instanceof EntityLivingBase) {
            finald = (double) getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(NutgodAutoplace.mc.world, (Entity) null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);

            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp((float) k, 0.0F, 20.0F);

            damage *= 1.0F - f / 25.0F;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0F;
            }

            damage = Math.max(damage - ep.getAbsorptionAmount(), 0.0F);
            return damage;
        } else {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            return damage;
        }
    }

    private static float getDamageMultiplied(float damage) {
        int diff = NutgodAutoplace.mc.world.getDifficulty().getId();

        return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
    }

    public static float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        NutgodAutoplace.yaw = (double) yaw1;
        NutgodAutoplace.pitch = (double) pitch1;
        NutgodAutoplace.isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (NutgodAutoplace.isSpoofingAngles) {
            NutgodAutoplace.yaw = (double) NutgodAutoplace.mc.player.rotationYaw;
            NutgodAutoplace.pitch = (double) NutgodAutoplace.mc.player.rotationPitch;
            NutgodAutoplace.isSpoofingAngles = false;
        }

    }

    protected void onEnable() {
        if (((Boolean) this.alert.getValue()).booleanValue()) {
            Command.sendChatMessage("§5AutoCrystal §8ON");
        }

    }

    public void onDisable() {
        if (((Boolean) this.alert.getValue()).booleanValue()) {
            Command.sendChatMessage("§5 AutoCrystal §8OFF");
        }

        this.render = null;
        this.renderEnt = null;
        resetRotation();
    }
}
