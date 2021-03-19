package de.huf.trends2csv.util;

import lombok.Data;

@Data
public class Pair<T, D> {

    private final T value1;
    private final D value2;
}
