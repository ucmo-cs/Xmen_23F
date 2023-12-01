package edu.ucmo.cbbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
    private Long applicationId;

    @Column(nullable = false, columnDefinition = "BLOB")
    @Lob

    private String description;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private String reason;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    /**
     * The time window start.
     * !!! DO NOT USE THIS FIELD directly  !!!
     * @see ChangeRequest#getTimeWindowStart()
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeWindowStart;


    /**
     * The time window end.
     * !!! DO NOT USE THIS FIELD directly  !!!
     *
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeWindowEnd;



    @Column(nullable = false)
    private Long timeToRevert;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private ChangeRequestApproveOrDeny approveOrDeny;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChangeRequestState state;

    @Column(nullable = false)
    @Min(3)
    private String Implementer = "Not Assigned";

    @ManyToOne
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChangeRequestRiskLevel riskLevel;

    @Column(nullable = false, columnDefinition = "BLOB")
    @Lob
    private String backoutPlan;

}
