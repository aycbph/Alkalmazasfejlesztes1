package hu.gde.aycbph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RunnerService {

    private final RaceRepository raceRepository;
    private final RunnerRepository runnerRepository;
    private final LapTimeRepository lapTimeRepository;
    public final RaceResultRepository raceResultRepository;

    @Autowired
    public RunnerService(RaceRepository raceRepository, RunnerRepository runnerRepository,
                         LapTimeRepository lapTimeRepository,
                         RaceResultRepository raceResultRepository)
    {
        this.raceRepository = raceRepository;
        this.runnerRepository = runnerRepository;
        this.lapTimeRepository = lapTimeRepository;
        this.raceResultRepository = raceResultRepository;
    }



    public void recordRaceResult(Long raceId, Long runnerId, int position) throws ChangeSetPersister.NotFoundException {
        RaceEntity race = raceRepository.findById(raceId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        RunnerEntity runner;
        runner = runnerRepository.findById(raceId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        if (!race.getRunners().contains(runner)) {
            throw new RuntimeException("Runner not registered for this race");
        }

        RaceResultEntity result = new RaceResultEntity(race, runner, position);
        raceResultRepository.save(result);
        System.out.println("RS record");
    }

    @Transactional
    public double calculateAverageRunningTime(Long runnerId) throws ChangeSetPersister.NotFoundException {
        RunnerEntity runner;
        runner = runnerRepository.findById(runnerId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<LapTimeEntity> lapTimes = runner.getLapTimes();
        System.out.println("RS calc");

        if (lapTimes.isEmpty()) {
            return 0.0;
        }

        double totalTime = lapTimes.stream().mapToDouble(LapTimeEntity::getTimeSecond).sum();
        return totalTime / lapTimes.size();
    }

    public void addLapTimeToRunner(Long runnerId) {
        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("Runner not found"));

        List<RaceEntity> races = runner.getRaces();
        for (RaceEntity race : races) generateLapTimeForRace(new LapTimeEntity(), 250, runner, race);
        System.out.println("RS addLap");
    }

    @Transactional
    public void generateLapTimeForRace(LapTimeEntity lapTime, int averagePace, RunnerEntity runner, RaceEntity race) {
        int minLapTimeValue = 80;
        int maxLapTimeValue = 200;
        int lapTimeValue = ThreadLocalRandom.current().nextInt(minLapTimeValue, maxLapTimeValue + 1);

        lapTime.setRace(race);
        lapTime.setRunner(runner);
        lapTime.setLapNumber(ThreadLocalRandom.current().nextInt(1, 10));
        lapTime.setTimeSecond(lapTimeValue);

        // Assuming lapTimes is a list in RunnerEntity
        runner.addLapTime(lapTime, averagePace, runner, race);
        runnerRepository.save(runner);
        System.out.println("RS gene");

        if (runnerRepository.findById(runner.getRunnerId()).isPresent()) {
            System.out.println("Runner saved successfully.");
        } else {
            System.out.println("Error occurred while saving runner.");
        }

        System.out.println("Runner lapTimeValue: " + lapTimeValue
                + " lapTime: " + lapTime + " lapTime.getTimeSecond: " + lapTime.getTimeSecond());
    }



    // Method to add lap time to a runner along with average pace
    public void addLapTime(LapTimeEntity lapTime, int averagePace, RunnerEntity runner, RaceEntity race) {
        // Assuming lapTimes is a list in RunnerEntity
        runner.getLapTimes().add(lapTime);
        lapTime.setRunner(runner);
        lapTime.setRace(race);
        lapTime.setTimeSecond(averagePace);
        System.out.println("RS addLapTime");
    }


    public void displayRunnersForAllRaces() {
        List<RaceEntity> races = raceRepository.findAll();
        for (RaceEntity race : races) {
            System.out.println("Race: " + race.getRaceName());
            //List<RunnerEntity> runners = race.getRunners();
            List<RunnerEntity> allRunners = runnerRepository.findAll();
            System.out.println("RS disp");
            //for (RunnerEntity runner : allRunners) {
            for (RunnerEntity runner : allRunners) {
                System.out.println("\tRunner getLapTimes: "
                        + runner.getLapTimes());
            }
        }
    }
    public double getAverageLaptime(Long runnerId) {
        RunnerEntity runner = runnerRepository.findById(runnerId).orElse(null);
        System.out.println("RS GetAvere");
        if (runner != null) {
            List<LapTimeEntity> laptimes = runner.getLapTimes();
            int totalTime = 0;
            for (LapTimeEntity laptime : laptimes) {
                totalTime += laptime.getTimeSecond();
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

    public double calculateAverageLapTime() {
        int totalTime = 0;
        int totalLaps = 0;
        for (LapTimeEntity lapTime : lapTimeRepository.findAll()) {
            totalTime += lapTime.getTimeSecond();
            totalLaps++;
            System.out.println("RS calsulator");
        }
        if (totalLaps > 0) {
            return (double) totalTime / totalLaps;
        } else {
            return 0.0;
        }
    }
    // Egy futóhoz ad egy laptidőt és beállítja az átlagtempót
    public void addLapTimeToRunner(LapTimeEntity lapTime, int averagePace, RunnerEntity runner, RaceEntity race) {
        runner.addLapTime(lapTime, averagePace, runner, race);
        System.out.println("RS addLapTimeToRun");
    }
    public void addRunner(RunnerEntity runner) {
        // Itt lehet végezni a szükséges ellenőrzéseket vagy validációkat,
        // majd hozzáadni a futót az adatbázishoz a RunnerRepository segítségével
        runnerRepository.save(runner);
    }

    public void generateLapTimeForRunnerAndRace(RunnerEntity runner, RaceEntity race) {
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
        runner.getLapTimes().add(lapTime);
        System.out.println("RunnerS gener");
    }
}