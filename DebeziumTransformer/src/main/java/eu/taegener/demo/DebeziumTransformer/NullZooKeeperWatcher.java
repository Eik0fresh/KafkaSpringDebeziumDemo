package eu.taegener.demo.DebeziumTransformer;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class NullZooKeeperWatcher implements Watcher {

    private static final Logger log = Logger.getLogger( NullZooKeeperWatcher.class.getName() );

    @Override
    public void process(WatchedEvent event) {
        KeeperState state = event.getState();
        log.info("Event process: " + state.toString());
        switch (state) {
            case Expired:
                try {
                    CustomTransformation.closeZooKeeper();
                    CustomTransformation.initZooKeeper();
                } catch (InterruptedException e) {
                    System.out.println(e);
                } catch (IOException e) {
                    System.out.println(e);
                }
                break;
            default:
                break;
        }
    }
}
