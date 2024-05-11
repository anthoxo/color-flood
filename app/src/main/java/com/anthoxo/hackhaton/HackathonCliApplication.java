package com.anthoxo.hackhaton;

import com.anthoxo.hackhaton.dtos.GridResultDto;
import com.anthoxo.hackhaton.models.Mode;
import com.anthoxo.hackhaton.services.game.BattleRunService;
import com.anthoxo.hackhaton.services.game.SoloRunService;
import com.anthoxo.hackhaton.services.game.VersusRunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;

//@SpringBootApplication
public class HackathonCliApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
        .getLogger(HackathonCliApplication.class);

    private final SoloRunService soloRunService;
    private final VersusRunService versusRunService;
    private final BattleRunService battleRunService;
    private final ApplicationContext applicationContext;

    public HackathonCliApplication(
        SoloRunService soloRunService,
        VersusRunService versusRunService,
        BattleRunService battleRunService,
        ApplicationContext applicationContext
    ) {
        this.soloRunService = soloRunService;
        this.versusRunService = versusRunService;
        this.battleRunService = battleRunService;
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
//        SpringApplication.run(HackathonCliApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        String pathFile = "/Users/anthony.araye/Documents/Test.java";
        File file = new File(pathFile);
        Mode mode = Mode.BATTLE;

        GridResultDto gridResultDto = switch (mode) {
            case SOLO -> soloRunService.run(file, true);
            case VERSUS -> versusRunService.runWithRandom(file, true);
            case BATTLE -> battleRunService.runAlone(file, true);
        };

        LOG.info("");
        LOG.info("result={}", gridResultDto.statistics());
        LOG.info("colors={}, moves={}", gridResultDto.history().getLast().getNumberOfColors(), gridResultDto.history().size());
        LOG.info("");
        SpringApplication.exit(applicationContext);
    }
}
