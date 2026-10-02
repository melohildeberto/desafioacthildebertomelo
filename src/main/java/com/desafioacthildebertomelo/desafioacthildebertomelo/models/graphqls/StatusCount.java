package com.desafioacthildebertomelo.desafioacthildebertomelo.models.graphqls;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class StatusCount {
    private String status;
    private Long quantidade;

    public StatusCount(String status, Long quantidade) {
        this.status = status;
        this.quantidade = quantidade;
    }
}
