package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.service.exception.BadRequestException;
import com.space.service.exception.ShipNotFoundException;
import com.space.service.specification.ShipSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
public class ShipServiceImpl implements ShipService {
    private static final int THIS_YEAR = 3019;
    @Autowired
    private ShipRepository shipRepository;


    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }



    @Override
    public List<Ship> getShipsList(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                   Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order, Integer pageNumber, Integer pageSize) {
        Specification specification = ShipSpecificationBuilder.getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return shipRepository.findAll(specification, PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getContent();
    }

    @Override
    public Long getShipsCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        Specification specification = ShipSpecificationBuilder.getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return shipRepository.count(specification);
    }

    @Override
    public Ship createShip(Ship ship) {
        validate(ship);
        if (ship.getUsed() == null) ship.setUsed(false);
        enableRating(ship);
        return shipRepository.save(ship);
    }

    private void validate(Ship ship) {
        if (!validateString(ship.getName()) || ship.getName().length() > 50)
            throw new BadRequestException("Incorrect Ship.name");

        if (!validateString(ship.getPlanet()) || ship.getPlanet().length() > 50)
            throw new BadRequestException("Incorrect Ship.planet");

        if (ship.getCrewSize() == null || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)
            throw new BadRequestException("Incorrect Ship.crewSize");

        if (ship.getSpeed() == null || ship.getSpeed() < 0.01D || ship.getSpeed() > 0.99D)
            throw new BadRequestException("Incorrect Ship.speed");

        if (ship.getProdDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ship.getProdDate());
            if (cal.get(Calendar.YEAR) < 2800 || cal.get(Calendar.YEAR) > THIS_YEAR)
                throw new BadRequestException("Incorrect Ship.date");
        }
    }


    private  void enableRating (Ship ship) {

        Calendar calendar =Calendar.getInstance();
        Date date = ship.getProdDate();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);

        //Получаем коэфф.
        double k;
        if(ship.getUsed()) {
            k = 0.5;
        }
        else {
            k =1;
        }

        BigDecimal rating = new BigDecimal((80 * ship.getSpeed() * k) / (THIS_YEAR - year + 1));

        rating = rating.setScale(2, RoundingMode.HALF_UP);

        ship.setRating(rating.doubleValue());
    }
    @Override
    public Ship getShip(Long id) {
        return shipRepository.findById(id).orElseThrow(() -> new ShipNotFoundException(id.toString()));

    }

    @Override
    public Ship updateship(Long id, Ship ship) {
        Ship currentShip = getShip(id);
        if (ship.getName() != null) currentShip.setName(ship.getName());
        if (ship.getPlanet() != null) currentShip.setPlanet(ship.getPlanet());
        if (ship.getShipType() != null) currentShip.setShipType(ship.getShipType());
        if (ship.getProdDate() != null) currentShip.setProdDate(ship.getProdDate());
        if (ship.getUsed() != null) currentShip.setUsed(ship.getUsed());
        if (ship.getSpeed() != null) currentShip.setSpeed(ship.getSpeed());
        if (ship.getCrewSize() != null) currentShip.setCrewSize(ship.getCrewSize());
        validate(currentShip);
        enableRating(currentShip);
        return shipRepository.save(currentShip);
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.delete(getShip(id));

    }
    private boolean validateString (String s) {
        return s != null && !s.isEmpty();
    }
}
