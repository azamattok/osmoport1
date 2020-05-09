package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class ShipController {

    @Autowired
    private ShipService shipService;

    //Get list of Ships by filter
    @GetMapping("/ships")
    public List<Ship> getAllShips(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "planet", required = false) String planet,
                                  @RequestParam(value = "shipType", required = false) ShipType shipType,
                                  @RequestParam(value = "after", required = false) Long after,
                                  @RequestParam(value = "before", required = false) Long before,
                                  @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                  @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                  @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                  @RequestParam(value = "minRating", required = false) Double minRating,
                                  @RequestParam(value = "maxRating", required = false) Double maxRating,
                                  @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {
        return shipService.getShipsList(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);
    }

    //Get ship count
    @GetMapping("/ships/count")
    public Long getCountOfShips(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "planet", required = false) String planet,
                                @RequestParam(value = "shipType", required = false) ShipType shipType,
                                @RequestParam(value = "after", required = false) Long after,
                                @RequestParam(value = "before", required = false) Long before,
                                @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                @RequestParam(value = "minRating", required = false) Double minRating,
                                @RequestParam(value = "maxRating", required = false) Double maxRating) {
        return shipService.getShipsCount(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    //Create ship
    @PostMapping("/ships")
    public Ship createShip(@RequestBody Ship ship) {
        return shipService.createShip(ship);
    }

    //Get ship by ID
    @GetMapping("/ships/{id}")
    public Ship getShip(@PathVariable String id) {
        Long lonId = parseId(id);
        return shipService.getShip(lonId);
    }

    //Update ship
    @PostMapping("/ships/{id}")
    public Ship updateOfShip(@PathVariable(value = "id") String id, @RequestBody Ship ship) {
        Long lonId = parseId(id);
        return shipService.updateship(lonId, ship);
    }

    //Delete ship
    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE)
    public void deleteShip(@PathVariable String id) {
        Long lonId = parseId(id);
        shipService.deleteShip(lonId);
    }

    private Long parseId (String id) {
        Long longId = 0L;
        if (checkString(id) && !id.contains(",") && !id.contains(".")) {
            try {
                longId = Long.parseLong(id);
                if (checkId(longId)) return longId;
            }
            catch (NumberFormatException e) { }
        }
        throw  new BadRequestException("ID doesn't correct.");
    }
    private boolean checkId (Long id) {
        return id > 0;
    }

    //Return true if all good
    private boolean checkString (String s) {
        return s != null && !s.isEmpty();
    }
}
