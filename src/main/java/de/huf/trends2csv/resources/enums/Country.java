package de.huf.trends2csv.resources.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Country implements CountryRegion {
    DE("DE", "Deutschland");

    private final String code;
    private final String name;
}
