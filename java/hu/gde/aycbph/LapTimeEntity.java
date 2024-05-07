package hu.gde.aycbph;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// LapTimeEntity osztály
// LapTimeEntity osztály
@Entity
@Table(name = "laptimes")
public class LapTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "runner_id")
    private RunnerEntity runner;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private RaceEntity race;

    @Column(name = "lap_number")
    private int lapNumber;

    @Column(name = "time_seconds")
    private int timeSecond;

    Random random = new Random();
    int lapTimeValue = ThreadLocalRandom.current().nextInt(80, 201); // Generate random lap time

    public LapTimeEntity(RunnerEntity runner, RaceEntity race, int lapNumber, int timeSecond) {
        this.runner = runner;
        this.race = race;
        this.lapNumber = lapNumber;
        this.timeSecond = timeSecond;
        //this.lapTimeValue = lapTimeValue;
    }

    public LapTimeEntity(RunnerEntity runner, int lapTimeValue) {
        this.runner = runner;
        this.lapTimeValue = lapTimeValue;

    }


    // Getterek és setterek


    public LapTimeEntity() {}

    public LapTimeEntity(RunnerEntity runner, RaceEntity race, int i, int lapTimeValue, long averagePace, int lapTimeValue1) {
        this.runner = runner;
        this.race = race;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RunnerEntity getRunner() {
        return runner;
    }

    public void setRunner(RunnerEntity runner) {
        this.runner = getRunner();
    }


    public RaceEntity getRace() {
        return race;
    }

    public void setRace(RaceEntity race) {
        this.race = getRace();
    }

    public int getLapNumber() {return lapNumber;}


    public void setLapNumber(int lapNumber) {
        this.lapNumber = lapNumber;
    }

    public void setTimeSecond(int timeSecond) {
        this.timeSecond = timeSecond;
    }

    public int getTimeSecond() {
        return timeSecond;
    }

    //public void setTimeSecond(int timeSecond) {this.timeSecond = timeSecond;}

    //public Long getRaceId() {return race != null ? race.getRaceId() : null;}

    public Long getId(RunnerEntity race) {return getId(race);}

    public void setRaceId(Long raceId) {
        this.setRace(race);
    }
    public static class LapTimeRequest {
        private int lapTimeSeconds;

        public int getLapTimeSeconds() {
            return lapTimeSeconds;
        }

        //public void setLapTimeSeconds(int lapTimeSeconds) {
            //this.lapTimeSeconds = lapTimeSeconds;
        }
    }



