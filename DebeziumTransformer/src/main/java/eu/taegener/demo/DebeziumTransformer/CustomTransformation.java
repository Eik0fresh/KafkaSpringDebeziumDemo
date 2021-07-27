package eu.taegener.demo.DebeziumTransformer;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.header.Headers;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.zookeeper.ZooKeeper;

public class CustomTransformation<R extends ConnectRecord<R>> implements Transformation<R> {

    private static ZooKeeper zk;
    private static NullZooKeeperWatcher zkWatcher = new NullZooKeeperWatcher();
    private static final Logger log = Logger.getLogger( CustomTransformation.class.getName() );

    public R apply(R sourceRecord) {
        if (sourceRecord.value() == null) {
            return sourceRecord;
        }
        log.info("Apply CostumTransformation");

        Struct struct = (Struct) sourceRecord.value();
        String databaseOperation = struct.getString("op");

        if (databaseOperation.equalsIgnoreCase("d")) {
            return null;
        } else if (databaseOperation.equalsIgnoreCase("c") || databaseOperation.equalsIgnoreCase("r")) {
            Long timestamp = struct.getInt64("ts_ms");
            Struct after = struct.getStruct("after");

            String eventId = after.getString("event_id");
            Integer version = after.getInt32("version");
            String rootAggregateType = after.getString("root_aggregate_type");
            String rootAggregateId = after.getString("root_aggregate_id");
            String aggregateType = after.getString("aggregate_type");
            String aggregateId = after.getString("aggregate_id");
            String eventType = after.getString("event_type");
            String payload = after.getString("payload");

            log.info("EventType: " + rootAggregateType);

            String key = String.format("%s#%s", aggregateId, eventType);

            Headers headers = sourceRecord.headers();
            headers.addString("eventId", eventId);

            Schema valueSchema = SchemaBuilder.struct().field("eventType", after.schema().field("event_type").schema())
                    .field("version", after.schema().field("version").schema())
                    .field("timestamp", struct.schema().field("ts_ms").schema())
                    .field("aggregateType", after.schema().field("aggregate_type").schema())
                    .field("eventData", after.schema().field("payload").schema()).build();
            headers.addString("eventType", eventType);

            Struct value = new Struct(valueSchema).put("eventType", eventType).put("version", version)
                    .put("timestamp", timestamp).put("aggregateType", aggregateType).put("eventData", payload);

            try {
                initZooKeeper();
                Integer numberOfPartition = zk
                        .getChildren(String.format("/brokers/topics/%s/partitions", rootAggregateType), zkWatcher).size();
                Integer partition = Utils.toPositive(Utils.murmur2(rootAggregateId.getBytes())) % numberOfPartition;
                // Build the event to be published.

                sourceRecord = sourceRecord.newRecord(rootAggregateType, partition, Schema.STRING_SCHEMA, key, valueSchema,
                        value, sourceRecord.timestamp(), headers);
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println(sourceRecord);
            return sourceRecord;
        } else {
            throw new IllegalArgumentException("Record of unexpected op type: " + sourceRecord);
        }
    }

    public static void initZooKeeper() throws IOException {
        if (zk == null) {
            synchronized (CustomTransformation.class) {
                CustomTransformation.zk = new ZooKeeper(System.getenv("ZOOKEEPER_CONNECTION"), 1000, zkWatcher);
            }
        }
    }

    public static void closeZooKeeper() throws InterruptedException {
        CustomTransformation.zk.close();
        CustomTransformation.zk = null;
    }

    public ConfigDef config() {
        return new ConfigDef();
    }

    public void close() {
        try {
            CustomTransformation.closeZooKeeper();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void configure(Map<String, ?> configs) {

    }
}
