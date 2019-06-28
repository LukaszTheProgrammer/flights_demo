package pl.sda.flights;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/flights")
@AllArgsConstructor
class FlightsController {

    private final static List<Flight> flights = Arrays.asList(new Flight("Katowice", "Londyn"),
        new Flight("Londyn", "Monachium"),
        new Flight("Madryt", "Mediolan"), new Flight("Katowice", "Rzym"),
        new Flight("Rzym", "Barcelona"),
        new Flight("Barcelona", "Madryt"), new Flight("Frankfurt", "Krakow"),
        new Flight("Rodos", "Paryż"),
        new Flight("Saloniki", "Barcelona"), new Flight("Dublin", "Frankfurt"),
        new Flight("Frankfurt", "Paryż"),
        new Flight("Warszawa", "Moskwa"), new Flight("Moskwa", "Turyn"),
        new Flight("Frankfurt", "Rzym"),
        new Flight("Moskwa", "Londyn"), new Flight("Warszawa", "Rzym"),
        new Flight("Madryt", "Warszwa"),
        new Flight("Amsterdam", "Ateny"), new Flight("Monachium", "Paryż"), new Flight("Londyn", "Paryż"),
        new Flight("Ateny", "Berno"), new Flight("Praga", "Moskwa"), new Flight("Madryt", "Warszawa"),
        new Flight("Dublin", "Praga"), new Flight("Katowice", "Amsterdam"), new Flight("Mediolan", "Ateny"),
        new Flight("Frankfurt", "Amsterdam"), new Flight("Barcelona", "Dublin"), new Flight("Turyn", "Rodos"),
        new Flight("Dublin", "Paryż"));

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<Flight> findFlights(@RequestParam String from, @RequestParam String to) {
        return findFlight(flights, from, to);
    }

    private static List<Flight> findFlight(List<Flight> flights,
                                           String departure, String destination) {

        Map<Boolean, List<Flight>> flighsByMatchingDeparture = flights.stream()
            .collect(Collectors.partitioningBy(f -> f.getFrom().equals(departure)));

        List<Flight> flightsFromDeparture = flighsByMatchingDeparture.get(true);
        List<Flight> restOfFlights = flighsByMatchingDeparture.get(false);

        List<Flight> matching = flightsFromDeparture.stream()
            .filter(f -> f.isTo(destination))
            .collect(Collectors.toList());

        List<Flight> connectingFlights = flightsFromDeparture.stream()
            .filter(f -> !f.isTo(destination))
            .flatMap(f -> findFlight(restOfFlights, f.getTo(), destination).stream()
                .map(pp -> makeConnection(f, pp)))
            .collect(Collectors.toList());

        return Stream.concat(matching.stream(), connectingFlights.stream())
            .collect(Collectors.toList());
    }

    private static Flight makeConnection(Flight f1, Flight f2) {
        List<String> connections = new ArrayList<>(f2.getConnections());
        connections.add(0, f1.getTo());
        return new Flight(f1.getFrom(), connections, f2.getTo());
    }
}
