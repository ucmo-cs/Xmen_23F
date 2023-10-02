package edu.ucmo.cbbackend.model;

import jakarta.persistence.*;

@Table(name = "change_requests")
public class ChangeRequest {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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



    ChangeRequest(){

    }

    public ChangeRequest(Long id, User author, String title, String description, String status) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.status = status;
    }

}
