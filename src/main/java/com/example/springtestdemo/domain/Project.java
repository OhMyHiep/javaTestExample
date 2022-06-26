package com.example.springtestdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Employee leader;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "employee_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    public Set<Employee> employees;

}

