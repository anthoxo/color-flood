package com.anthoxo.hackhaton.listeners;

import com.anthoxo.hackhaton.events.ContestEvent;
import com.anthoxo.hackhaton.services.contest.ContestService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ContestListener implements ApplicationListener<ContestEvent> {

    private final ContestService contestService;

    public ContestListener(ContestService contestService) {
        this.contestService = contestService;
    }

    @Override
    public void onApplicationEvent(ContestEvent event) {
        switch (event.getMode()) {
            case SOLO -> contestService.runSoloContest();
            case VERSUS -> contestService.runVersusContest();
            case BATTLE -> contestService.runBattleContest();
        }
    }
}
