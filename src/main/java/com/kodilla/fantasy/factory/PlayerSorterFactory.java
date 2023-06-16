package com.kodilla.fantasy.factory;

import com.kodilla.fantasy.domain.SortType;
import org.springframework.data.domain.Sort;

public class PlayerSorterFactory {

    public static Sort makeSorter(SortType sortType) {
        return switch (sortType) {
            case ID_ASCENDING -> Sort.by("id").ascending();
            case ID_DESCENDING -> Sort.by("id").descending();
            case LASTNAME_ASCENDING -> Sort.by("lastname").ascending();
            case LASTNAME_DESCENDING -> Sort.by("lastname").descending();
            case VALUE_ASCENDING -> Sort.by("value").ascending();
            case VALUE_DESCENDING -> Sort.by("value").descending();
            case TEAM_ASCENDING -> Sort.by("team").ascending();
            case TEAM_DESCENDING -> Sort.by("team").descending();
        };
    }
}
