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
                // handle error when lap times are not found
                return 0.0;
            }
        } else {
            // handle error when runner is not found
            return -1.0;
        }
    }

    @Transactional
    public void addRunner(RunnerEntity runner) {
        // Itt lehet végezni a szükséges ellenőrzéseket vagy validációkat,
        // majd hozzáadni a futót az adatbázishoz a RunnerRepository segítségével
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
        //getAverageLaptime(runnerId);
        //generateLapTimeForRunnerAndRace(runner, race);


        System.out.println("RRS addRnunner " + runner + " race: " + race + " average: " + getAverageLaptime(runnerId));


    }
    // Generate lap time for the runner and associate it with the race
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

        // Set average pace for the runner
        long averagePace = random.nextInt(100) + 1;
        runner.setAveragePace(averagePace);

        // Add lap time to the runner
        //runner.getLapTimes().add(lapTime);
        System.out.println("RaceRunnerC gener");
        // Hozzáadod a LapTimeEntity-t a versenyhez és a futóhoz
        race.addLapTime(lapTime);
        runner.addLapTime(lapTime);

        // A verseny LapTime-kat tartalmazó listájához hozzáadod a lapTime objektumot
        race.getLapTimes().add(lapTime);
        // A futó LapTime-kat tartalmazó listájához is hozzáadod a lapTime objektumot
        runner.getLapTimes().add(lapTime);

        // A futóhoz tartozó versenyek LapTime-kat tartalmazó listájához is hozzáadod a lapTime objektumot
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
            // Ha nincsenek lapidők, akkor az átlagos idő 0.0 lesz
            result = 0.0;
        }

        // Az átlagos időt most már lehet használni
        System.out.println("Average running time for runner " + runnerId + ": " + result);
    }


    @Transactional
    public void addLapTimeToRunner(Long runnerId) {
        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        List<RaceEntity> races = runner.getRaces();
        //for (RaceEntity race : races) generateLapTimeForRunnerAndRace(new LapTimeEntity(), 250, runner, race);
        for (RaceEntity race : races) generateLapTimeForRunnerAndRace(new LapTimeEntity(), 250, runner, race);{
            // Hozzáadod ugyanazt a lapTime objektumot minden versenyhez
            //race.addLapTime(lapTime);


        }

        // Mentsd el a változtatásokat az adatbázisban
        runnerRepository.save(runner);
    }
}





