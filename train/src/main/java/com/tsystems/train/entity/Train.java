package com.tsystems.train.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "number")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Train {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    private String id;

    @Column
    private String number;

    @Column
    private int places;

    @Column(nullable = false)
    @Builder.Default
    private EntityStatus status = EntityStatus.ACTIVE;

    @ManyToOne
    private Route route;

    @OneToMany(mappedBy = "train", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<StationStatus> stationStatuses;
}
