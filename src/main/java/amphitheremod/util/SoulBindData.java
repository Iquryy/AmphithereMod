package amphitheremod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Crystal Feather soul bonds, saved with the world (stored on the overworld so every dimension shares it).
 * bonds: amphithere -> owner. doomed: the owner died while the amphithere was unloaded, so it dies when next loaded.
 * dead: the amphithere is gone, so its feather shatters.
 */
public class SoulBindData extends SavedData {
    private static final String NAME = "amphitheremod_soul_binds";
    private final Map<UUID, UUID> bonds = new HashMap<>();
    private final Set<UUID> doomedAmphis = new HashSet<>();
    private final Set<UUID> deadAmphis = new HashSet<>();

    public static SoulBindData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(SoulBindData::load, SoulBindData::new, NAME);
    }

    public void bind(UUID amphi, UUID owner) {
        this.bonds.put(amphi, owner);
        this.doomedAmphis.remove(amphi);
        this.deadAmphis.remove(amphi);
        setDirty();
    }

    public boolean isBound(UUID amphi) {
        return this.bonds.containsKey(amphi);
    }

    /** Removes the bond; returns the owner it had, or null. */
    @Nullable
    public UUID breakBond(UUID amphi) {
        UUID owner = this.bonds.remove(amphi);
        if (owner != null) {
            setDirty();
        }
        return owner;
    }

    public List<UUID> amphisBoundTo(UUID owner) {
        List<UUID> list = new ArrayList<>();
        this.bonds.forEach((amphi, o) -> {
            if (o.equals(owner)) {
                list.add(amphi);
            }
        });
        return list;
    }

    public void doom(UUID amphi) {
        if (this.doomedAmphis.add(amphi)) {
            setDirty();
        }
    }

    public boolean isDoomed(UUID amphi) {
        return this.doomedAmphis.contains(amphi);
    }

    public void markDead(UUID amphi) {
        this.doomedAmphis.remove(amphi);
        this.deadAmphis.add(amphi);
        setDirty();
    }

    /** The amphithere died or is doomed, so its feather should shatter. */
    public boolean isLost(UUID amphi) {
        return this.deadAmphis.contains(amphi) || this.doomedAmphis.contains(amphi);
    }

    private static SoulBindData load(CompoundTag nbt) {
        SoulBindData data = new SoulBindData();
        ListTag bondList = nbt.getList("Bonds", Tag.TAG_COMPOUND);
        for (int i = 0; i < bondList.size(); i++) {
            CompoundTag t = bondList.getCompound(i);
            if (t.hasUUID("Amphi") && t.hasUUID("Owner")) {
                data.bonds.put(t.getUUID("Amphi"), t.getUUID("Owner"));
            }
        }
        readSet(nbt, "Doomed", data.doomedAmphis);
        readSet(nbt, "Dead", data.deadAmphis);
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag bondList = new ListTag();
        this.bonds.forEach((amphi, owner) -> {
            CompoundTag t = new CompoundTag();
            t.putUUID("Amphi", amphi);
            t.putUUID("Owner", owner);
            bondList.add(t);
        });
        nbt.put("Bonds", bondList);
        writeSet(nbt, "Doomed", this.doomedAmphis);
        writeSet(nbt, "Dead", this.deadAmphis);
        return nbt;
    }

    private static void readSet(CompoundTag nbt, String key, Set<UUID> set) {
        ListTag list = nbt.getList(key, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            try {
                set.add(UUID.fromString(list.getString(i)));
            } catch (IllegalArgumentException ignored) {
                // A hand-edited or corrupt entry; skip it
            }
        }
    }

    private static void writeSet(CompoundTag nbt, String key, Set<UUID> set) {
        ListTag list = new ListTag();
        set.forEach(id -> list.add(StringTag.valueOf(id.toString())));
        nbt.put(key, list);
    }
}
