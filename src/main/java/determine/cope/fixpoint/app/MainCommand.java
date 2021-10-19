package determine.cope.fixpoint.app;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import determine.cope.fixpoint.domain.service.FindAbnormalStatusService;
import determine.cope.fixpoint.domain.util.CsvUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "find-abnormal")
public class MainCommand implements Callable<Integer> {

    @Option(names = "--n")
    private int retryTime;

    @Option(names = "--m")
    private int recentTime;

    @Option(names = "--t")
    private int thresholdResoponseTime;

    @Parameters(description = "log file path")
    private Path logFilePath;

    @Override
    public Integer call() throws IOException {
        var monitoringLogs = CsvUtils.getMonitoringLogs(logFilePath);
        var findAbnormalstatusService = new FindAbnormalStatusService();
        monitoringLogs.forEach(log -> findAbnormalstatusService.addStatusMap(log));
        return 0;
    }

}
