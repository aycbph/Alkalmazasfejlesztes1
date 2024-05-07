package hu.gde.aycbph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private final RaceRepository raceRepository;
    private final RaceRunnersService raceRunnersService;
    private final RunnerRepository runnerRepository;
    private final LapTimeRepository lapTimeRepository;

    @Autowired
    public DataLoader(RaceRepository raceRepository, RunnerRepository runnerRepository, LapTimeRepository lapTimeRepository, RaceRunnersService raceRunnersService) {
        this.raceRepository = raceRepository;
        this.runnerRepository = runnerRepository;
        this.lapTimeRepository = lapTimeRepository;
        this.raceRunnersService = raceRunnersService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Létrehozunk két versenyt
        RaceEntity race1 = new RaceEntity();
        race1.setRaceName("Kápolnai");
        race1.setRaceDistance(20.0);
        race1.setRaceDate("2024-04-23");
        race1.setRaceOrganizer("Kisfalvi Béla");
        raceRepository.save(race1);
        System.out.println("Race1" + race1);

        RaceEntity race2 = new RaceEntity();
        race2.setRaceName("Maraton");
        race2.setRaceDistance(26.0);
        race2.setRaceDate("2024-04-28");
        race2.setRaceOrganizer("Kelemen Andrea");
        raceRepository.save(race2);
        System.out.println("Race2" + race2);

        // Létrehozunk három futót
        RunnerEntity runner1 = new RunnerEntity();
        runner1.setRunnerName("Mészáros Zsanett");
        runner1.setAveragePace(290);
        runner1.setRunnerAge(24);
        runner1.setRunnerGender("female");
        runnerRepository.save(runner1);
        System.out.println("R1" + runner1);

        RunnerEntity runner2 = new RunnerEntity();
        runner2.setRunnerName("Hidvégi Zsuzsi");
        runner2.setAveragePace(240);
        runner2.setRunnerAge(24);
        runner2.setRunnerGender("female");
        runnerRepository.save(runner2);
        System.out.println("R2" + runner2);

        RunnerEntity runner3 = new RunnerEntity();
        runner3.setRunnerName("Herczeg Zoltán");
        runner3.setAveragePace(190);
        runner3.setRunnerAge(22);
        runner3.setRunnerGender("male");
        runnerRepository.save(runner3);
        System.out.println("R3" + runner3);


        // Ellenőrizze, hogy a runnerek adatbázisba kerültek-e, és írja ki a konzolra
        if (runnerRepository.count() > 0) {
            System.out.println("A runnerek adatbázisba kerültek.");
        } else {
            System.out.println("Nem sikerült a runnerek adatbázisba mentése.");
        }

        raceRunnersService.generateLapTimeForRunnerAndRace(new LapTimeEntity(), 250, runner2, race2);

        race2.getRunners().add(runner2);
        raceRepository.save(race2);
        raceRunnersService.getAverageLaptime(runner2.getRunnerId());

        runner2.getRaces().add(race2);
        runnerRepository.save(runner2);
        raceRunnersService.getAverageLaptime(runner1.getRunnerId());
        raceRunnersService.getAverageLaptime(race1.getRaceId());
        raceRunnersService.addLapTimeToRunner(runner1.getRunnerId());


        for (RaceEntity race : raceRepository.findAll()) {
            //for (RaceEntity race : raceRepository.findAll()) {}
            // Kiírjuk a race1-hez rendelt runnerek nevét és generált lapidejét
            raceRunnersService.generateLapTimeForRunnerAndRace(new LapTimeEntity(), 250, runner1, race1);

            race1.getRunners().add(runner1);
            raceRepository.save(race1);
            raceRunnersService.getAverageLaptime(runner1.getRunnerId());
            raceRunnersService.getAverageLaptime(runner2.getRunnerId());
            raceRunnersService.getAverageLaptime(race2.getRaceId());
            raceRunnersService.addLapTimeToRunner(runner2.getRunnerId());

            runner1.getRaces().add(race1);
            runnerRepository.save(runner1);
            //race.addLapTime(lapTimeRepository.getReferenceById(runner1.getRunnerId()));

            //raceRunnersService.generateLapTimeForRunnerAndRace(new LapTimeEntity(), 250, runner1, race2);

            System.out.println("Runner11 " + race1 + " hozzárendelve a versenyhez " + race1 + ":");
            for (LapTimeEntity laptime : race1.getLapTimes()) {
                System.out.println("Laptime: " + laptime);
                System.out.println("Runner11 " + race1.getLapTimes() + " hozzárendelve a versenyhez " + race1.getRunners() + ":" + runner1.getRaces());
            }


// Kiírjuk a race2-höz rendelt runnerek nevét és generált lapidejét
            for (RunnerEntity runner : runnerRepository.findAll()) {
                //raceRunnersService.generateLapTimeForRace(runner, race2);
                System.out.println("Runner22 " + runner1 + " hozzárendelve a versenyhez " + runner1.getRaces() + ":");
                for (LapTimeEntity laptime : runner1.getLapTimes()) {
                    System.out.println("Laptime: " + laptime);
                }
            }

            // Ellenőrizheted, hogy a futókat sikeresen hozzárendelték-e a versenyhez
            if (raceRepository.count() > 0) {
                System.out.println("A futó hozzárendelésre került a versenyhez.");
            } else {
                System.out.println("Nem sikerült a futó hozzárendelése a versenyhez.");
            }

            // Ellenőrizzük a mentett adatokat
            System.out.println("A futók és a laptimes-ek sikeresen hozzá lettek adva a versenyekhez.");
            //lapTime.getRunner().getRaces().add(race1); // Verseny hozzáadása
        }
    }
}