package southwest.monsoon.module.common.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class CustomSnowflakeIdGenerator implements IdentifierGenerator {
    private final Sequence sequence;

    public CustomSnowflakeIdGenerator() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        Sequence sequence;
        try {
            String hostName = localHost.getHostName();
            String[] split = hostName.split("-");
            String partHost = split[split.length - 1];
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

            String workerIdSource = partHost + pid;
            long workerId = rehash(workerIdSource, 32);

            byte[] address = localHost.getAddress();
            byte lastAddress = address[address.length - 1];
            long dataCenterId = rehash(lastAddress, 32);
            log.info("""
                    CustomSnowflakeIdGenerator                                                
                    Localhost: {}
                    pid: {}
                    WorkerIdSource: {}
                    DataCenterSource: {}
                    workerId: {}
                    dataCenterId: {}
                    """, localHost, pid, workerIdSource, lastAddress, workerId, dataCenterId);
            sequence = new Sequence(workerId, dataCenterId);
        } catch (Exception e) {
            log.warn("Use default IdentifierGenerator");
            log.warn("Fail to init customized ID generator", e);
            sequence = new Sequence(localHost);
        }
        this.sequence = sequence;
    }

    @Override
    public Number nextId(Object entity) {
        return sequence.nextId();
    }

    public static long rehash(Object obj, int max) {
        return Math.abs(obj.hashCode()) % max;
    }
}
