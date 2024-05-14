package hu.gde.aycbph;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
//ez a jó
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

    int lapTimeValue = ThreadLocalRandom.current().nextInt(180, 800); // Generate random lap time

    public LapTimeEntity(RunnerEntity runner, RaceEntity race, int lapNumber, int timeSecond, int lapTimeValue) {
        this.runner = runner;
        this.race = race;
        this.lapNumber = lapNumber;
        this.timeSecond = timeSecond;
        this.lapTimeValue = lapTimeValue;

    }


    public LapTimeEntity() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RunnerEntity getRunner() {
        return runner;
    }

    public void setRace(RaceEntity race) {
        this.race = race;
    }

    public void setRunner(RunnerEntity runner) {
        this.runner = runner;
    }


    public RaceEntity getRace() {
        return race;
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


    public Long getId(RunnerEntity race) {return getId(race);}



    public static class LapTimeRequest {
        private int lapTimeSeconds;

        public int getLapTimeSeconds() {
            return lapTimeSeconds;
        }

        }
    }



