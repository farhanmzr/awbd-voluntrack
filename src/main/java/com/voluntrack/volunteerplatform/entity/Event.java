package com.voluntrack.volunteerplatform.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.voluntrack.volunteerplatform.enums.EventStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Event title is required.")
    @Size(max = 150, message = "Event title must not exceed 150 characters.")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Event description is required.")
    @Size(max = 2000, message = "Event description must not exceed 2000 characters.")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "start_date_time", nullable = false)
    @NotNull(message = "Start date and time is required.")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end_date_time", nullable = false)
    @NotNull(message = "End date and time is required.")
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    @NotNull(message = "Capacity is required.")
    @Min(value = 1, message = "Capacity must be at least 1.")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Event status is required.")
    private EventStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}