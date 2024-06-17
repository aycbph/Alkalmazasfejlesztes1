package hu.gde.aycbph.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "districts")

public class Districts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String district;
    private String siteId;
    private String addressId;


    public Districts() {

    }
}
