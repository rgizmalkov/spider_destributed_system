package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import java.io.Serializable;

public interface InfoWrapper {

    Serializable wrapOne(@NonNull WrappingModel wrappingModel, @NonNull String json);

    ImmutableList<Serializable> wrapList(@NonNull WrappingModel wrappingModel, @NonNull String jsons);


}
