package de.huf.trends2csv;

import de.huf.trends2csv.csv.CsvExportComponent;
import de.huf.trends2csv.resources.enums.Country;
import de.huf.trends2csv.resources.enums.CountryRegion;
import de.huf.trends2csv.resources.enums.Region;
import de.huf.trends2csv.service.trend.TrendResult;
import de.huf.trends2csv.service.trend.TrendsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Trends2CsvApplication implements ApplicationRunner {

    private static final String KEYWORD_SEPERATOR = ",";
    private final TrendsService trendsService;
    private final CsvExportComponent csvExportComponent;
    private final Environment environment;

    @Getter
    @RequiredArgsConstructor
    public enum ProgrammArgs {
        KEYWORDS("keywords="),
        START_DATE("start="),
        END_DATE("end="),
        FILENAME("filename=");

        private final String argName;
    }

    public static void main(final String[] args) {
        SpringApplication.run(Trends2CsvApplication.class, args);
    }

    @Override
    public void run(final ApplicationArguments args) throws IOException {
        if (Arrays.stream(environment.getActiveProfiles()).noneMatch(profile -> profile.equals("integrationtest"))) {
            final long startTime = System.currentTimeMillis();

            String[] keywords = new String[]{"example"};
            String filename = "trends_export.csv";
            LocalDate start = LocalDate.of(2019, Month.SEPTEMBER, 1);
            LocalDate end = LocalDate.now();
            for (final String arg : args.getNonOptionArgs()) {
                if (arg.contains(ProgrammArgs.KEYWORDS.getArgName())) {
                    keywords = arg.replace(ProgrammArgs.KEYWORDS.getArgName(), "").split(KEYWORD_SEPERATOR);
                } else if (arg.contains(ProgrammArgs.START_DATE.getArgName())) {
                    start = LocalDate.parse(arg.replace(ProgrammArgs.START_DATE.getArgName(), ""));
                } else if (arg.contains(ProgrammArgs.END_DATE.getArgName())) {
                    end = LocalDate.parse(arg.replace(ProgrammArgs.END_DATE.getArgName(), ""));
                } else if (arg.contains(ProgrammArgs.FILENAME.getArgName())) {
                    filename = arg.replace(ProgrammArgs.FILENAME.getArgName(), "");
                }
            }

            log.info("**********************************");
            log.info("#           Trends2CSV           #");
            log.info("**********************************");
            log.info("Start CSV export with following params:");
            log.info("keywords={}", Arrays.toString(keywords));
            log.info("filename={}", filename);
            log.info("start={}", start);
            log.info("end={}", end);
            log.info("----------------------------------");

            final List<TrendResult> results = new ArrayList<>();
            for (final CountryRegion countryRegion : Country.values()) {
                for (final String keyword : keywords) {
                    log.info("pulling data for {}_{}", keyword, countryRegion);
                    results.add(trendsService.getTrendsData(keyword, countryRegion, start, end));
                }
            }
            for (final CountryRegion countryRegion : Region.values()) {
                for (final String keyword : keywords) {
                    log.info("pulling data for {}_{}", keyword, countryRegion);
                    results.add(trendsService.getTrendsData(keyword, countryRegion, start, end));
                }
            }
            csvExportComponent.export(results, Paths.get(filename));
            log.info("finish after {}ms", System.currentTimeMillis() - startTime);
            System.exit(0);
        }
    }

}
