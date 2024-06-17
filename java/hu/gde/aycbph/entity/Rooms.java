package hu.gde.aycbph.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToOne
    private Long id;
    private int floor;
    private int room;
}
