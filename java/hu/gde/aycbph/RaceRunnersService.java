package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class RaceRunnersService {

    private final RunnerRepository runnerRepository;
    private final RunnerService runnerService;
    private final RaceRepository raceRepository;

    @Autowired
    public RaceRunnersService(RunnerRepository runnerRepository, RaceRepository raceRepository, RunnerService runnerService) {
        this.runnerRepository = runnerRepository;
        this.raceRepository = raceRepository;
        this.runnerService = runnerService;
    }



    @Transactional
    public double getAverageLaptime(Long runnerId) {
        RunnerEntity runner;
        runner = runnerRepository.findById(runnerId).orElse(null);
        System.out.println("RRS getAve");
        if (runner != null) {
            List<LapTimeEntity> laptimes;
            laptimes = runner.getLapTimes();
            int totalTime = 0;
            for (LapTimeEntity laptime : laptimes) {
                totalTime += laptime.getTimeSecond();
                System.out.println("RRS getAve");
            }
            if (!laptimes.isEmpty()) {
                return (double) totalTime / laptimes.size();
            } else {

                return 0.0;
            }
        } else {

            return -1.0;
        }
    }

    @Transactional
    public void addRunner(RunnerEntity runner) {

        System.out.println("RRS addR");
        runnerRepository.save(runner);
    }

    @Transactional
    public void addRunner(Long raceId, Long runnerId) throws ChangeSetPersister.NotFoundException {
        RaceEntity race = raceRepository.findById(raceId)
                .orElseThrow(() -> new RuntimeException("Race not found"));

        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        getAverageLaptime(runnerId);
        getAverageLaptime(raceId);
        addLapTimeToRunner(runner.getRunnerId());

        race.getRunners().add(runner);
        raceRepository.save(race);

        runner.getRaces().add(race);
        runnerRepository.save(runner);



        System.out.println("RRS addRnunner " + runner + " race: " + race + " average: " + getAverageLaptime(runnerId));


    }

    @Transactional
    public void generateLapTimeForRunnerAndRace(LapTimeEntity lapTimeEntity, int i, RunnerEntity runner, RaceEntity race) {
        Random random = new Random();
        int lapNumber = random.nextInt(10) + 1;
        int timeSeconds = random.nextInt(600) + 1;

        LapTimeEntity lapTime = new LapTimeEntity();
        lapTime.setLapNumber(lapNumber);
        lapTime.setTimeSecond(timeSeconds);
        lapTime.setRunner(runner);
        lapTime.setRace(race);

        long averagePace = random.nextInt(100) + 1;
        runner.setAveragePace(averagePace);


        race.addLapTime(lapTime);
        runner.addLapTime(lapTime);


        race.getLapTimes().add(lapTime);

        runner.getLapTimes().add(lapTime);


        runner.getRaces().forEach(r -> r.getLapTimes().add(lapTime));
        try {
            calculateAverageRunningTime(runner.getRunnerId());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Transactional
    public void calculateAverageRunningTime(Long runnerId) throws ChangeSetPersister.NotFoundException {
        double result = 0.0;
        RunnerEntity runner;
        runner = runnerRepository.findById(runnerId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<LapTimeEntity> lapTimes = runner.getLapTimes();

        if (!lapTimes.isEmpty()) {
            double totalTime = lapTimes.stream().mapToDouble(LapTimeEntity::getTimeSecond).sum();
            result = totalTime / lapTimes.size();
        } else {

            result = 0.0;
        }
        System.out.println("Average running time for runner " + runnerId + ": " + result);
    }


    @Transactional
    public void addLapTimeToRunner(Long runnerId) {
        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        List<RaceEntity> races = runner.getRaces();

        for (RaceEntity race : races) generateLapTimeForRunnerAndRace(new LapTimeEntity(), 250, runner, race);{

        }
        runnerRepository.save(runner);
    }
}





