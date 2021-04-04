package com.magareto.yorick.models.anime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Inclusion {
    private String id;
    private String type;
    private InclusionAttributes attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InclusionAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(InclusionAttributes attributes) {
        this.attributes = attributes;
    }
}
