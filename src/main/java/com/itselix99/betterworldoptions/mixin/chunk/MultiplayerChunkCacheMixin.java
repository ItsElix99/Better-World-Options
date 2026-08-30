package com.itselix99.betterworldoptions.mixin.chunk;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.chunk.BWOChunkGenerator;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.chunk.BWOLimitChunk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.chunk.MultiplayerChunkCache;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(MultiplayerChunkCache.class)
public abstract class MultiplayerChunkCacheMixin implements BWOWorld {
    @Shadow private World world;
    @Shadow private Map chunksByPos = new HashMap();
    @Unique private int width;
    @Unique private int length;
    @Unique private BWOProperties bwoProperties;
    @Unique private boolean finiteWorld;
    @Unique private String finiteWorldType;
    @Unique private String indevWorldType;

    @Shadow public abstract Chunk loadChunk(int chunkX, int chunkZ);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bwo_init(World var1, CallbackInfo ci) {
        this.bwoProperties = (BWOProperties) var1.getProperties();
        this.width = this.bwoProperties.bwo_getIntOptionValue("Width", OptionType.GENERAL_OPTION);
        this.length = this.bwoProperties.bwo_getIntOptionValue("Length", OptionType.GENERAL_OPTION);
        this.finiteWorld = this.bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        this.finiteWorldType = this.bwoProperties.bwo_getStringOptionValue("FiniteWorldType", OptionType.GENERAL_OPTION);
        this.indevWorldType = this.bwoProperties.bwo_getStringOptionValue("IndevWorldType", OptionType.WORLD_TYPE_OPTION);

        if (this.bwoProperties.bwo_getWorldType().equals(BetterWorldOptions.NAMESPACE.id("early_infdev").toString())) {
            BWOChunkGenerator.setSizeLimits(-this.width / 2, this.width / 2, -this.length / 2, this.length / 2);
        } else if (this.finiteWorldType.equals("MCPE") || WorldType.getWorldTypeById(Identifier.of(this.bwoProperties.bwo_getWorldType())).isPregenerateFiniteWorld()) {
            BWOChunkGenerator.setSizeLimits(0, this.width, 0, this.length);
        } else {
            BWOChunkGenerator.setSizeLimits(-this.width / 2, this.width / 2, -this.length / 2, this.length / 2);
        }
    }

    public Chunk bwo_loadFiniteWorldLimitChunk(int chunkX, int chunkZ) {
        if (this.finiteWorld && BWOChunkGenerator.getMultiplayerLimitChunkFiniteWorld(chunkX, chunkZ)) {
            ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
            String limitMode;
            if (this.bwoProperties.bwo_getWorldType().equals(BetterWorldOptions.NAMESPACE.id("indev_20100223").toString()) && !this.finiteWorldType.equals("MCPE")) {
                limitMode = this.indevWorldType;
            } else {
                limitMode = this.finiteWorldType;
            }

            BWOLimitChunk bwoLimitChunk = new BWOLimitChunk(this.world, chunkX, chunkZ, limitMode);
            this.chunksByPos.put(chunkPos, bwoLimitChunk);
            bwoLimitChunk.loaded = true;
            return bwoLimitChunk;
        } else {
            return this.loadChunk(chunkX, chunkZ);
        }
    }
}