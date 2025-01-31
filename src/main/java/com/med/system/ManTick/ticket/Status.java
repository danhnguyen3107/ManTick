

package com.med.system.ManTick.ticket;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    OPEN, IN_PROGRESS, CLOSED;


    @JsonCreator // Allows Jackson to deserialize lowercase or mixed case values
    public static Status fromString(String value) {
        return Status.valueOf(value.toUpperCase());
    }

    
}
