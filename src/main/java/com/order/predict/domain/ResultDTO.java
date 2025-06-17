package com.order.predict.domain;

import lombok.Data;

import java.util.Set;

@Data
public class ResultDTO {
    private Set<Long> bestA;
    private Set<Long> bestR;
    private double cost;
    private double runtime;

    public ResultDTO(Set<Long> bestA, Set<Long> bestR, double cost, double runtime) {
        this.bestA = bestA;
        this.bestR = bestR;
        this.cost = cost;
        this.runtime = runtime;
    }

}


