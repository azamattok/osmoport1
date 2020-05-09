package com.space.service.specification;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShipSpecificationBuilder {

    private final List<SearchCriteria> params;

    public ShipSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public ShipSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(operation, value, key ));
        return this;
    }

    public Specification<Ship> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(ShipSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }

    public static Specification getSpecification(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                                                 Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {

        ShipSpecificationBuilder builder = new ShipSpecificationBuilder();

        if (name != null) {
            builder.with("name", ":", name);
        }
        if (planet != null) {
            builder.with("planet", ":", planet);
        }
        if (shipType != null) {
            builder.with("shipType", ":", shipType);
        }
        if (isUsed != null) {
            builder.with("isUsed", ":", isUsed);
        }
        if (minSpeed != null) {
            builder.with("speed", ">", minSpeed);
        }
        if (maxSpeed != null) {
            builder.with("speed", "<", maxSpeed);
        }
        if (minCrewSize != null) {
            builder.with("crewSize", ">", minCrewSize);
        }
        if (maxCrewSize != null) {
            builder.with("crewSize", "<", maxCrewSize);
        }
        if (minRating != null) {
            builder.with("rating", ">", minRating);
        }
        if (maxRating != null) {
            builder.with("rating", "<", maxRating);
        }
        if (after != null) {
            builder.with("prodDate", ">", new Date(after));
        }
        if (before != null) {
            builder.with("prodDate", "<", new Date(before));
        }

        Specification<Ship> shipSpecification = builder.build();
        return shipSpecification;
    }


}
