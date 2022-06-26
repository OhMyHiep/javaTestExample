package com.example.springtestdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="employee")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="firstname")
    private String fname;

    @Column
    private String lastname;

    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    public Set<Project> projects;

}

