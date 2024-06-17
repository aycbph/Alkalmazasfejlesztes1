package hu.gde.aycbph.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "sites")
@OneToOne
public class Sites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int postalCode;
    private String addressId;
    private String userId;
}
