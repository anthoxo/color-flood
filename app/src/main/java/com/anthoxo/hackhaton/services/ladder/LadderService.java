package com.anthoxo.hackhaton.services.ladder;

import com.anthoxo.hackhaton.dtos.UserDto;
import com.anthoxo.hackhaton.repositories.LadderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LadderService {

    private final LadderRepository ladderRepository;

    public LadderService(LadderRepository ladderRepository) {
        this.ladderRepository = ladderRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getLadder() {
        return ladderRepository.findAll()
                .stream()
                .map(ladder -> new UserDto(ladder.getUserId(), ladder.getUser().getTeamName(), ladder.getSoloElo()))
                .toList();
    }
}
