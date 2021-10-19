package determine.cope.fixpoint.domain.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import determine.cope.fixpoint.domain.model.MonitoringLog;

public class CsvUtils {

    private CsvUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final List<MonitoringLog> getMonitoringLogs(Path filePath) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(MonitoringLog.class);

        var monitoringLogs = new ArrayList<MonitoringLog>();

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            MappingIterator<MonitoringLog> it = mapper.readerFor(MonitoringLog.class).with(schema).readValues(br);
            while (it.hasNextValue()) {
                monitoringLogs.add(it.nextValue());
            }
        }

        return monitoringLogs;
    }

}
