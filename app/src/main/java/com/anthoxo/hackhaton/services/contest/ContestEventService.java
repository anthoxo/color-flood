package com.anthoxo.hackhaton.services.contest;

import com.anthoxo.hackhaton.events.ContestEvent;
import com.anthoxo.hackhaton.models.Mode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ContestEventService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ContestEventService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishSoloEvent() {
        applicationEventPublisher.publishEvent(new ContestEvent(this, Mode.SOLO));
    }

    public void publishVersusEvent() {
        applicationEventPublisher.publishEvent(new ContestEvent(this, Mode.VERSUS));
    }

    public void publishBattleEvent() {
        applicationEventPublisher.publishEvent(new ContestEvent(this, Mode.BATTLE));
    }
}
