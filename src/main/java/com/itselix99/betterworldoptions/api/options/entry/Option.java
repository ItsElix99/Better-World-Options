package com.itselix99.betterworldoptions.api.options.entry;

import com.itselix99.betterworldoptions.api.options.GeneralOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Option<T> {
    private final int id;
    private final String displayName;
    private final String name;
    private final String[] description;
    private final OptionType optionType;
    private boolean visible = true;
    private boolean save = true;
    private String compatibleType = "All";
    private final Set<Identifier> compatibleWorldTypes = new HashSet<>();
    private final List<Option<?>> dependentOptions = new ArrayList<>();
    private Option<?> parentOption;

    public Option(String displayName, String name, String[] description, OptionType optionType) {
        this.id = GeneralOptions.getGeneralOptionsList().size();
        this.displayName = displayName;
        this.name = name;
        this.description = description;
        this.optionType = optionType;
        if (this.optionType == OptionType.GENERAL_OPTION) {
            GeneralOptions.getGeneralOptionsList().add(this);
        }
    }

    public Option(int id, String displayName, String name, String[] description, OptionType optionType) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.description = description;
        this.optionType = optionType;
        if (this.optionType == OptionType.GENERAL_OPTION) {
            GeneralOptions.getGeneralOptionsList().add(this);
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setCompatibleType(String compatibleType) {
        this.compatibleType = compatibleType;
    }

    public void setParentOption(Option<?> parentOption) {
        this.parentOption = parentOption;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public String[] getDescription() {
        return this.description;
    }

    public OptionType getOptionType() {
        return this.optionType;
    }

    public T getDefaultValue() {
        return null;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean allowSave() {
        return this.save;
    }

    public String getCompatibleType() {
        return this.compatibleType;
    }

    public Set<Identifier> getCompatibleWorldTypes() {
        return this.compatibleWorldTypes;
    }

    public List<Option<?>> getDependentOptions() {
        return this.dependentOptions;
    }

    public Option<?> getParentOption() {
        return this.parentOption;
    }

    public void addCompatibleWorldType(Identifier worldTypeId) {
        this.compatibleWorldTypes.add(worldTypeId);
    }

    public void addDependentOptions(Option<?> dependentOption) {
        this.dependentOptions.add(dependentOption);
    }
}