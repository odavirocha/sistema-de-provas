package dev.odroca.api_provas.service;

import dev.odroca.api_provas.entity.OptionEntity;

import java.util.Set;

public class OptionFactory {

    public static Set<OptionEntity> buildOptions() {
        return Set.of(
            new OptionEntity("2", false),
            new OptionEntity("3", true),
            new OptionEntity("4", false)
        );
    }

}
