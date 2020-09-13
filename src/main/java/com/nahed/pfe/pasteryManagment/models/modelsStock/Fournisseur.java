package com.nahed.pfe.pasteryManagment.models.modelsStock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Temporal(TemporalType.DATE)
//@JsonFormat(shape = JsonFormat.Shape.STRING, locale = "es_PE", timezone = "America/Lima")
//    @DateTimeFormat(pattern = "dd-MM-yyyy", iso = DateTimeFormat.ISO.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")

//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name="datecreation")
    private LocalDate dateCreation;
    private String name;
    private String email;
    private String phone;
    private String address;
    private BigDecimal compte;
    private Integer tax;
    @Column(name="contact_name")
    private String contactName;

    private String note;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = BonReception.class,mappedBy = "fournisseur")
    @JsonIgnore
    private List bonreceptions;

}
