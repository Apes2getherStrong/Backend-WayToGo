package com.example.waytogo.route.model.entity;

import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Table(name="route")
@EqualsAndHashCode
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    private UUID id;
    @ManyToOne
    private User user;
    private String name;
}
