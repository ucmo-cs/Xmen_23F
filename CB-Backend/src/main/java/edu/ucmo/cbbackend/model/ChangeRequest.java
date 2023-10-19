package edu.ucmo.cbbackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "change_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder

public class ChangeRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "author", nullable = false, foreignKey = @ForeignKey(name = "FK_users"))
    private User author;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeType changeType;

    @Column(nullable = false)
    private Integer applicationId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String reason;


}
