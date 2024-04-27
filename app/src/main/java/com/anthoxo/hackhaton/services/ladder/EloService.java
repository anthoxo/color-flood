package com.anthoxo.hackhaton.services.ladder;

import com.anthoxo.hackhaton.entities.Ladder;
import com.anthoxo.hackhaton.entities.SoloRun;
import com.anthoxo.hackhaton.repositories.LadderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EloService {

    private final LadderRepository ladderRepository;

    public EloService(LadderRepository ladderRepository) {
        this.ladderRepository = ladderRepository;
    }

    public void computeElo(List<SoloRun> soloRuns) {
        List<SoloRun> sortedSoloRuns = soloRuns
                .stream()
                .toList();
        for (int i = 0; i < soloRuns.size(); i++) {
            SoloRun soloRun1 = sortedSoloRuns.get(i);
            Ladder ladder1 = ladderRepository.findByUser(soloRun1.getUser())
                    .orElseGet(() -> new Ladder(soloRun1.getUser()));
            for (int j = i + 1; j < soloRuns.size(); j++) {
                SoloRun soloRun2 = sortedSoloRuns.get(j);
                Ladder ladder2 = ladderRepository.findByUser(soloRun2.getUser())
                        .orElseGet(() -> new Ladder(soloRun2.getUser()));
                double elo1 = computeElo(
                        soloRun1.getMoves().size(),
                        ladder1.getElo(),
                        soloRun2.getMoves().size(),
                        ladder2.getElo()
                );
                double elo2 = computeElo(
                        soloRun2.getMoves().size(),
                        ladder2.getElo(),
                        soloRun1.getMoves().size(),
                        ladder1.getElo()
                );
                ladder1.addChange(elo1);
                ladder2.addChange(elo2);
                ladderRepository.save(ladder1);
                ladderRepository.save(ladder2);

            }
        }
    }

    private double computeElo(int rank1, double myElo, int rank2,
            double opponentElo) {
        double difference = (opponentElo - myElo) / 400.;
        double tenRaisedPower = Math.pow(10, difference) + 1;
        double expected = 1 / tenRaisedPower;
        double score = 0;
        if (rank1 < rank2) {
            score = 1;
        }
        if (rank1 == rank2) {
            score = 0.5;
        }
        double K = 20;
        return K * (score - expected);
    }
}
