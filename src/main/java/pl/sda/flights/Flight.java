package pl.sda.flights;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Flight {

    private final String from;
    private final String to;
    private final List<String> connections;

    public Flight(String from, String to) {
        this.from = from;
        this.to = to;
        this.connections = Collections.emptyList();
    }

    public Flight(String from, List<String> connections, String to) {
        this.from = from;
        this.to = to;
        this.connections = connections;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<String> getConnections() {
        return connections;
    }

    @Override
    public String toString() {
        String c = connections.stream()
            .collect(Collectors.joining("->"));
        String b = c.isEmpty() ? "->" : "->" + c + "->";
        return from + b + to;
    }

    public boolean isTo(String destination) {
        return this.to.equals(destination);
    }
}

