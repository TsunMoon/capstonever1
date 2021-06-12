package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ProcessStep")
@Table(name = "process_step")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcessStep implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ordinal")
    private Integer ordinal;

    @ManyToOne
    @JoinColumn(name = "process_id")
    @JsonIgnore
    private Process process;

    @ManyToOne
    @JoinColumn(name = "service_component_id")
    private ServiceComponent serviceComponent;

    @JsonIgnore
    @OneToMany(mappedBy = "processStep")
    private List<BookingProcessStep> listBookingProcessSteps;

}
