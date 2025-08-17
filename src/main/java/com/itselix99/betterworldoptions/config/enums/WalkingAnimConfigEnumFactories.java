package com.itselix99.betterworldoptions.config.enums;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi3.impl.SeptFunction;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import net.glasslauncher.mods.gcapi3.impl.object.entry.EnumConfigEntryHandler;

import java.lang.reflect.*;
import java.util.function.*;

public class WalkingAnimConfigEnumFactories implements ConfigFactoryProvider {

    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(WalkingAnimConfigEnum.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) ->
        {
            int enumOrdinal;
            if (enumOrOrdinal instanceof Integer ordinal) {
                enumOrdinal = ordinal;
            } else {
                enumOrdinal = ((WalkingAnimConfigEnum) enumOrOrdinal).ordinal();
            }
            return new EnumConfigEntryHandler<WalkingAnimConfigEnum>(id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrdinal, ((WalkingAnimConfigEnum) defaultEnum).ordinal(), WalkingAnimConfigEnum.class);
        }));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(WalkingAnimConfigEnum.class, enumEntry -> enumEntry);
    }
}