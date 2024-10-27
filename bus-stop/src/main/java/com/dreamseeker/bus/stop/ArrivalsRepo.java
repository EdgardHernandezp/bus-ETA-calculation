package com.dreamseeker.bus.stop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class ArrivalsRepo {
    private final List<Record> records = new ArrayList<>();

    public void add(String busNumber, String routeNumber, int eta) {
        Record record = new Record(busNumber, routeNumber, eta);
        int i = records.indexOf(record);
        if (i >= 0)
            records.set(i, record);
        else
            records.add(record);
        records.sort(Comparator.comparing(r -> r.eta));
    }

    public List<Record> get(int size) {
        return records.subList(0, Math.min(size, records.size()));
    }

    public record Record(String busNumber, String routeNumber, int eta) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Record record = (Record) o;
            return Objects.equals(busNumber, record.busNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(busNumber);
        }
    }
}
