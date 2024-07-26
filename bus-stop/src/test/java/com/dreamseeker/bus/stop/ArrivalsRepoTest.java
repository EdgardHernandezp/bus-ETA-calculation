package com.dreamseeker.bus.stop;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class ArrivalsRepoTest {
    private ArrivalsRepo repo = new ArrivalsRepo();

    @Test
    void whenGetReturnRightListSizeAndOrder() {
        repo.add("4", "415", 5);
        repo.add("5","501", 1);
        repo.add("2", "302",10);
        repo.add("1", "220",2);

        List<ArrivalsRepo.Record> result = repo.get(3);
        assertThat(result).hasSize(3);
        assertThat(result.get(0).busNumber()).isEqualTo("5");
        assertThat(result.get(1).busNumber()).isEqualTo("1");
        assertThat(result.get(2).busNumber()).isEqualTo("4");
    }

    @Test
    void whenAddingARepeatedRecordOverwritingExistingOne() {
        repo.add("1", "415", 4);
        repo.add("1","415", 3);

        List<ArrivalsRepo.Record> records = repo.get(2);
        assertThat(records).hasSize(1);
    }

    @Test
    void whenComparingTwoRecordsWithSameBusNumberEqualsIsTrue() {
        ArrivalsRepo.Record record1 = new ArrivalsRepo.Record("1", "415", 4);
        ArrivalsRepo.Record record2 = new ArrivalsRepo.Record("1", "400", 5);

        assertThat(record1).isEqualTo(record2);
    }

    @Test
    void whenComparingTwoRecordsWithSimilarValuesExceptForBusNumberEqualsIsFalse() {
        ArrivalsRepo.Record record1 = new ArrivalsRepo.Record("1", "415", 4);
        ArrivalsRepo.Record record2 = new ArrivalsRepo.Record("2", "415", 4);

        assertThat(record1).isNotEqualTo(record2);
    }
}