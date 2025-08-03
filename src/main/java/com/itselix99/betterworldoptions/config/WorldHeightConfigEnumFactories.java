package com.itselix99.betterworldoptions.config;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi3.impl.SeptFunction;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import net.glasslauncher.mods.gcapi3.impl.object.entry.EnumConfigEntryHandler;

import java.lang.reflect.*;
import java.util.function.*;

public class WorldHeightConfigEnumFactories implements ConfigFactoryProvider {

    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(WorldHeightConfigEnum.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) ->
        {
            int enumOrdinal;
            if (enumOrOrdinal instanceof Integer ordinal) {
                enumOrdinal = ordinal;
            } else {
                enumOrdinal = ((WorldHeightConfigEnum) enumOrOrdinal).ordinal();
            }
            return new EnumConfigEntryHandler<WorldHeightConfigEnum>(id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrdinal, ((WorldHeightConfigEnum) defaultEnum).ordinal(), WorldHeightConfigEnum.class);
        }));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(WorldHeightConfigEnum.class, enumEntry -> enumEntry);
    }
}