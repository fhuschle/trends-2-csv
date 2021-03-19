package de.huf.trends2csv.csv;

import de.huf.trends2csv.resources.enums.Country;
import de.huf.trends2csv.resources.enums.CountryRegion;
import de.huf.trends2csv.service.trend.TrendResult;
import de.huf.trends2csv.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

@Component
@Slf4j
public class CsvExportComponent {

    private static final String TIME_COLUMN_LABEL = "Time";

    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class KeywordDataKey implements Comparable<KeywordDataKey> {

        private final CountryRegion countryRegion;
        private final String keyword;

        @Override
        public String toString() {
            return String.format("%s_%s", keyword, countryRegion);
        }

        @Override
        public int compareTo(final KeywordDataKey o) {
            final int result = o.toString().compareTo(toString());
            if (o.getCountryRegion() instanceof Country) {
                if (countryRegion instanceof Country) {
                    return result;
                } else {
                    return 1;
                }
            }
            return result;
        }
    }

    public void export(final List<TrendResult> trendResults,
                       final Path path) throws IOException {

        final Map<KeywordDataKey, TrendResult> keywordDateValueMap = new TreeMap<>();
        trendResults.forEach(trendResult -> keywordDateValueMap.put(new KeywordDataKey(trendResult.getCountryRegion(), trendResult.getKeyword()), trendResult));
        try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
            final List<String> headers = new LinkedList<>();
            for (final KeywordDataKey keywordDataKey : keywordDateValueMap.keySet()) {
                headers.add(String.format("%s %s", keywordDataKey.getKeyword(), keywordDataKey.getCountryRegion().getName()));
            }
            final TrendResult firstSet = keywordDateValueMap.entrySet().iterator().next().getValue();
            final String resolution = firstSet.getResolution();
            final List<Pair<String, String>> resolutionRows = new LinkedList<>();
            firstSet.getData().forEach(entry -> resolutionRows.add(new Pair<>(entry.getTime(), entry.getFormattedTime())));

            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(Stream.concat(Stream.of(TIME_COLUMN_LABEL, resolution), headers.stream()).toArray(String[]::new)));
            for (int i = 0; i < resolutionRows.size(); ++i) {
                final List<String> row = new ArrayList<>();
                final Pair<String, String> resolutionEntry = resolutionRows.get(i);
                row.add(resolutionEntry.getValue1());
                row.add(resolutionEntry.getValue2());
                for (final Map.Entry<KeywordDataKey, TrendResult> keywordDataKeyListEntry : keywordDateValueMap.entrySet()) {
                    row.add(String.valueOf(keywordDataKeyListEntry.getValue().getData().get(i).getValue()[0]));
                }
                csvPrinter.printRecord(row);
            }
            log.info("writing {} lines to {}", resolutionRows.size(), path.getFileName());
            csvPrinter.flush();

        }
    }
}
