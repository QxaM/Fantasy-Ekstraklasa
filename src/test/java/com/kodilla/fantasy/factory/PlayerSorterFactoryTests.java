package com.kodilla.fantasy.factory;

import com.kodilla.fantasy.domain.SortType;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerSorterFactoryTests {

    @Test
    void testPlayerSorter() {
        //Given
        SortType idAscending = SortType.ID_ASCENDING;
        SortType idDescending = SortType.ID_DESCENDING;
        SortType lastnameAscending = SortType.LASTNAME_ASCENDING;
        SortType lastnameDescending = SortType.LASTNAME_DESCENDING;
        SortType valueAscending = SortType.VALUE_ASCENDING;
        SortType valueDescending = SortType.VALUE_DESCENDING;
        SortType teamAscending = SortType.TEAM_ASCENDING;
        SortType teamDescending = SortType.TEAM_DESCENDING;

        //When
        Sort sort_idAscending = PlayerSorterFactory.makeSorter(idAscending);
        Sort sort_idDescending = PlayerSorterFactory.makeSorter(idDescending);
        Sort sort_lastnameAscending = PlayerSorterFactory.makeSorter(lastnameAscending);
        Sort sort_lastnameDescending = PlayerSorterFactory.makeSorter(lastnameDescending);
        Sort sort_valueAscending = PlayerSorterFactory.makeSorter(valueAscending);
        Sort sort_valueDescending = PlayerSorterFactory.makeSorter(valueDescending);
        Sort sort_teamAscending = PlayerSorterFactory.makeSorter(teamAscending);
        Sort sort_teamDescending = PlayerSorterFactory.makeSorter(teamDescending);

        //Then
        assertAll(() -> assertEquals(Sort.by("id").ascending(), sort_idAscending),
                () -> assertEquals(Sort.by("id").descending(), sort_idDescending),
                () -> assertEquals(Sort.by("lastname").ascending(), sort_lastnameAscending),
                () -> assertEquals(Sort.by("lastname").descending(), sort_lastnameDescending),
                () -> assertEquals(Sort.by("value").ascending(), sort_valueAscending),
                () -> assertEquals(Sort.by("value").descending(), sort_valueDescending),
                () -> assertEquals(Sort.by("team").ascending(), sort_teamAscending),
                () -> assertEquals(Sort.by("team").descending(), sort_teamDescending));
    }
}
