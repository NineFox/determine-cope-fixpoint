package determine.cope.fixpoint.domain.service;

import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import determine.cope.fixpoint.domain.model.MonitoringLog;
import determine.cope.fixpoint.domain.model.Server;
import determine.cope.fixpoint.domain.model.StatusServer;
import lombok.Getter;

public class FindAbnormalStatusService {

    @Getter
    private Map<InetAddress, List<MonitoringLog>> statusMap = new HashMap<>();

    /**
     * モニタリングログをIPアドレスごとに仕分ける.
     * @param log
     */
    public void addStatusMap(MonitoringLog log) {

        // Server IPがすでにマップに存在する場合は、追加
        statusMap.computeIfPresent(log.getIp(), (key, list) -> {
            list.add(log);
            return list;
        });

        // Server IPがマップに存在しない場合は、新規登録
        statusMap.putIfAbsent(log.getIp(), new ArrayList<>(Arrays.asList(log)));
    }

    /**
     * 故障時間の算出.
     * @param retry 故障判定する回数
     * @return 故障したサーバー一覧
     */
    public Map<InetAddress, Server> getDownTime(int retry) {
        Map<InetAddress, Server> downTimeMap = new HashMap<>();
        statusMap.forEach((k, v) -> {
            int noResponseCount = 0;
            for (int i = 0; i < v.size(); i++) {
                //故障判定
                if ("-".equals(v.get(i).getPingResponseTime())) {
                    noResponseCount++;
                } else {
                    noResponseCount = 0;
                }

                if (noResponseCount >= retry) {
                    // 故障とする
                    var downTime = Duration.between(v.get(i).getLogDateTime(),
                            v.get(i - noResponseCount).getLogDateTime());
                    downTimeMap.put(k, new Server(downTime, k, StatusServer.NORESPONSE));
                    break;
                }
            }
        });

        return downTimeMap;
    }

    /**
     * 過負荷の検出.
     * @param recentTime 直近のレスポンス回数
     * @param thresholdResoponseTime 過負荷と判定するレスポンス秒数
     * @return 故障したサーバー一覧
     */
    public Map<InetAddress, Server> getOverload(int recentTime, int thresholdResoponseTime) {
        Map<InetAddress, Server> overloadMap = new HashMap<>();
        statusMap.forEach((k, v) -> {

            int sumResoponseTime = 0;
            for (int i = v.size() - 1; i >= v.size() - recentTime; i--) {
                if (!"-".equals(v.get(i).getPingResponseTime())) {
                    // レスポンスタイム合計
                    sumResoponseTime += Integer.parseInt(v.get(i).getPingResponseTime());
                }
            }

            double averageResoponseTime = sumResoponseTime / recentTime;
            if (averageResoponseTime >= thresholdResoponseTime) {
                // 過負荷とする
                overloadMap.put(k, new Server(Duration.ZERO, k, StatusServer.OVERLOAD));
            }
        });

        return overloadMap;
    }

}
