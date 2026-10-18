package amphitheremod.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Save-file record of every Crystal Feather soul bond, shared by all dimensions.
 * It remembers which amphitheres must die when they're next loaded, and which bonds have ended so their feathers shatter.
 */
public class SoulBindData extends WorldSavedData {
    private static final String NAME = "amphitheremod_soul_binds";

    /** amphithere UUID -> owner UUID, for every bond that is still alive. */
    private final Map<UUID, UUID> bonds = new HashMap<>();
    /** Amphitheres whose owner died while they weren't loaded (or were resting in a feather the owner wasn't carrying). They die as soon as they're found. */
    private final Set<UUID> doomedAmphis = new HashSet<>();
    /** Amphitheres that died. Feathers bound to them shatter. */
    private final Set<UUID> deadAmphis = new HashSet<>();

    public SoulBindData(String name) {
        super(name);
    }

    public static SoulBindData get(World world) {
        MapStorage storage = world.getMapStorage();
        SoulBindData data = (SoulBindData) storage.getOrLoadData(SoulBindData.class, NAME);
        if (data == null) {
            data = new SoulBindData(NAME);
            storage.setData(NAME, data);
        }
        return data;
    }

    public void bind(UUID amphi, UUID owner) {
        bonds.put(amphi, owner);
        doomedAmphis.remove(amphi);
        deadAmphis.remove(amphi);
        markDirty();
    }

    public boolean isBound(UUID amphi) { return bonds.containsKey(amphi); }

    @Nullable
    public UUID getOwner(UUID amphi) { return bonds.get(amphi); }

    /** Removes the bond and returns the owner it had, or null. */
    @Nullable
    public UUID breakBond(UUID amphi) {
        UUID owner = bonds.remove(amphi);
        if (owner != null) markDirty();
        return owner;
    }

    public List<UUID> amphisBoundTo(UUID owner) {
        List<UUID> list = new ArrayList<>();
        for (Map.Entry<UUID, UUID> e : bonds.entrySet())
            if (e.getValue().equals(owner)) list.add(e.getKey());
        return list;
    }

    public void doom(UUID amphi) { if (doomedAmphis.add(amphi)) markDirty(); }
    public boolean isDoomed(UUID amphi) { return doomedAmphis.contains(amphi); }

    public void markDead(UUID amphi) {
        doomedAmphis.remove(amphi);
        deadAmphis.add(amphi);
        markDirty();
    }

    /** True when a feather bound to this amphithere should shatter: the amphithere died, or is fated to. */
    public boolean isLost(UUID amphi) { return deadAmphis.contains(amphi) || doomedAmphis.contains(amphi); }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        bonds.clear();
        NBTTagList bondList = nbt.getTagList("Bonds", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < bondList.tagCount(); i++) {
            NBTTagCompound t = bondList.getCompoundTagAt(i);
            if (t.hasUniqueId("Amphi") && t.hasUniqueId("Owner"))
                bonds.put(t.getUniqueId("Amphi"), t.getUniqueId("Owner"));
        }
        readSet(nbt, "Doomed", doomedAmphis);
        readSet(nbt, "Dead", deadAmphis);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList bondList = new NBTTagList();
        for (Map.Entry<UUID, UUID> e : bonds.entrySet()) {
            NBTTagCompound t = new NBTTagCompound();
            t.setUniqueId("Amphi", e.getKey());
            t.setUniqueId("Owner", e.getValue());
            bondList.appendTag(t);
        }
        nbt.setTag("Bonds", bondList);
        writeSet(nbt, "Doomed", doomedAmphis);
        writeSet(nbt, "Dead", deadAmphis);
        return nbt;
    }

    private static void readSet(NBTTagCompound nbt, String key, Set<UUID> set) {
        set.clear();
        NBTTagList list = nbt.getTagList(key, Constants.NBT.TAG_STRING);
        for (int i = 0; i < list.tagCount(); i++) {
            try {
                set.add(UUID.fromString(list.getStringTagAt(i)));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private static void writeSet(NBTTagCompound nbt, String key, Set<UUID> set) {
        NBTTagList list = new NBTTagList();
        for (UUID id : set) list.appendTag(new NBTTagString(id.toString()));
        nbt.setTag(key, list);
    }
}