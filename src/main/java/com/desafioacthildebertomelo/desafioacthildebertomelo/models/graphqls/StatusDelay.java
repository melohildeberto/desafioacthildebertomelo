package com.desafioacthildebertomelo.desafioacthildebertomelo.models.graphqls;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class StatusDelay {
    private String status;
    private Double mediaAtraso;

    public StatusDelay(String status, Double mediaAtraso) {
        this.status = status;
        this.mediaAtraso = mediaAtraso;
    }
}
