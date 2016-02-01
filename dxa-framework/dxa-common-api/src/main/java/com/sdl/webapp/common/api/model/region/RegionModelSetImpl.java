package com.sdl.webapp.common.api.model.region;

import com.sdl.webapp.common.api.model.RegionModel;
import com.sdl.webapp.common.api.model.RegionModelSet;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@ToString
public class RegionModelSetImpl extends AbstractSet<RegionModel> implements RegionModelSet {

    private Map<String, RegionModel> modelMapByName = new LinkedHashMap<>();
    private Map<Class<? extends RegionModel>, Set<RegionModel>> modelMapByClass = new LinkedHashMap<>();

    @Override
    public Iterator<RegionModel> iterator() {
        return modelMapByName.values().iterator();
    }

    @Override
    public int size() {
        return modelMapByName.size();
    }

    @Override
    public boolean add(RegionModel regionModel) {
        if (!Objects.equals(modelMapByName.put(regionModel.getName(), regionModel), regionModel)) {
            Set<RegionModel> modelSet = modelMapByClass.get(regionModel.getClass());
            if (modelSet == null) {
                modelSet = new HashSet<>();
                modelMapByClass.put(regionModel.getClass(), modelSet);
            }
            modelSet.add(regionModel);
            return true;
        }
        return false;
    }

    @Override
    public RegionModel get(String name) {
        return modelMapByName.get(name);
    }

    @Override
    public <T extends RegionModel> Set<T> get(Class<T> clazz) {
        //noinspection unchecked
        return (Set<T>) modelMapByClass.get(clazz);
    }

    @Override
    public boolean containsName(final String name) {
        return modelMapByName.containsKey(name);
    }

    @Override
    public boolean containsClass(Class<? extends RegionModel> clazz) {
        return modelMapByClass.containsKey(clazz);
    }
}
