package es.uclm.Nester.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "properties")
public class Property {

    public enum PropertyType{
        PISO,
        CASA,
        HABITACION
    }

    public enum Complement{
        WIFI,
        LAVADORA,
        AIRE_ACONDICIONADO,
        //LO QUE SE QUIERA AÑADIR
    } 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType type;

    @Column(nullable = false)
    private int bedrooms;

    @Column(nullable = false)
    private int beds;

    @Column(nullable = false)
    private int bathrooms;

    @Column(nullable = false)
    private int guestCapacity;

    @ElementCollection
    @CollectionTable(
        name = "property_complements",
        joinColumns = @JoinColumn(name = "property_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "complement")
    private Set<Complement> complements = new LinkedHashSet<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    protected Property() {
    }

    public Property(
        String title,
        String city,
        String country,
        String postalCode,
        String address,
        PropertyType type,
        int bedrooms,
        int beds,
        int bathrooms,
        int guestCapacity,
        Set<Complement> complements,
        String description,
        BigDecimal pricePerNight,
        User owner) {
        this.title = title;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.address = address;
        this.type = type;
        this.bedrooms = bedrooms;
        this.beds = beds;
        this.bathrooms = bathrooms;
        this.guestCapacity = guestCapacity;
        this.complements = complements == null
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(complements);
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public PropertyType getType() {
        return type;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBeds() {
        return beds;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public int getGuestCapacity() {
        return guestCapacity;
    }

    public Set<Complement> getComplements() {
        return complements;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public User getOwner() {
        return owner;
    }
}
