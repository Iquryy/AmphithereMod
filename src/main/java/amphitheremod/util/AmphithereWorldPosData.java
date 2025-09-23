package amphitheremod.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AmphithereWorldPosData extends WorldSavedData {
    protected final Map<UUID, BlockPos> lastAmphitherePositions = new HashMap<>();
    private int tickCounter;
    private static final String IDENTIFIER = "amphithere_Positions";

    public AmphithereWorldPosData(String name) {
        super(name);
    }

    public AmphithereWorldPosData() {
        super(IDENTIFIER);
        this.markDirty();
    }

    public static AmphithereWorldPosData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        AmphithereWorldPosData instance = (AmphithereWorldPosData) storage.getOrLoadData(AmphithereWorldPosData.class, IDENTIFIER);
        if (instance == null) {
            instance = new AmphithereWorldPosData();
            storage.setData(IDENTIFIER, instance);
        }

        instance.markDirty();
        return instance;
    }

    public void addAmphithere(UUID uuid, BlockPos pos) {
        this.lastAmphitherePositions.put(uuid, pos);
        this.markDirty();
    }

    public void removeAmphithere(UUID uuid) {
        this.lastAmphitherePositions.remove(uuid);
        this.markDirty();
    }

    @Nullable
    public BlockPos getAmphitherePos(UUID uuid) {
        return this.lastAmphitherePositions.get(uuid);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList nbttaglist = nbt.getTagList("AmphithereMap", 10);
        this.lastAmphitherePositions.clear();

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            UUID uuid = nbttagcompound.getUniqueId("AmphithereUUID");
            BlockPos pos = new BlockPos(nbttagcompound.getInteger("AmphitherePosX"), nbttagcompound.getInteger("AmphitherePosY"), nbttagcompound.getInteger("AmphitherePosZ"));
            this.lastAmphitherePositions.put(uuid, pos);
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("Tick", this.tickCounter);
        NBTTagList nbttaglist = new NBTTagList();

        for (Map.Entry<UUID, BlockPos> pair : this.lastAmphitherePositions.entrySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setUniqueId("AmphithereUUID", pair.getKey());
            nbttagcompound.setInteger("AmphitherePosX", (pair.getValue()).getX());
            nbttagcompound.setInteger("AmphitherePosY", (pair.getValue()).getY());
            nbttagcompound.setInteger("AmphitherePosZ", (pair.getValue()).getZ());
            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("AmphithereMap", nbttaglist);
        return compound;
    }
}