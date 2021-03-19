package de.huf.trends2csv.csv;

import de.huf.trends2csv.resources.enums.Country;
import de.huf.trends2csv.resources.enums.Region;
import de.huf.trends2csv.resources.multiline.TimelineData;
import de.huf.trends2csv.service.trend.TrendResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CsvExportComponentTest {

    private CsvExportComponent csvExportComponent;
    private Path exportPath;

    @BeforeEach
    void setUp() throws IOException {
        csvExportComponent = new CsvExportComponent();
        exportPath = new File(Files.createTempDirectory("csvExportComponentTest").toFile(), "export.csv").toPath();
        Files.deleteIfExists(exportPath);
    }

    @Test
    void export() throws IOException {
        final TrendResult deNI = new TrendResult("test", Region.BREMEN);
        deNI.setResolution("Week");
        deNI.setData(List.of(TimelineData.builder().time("1615575600").formattedTime("12.03.2021 um 20:00").value(new int[]{30}).build()));
        final TrendResult de = new TrendResult("unit", Country.DE);
        de.setResolution("Week");
        de.setData(List.of(TimelineData.builder().time("1615575600").formattedTime("12.03.2021 um 20:00").value(new int[]{20}).build()));

        csvExportComponent.export(List.of(deNI, de), exportPath);

        final String expectedResult = """
                Time,Week,unit Deutschland,test Bremen
                1615575600,12.03.2021 um 20:00,20,30
                """;
        assertThat(Files.readString(exportPath).replace("\r", ""), is(expectedResult));
    }
}
