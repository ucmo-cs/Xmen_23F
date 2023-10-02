package edu.ucmo.cbbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Table(name = "change_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class ChangeRequest implements Serializable {


    @Id
    private Long id;


    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 80)
    private String status;

    @Column(nullable = false, length = 80)
    private String description;


}
