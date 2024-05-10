package com.anthoxo.hackhaton.events;

import com.anthoxo.hackhaton.models.Mode;
import org.springframework.context.ApplicationEvent;

public class ContestEvent extends ApplicationEvent {

    private final Mode mode;

    public ContestEvent(Object source, Mode mode) {
        super(source);
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }
}
