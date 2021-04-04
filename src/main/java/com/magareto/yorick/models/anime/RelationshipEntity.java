package com.magareto.yorick.models.anime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipEntity {
    private List<RelationShipDataEntity> data;

    public List<RelationShipDataEntity> getData() {
        return data;
    }

    public void setData(List<RelationShipDataEntity> data) {
        this.data = data;
    }
}
