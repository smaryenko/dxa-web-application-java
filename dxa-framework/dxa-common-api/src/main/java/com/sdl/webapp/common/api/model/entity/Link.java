package com.sdl.webapp.common.api.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sdl.webapp.common.api.mapping.semantic.annotations.SemanticEntity;
import com.sdl.webapp.common.api.mapping.semantic.annotations.SemanticProperties;
import com.sdl.webapp.common.api.mapping.semantic.annotations.SemanticProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.sdl.webapp.common.api.mapping.semantic.config.SemanticVocabulary.SDL_CORE;

@SemanticEntity(entityName = "EmbeddedLink", vocabulary = SDL_CORE, prefix = "e")
@Data
@EqualsAndHashCode(callSuper = true)
public class Link extends AbstractEntityModel {

    @SemanticProperties({
            @SemanticProperty("internalLink"),
            @SemanticProperty("externalLink"),
            @SemanticProperty("e:internalLink"),
            @SemanticProperty("e:externalLink")
    })
    @JsonProperty("Url")
    private String url;

    @JsonProperty("LinkText")
    @SemanticProperty("e:linkText")
    private String linkText;

    @SemanticProperty("e:alternateText")
    @JsonProperty("AlternateText")
    private String alternateText;
}
