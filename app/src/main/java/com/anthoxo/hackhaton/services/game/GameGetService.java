package com.anthoxo.hackhaton.services.game;

import com.anthoxo.hackhaton.dtos.GameOverviewDto;
import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.entities.*;
import com.anthoxo.hackhaton.repositories.BattleRunRepository;
import com.anthoxo.hackhaton.repositories.SoloRunRepository;
import com.anthoxo.hackhaton.repositories.VersusRunRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameGetService {

    private final SoloRunRepository soloRunRepository;
    private final VersusRunRepository versusRunRepository;
    private final BattleRunRepository battleRunRepository;
    private final GameResolverService gameResolverService;

    public GameGetService(SoloRunRepository soloRunRepository,
        VersusRunRepository versusRunRepository,
        BattleRunRepository battleRunRepository,
        GameResolverService gameResolverService) {
        this.soloRunRepository = soloRunRepository;
        this.versusRunRepository = versusRunRepository;
        this.battleRunRepository = battleRunRepository;
        this.gameResolverService = gameResolverService;
    }

    public GameOverviewDto getAll() {
        List<SoloRun> soloRuns = soloRunRepository.findAll();
        List<VersusRun> versusRuns = versusRunRepository.findAll();
        List<BattleRun> battleRuns = battleRunRepository.findAll();
        return new GameOverviewDto(
            soloRuns
                .stream()
                .map(this::mapRunToGameDto)
                .toList(),
            versusRuns
                .stream()
                .map(this::mapRunToGameDto)
                .toList(),
            battleRuns
                .stream()
                .map(this::mapRunToGameDto)
                .toList()
        );
    }

    public GridResultDto getSoloDetail(Long gameId) {
        return soloRunRepository.findById(gameId)
            .map(gameResolverService::resolve)
            .orElseThrow();
    }

    public GridResultDto getVersusDetail(Long gameId) {
        return versusRunRepository.findById(gameId)
            .map(gameResolverService::resolve)
            .orElseThrow();
    }

    public GridResultDto getBattleDetail(Long gameId) {
        return battleRunRepository.findById(gameId)
            .map(gameResolverService::resolve)
            .orElseThrow();
    }

    private GameOverviewDto.GameDto mapRunToGameDto(Run run) {
        return new GameOverviewDto.GameDto(run.getId(), run.getGrid().getId(), run.getUsers()
            .stream()
            .map(User::getTeamName)
            .toList());
    }
}
