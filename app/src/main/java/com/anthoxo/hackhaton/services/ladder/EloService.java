package com.anthoxo.hackhaton.services.ladder;

import com.anthoxo.hackhaton.entities.Ladder;
import com.anthoxo.hackhaton.entities.SoloRun;
import com.anthoxo.hackhaton.models.Grid;
import com.anthoxo.hackhaton.models.StartingTile;
import com.anthoxo.hackhaton.repositories.LadderRepository;
import com.anthoxo.hackhaton.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EloService {

    private final LadderRepository ladderRepository;

    public EloService(LadderRepository ladderRepository) {
        this.ladderRepository = ladderRepository;
    }

    public void computeElo(List<SoloRun> soloRuns) {
        for (int i = 0; i < soloRuns.size(); i++) {
            SoloRun soloRun1 = soloRuns.get(i);
            Ladder ladder1 = ladderRepository.findByUser(soloRun1.getUser())
                    .orElseGet(() -> new Ladder(soloRun1.getUser()));
            boolean playerOneHasFinished = hasFinished(soloRun1);
            for (int j = i + 1; j < soloRuns.size(); j++) {
                SoloRun soloRun2 = soloRuns.get(j);
                Ladder ladder2 = ladderRepository.findByUser(soloRun2.getUser())
                        .orElseGet(() -> new Ladder(soloRun2.getUser()));
                boolean playerTwoHasFinished = hasFinished(soloRun1);

                double elo1 = computeElo(
                        soloRun1.getMoves().size(),
                        ladder1.getElo(),
                        playerOneHasFinished,
                        soloRun2.getMoves().size(),
                        ladder2.getElo(),
                        playerTwoHasFinished
                );
                double elo2 = computeElo(
                        soloRun2.getMoves().size(),
                        ladder2.getElo(),
                        playerTwoHasFinished,
                        soloRun1.getMoves().size(),
                        ladder1.getElo(),
                        playerOneHasFinished
                );
                ladder1.addChange(elo1);
                ladder2.addChange(elo2);
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);
            }
        }
    }

    private double computeElo(
            int moves1,
            double myElo,
            boolean oneHasFinished,
            int moves2,
            double opponentElo,
            boolean twoHasFinished
    ) {
        double difference = (opponentElo - myElo) / 400.;
        double tenRaisedPower = Math.pow(10, difference) + 1;
        double expected = 1 / tenRaisedPower;
        double score = 0;
        if (!oneHasFinished) {
            score = 0;
        } else if (moves1 < moves2 || !twoHasFinished) {
            score = 1;
        } else if (moves1 == moves2) {
            score = 0.5;
        }
        double K = 20;
        return K * (score - expected);
    }

    private boolean hasFinished(SoloRun soloRun) {
        Grid grid = new Grid(ListUtils.copy(soloRun.getGrid().getGrid()));
        for (int i = 0; i < soloRun.getMoves().size(); i++) {
            grid.color(StartingTile.TOP_LEFT, grid.getCurrentColor(StartingTile.TOP_LEFT), Integer.valueOf(soloRun.getMoves().get(i)));
        }
        return grid.getNumberOfColors() == 1;
    }
}
