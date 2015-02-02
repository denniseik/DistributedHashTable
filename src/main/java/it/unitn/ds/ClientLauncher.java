package it.unitn.ds;

import it.unitn.ds.entity.Item;
import it.unitn.ds.rmi.NodeClient;
import it.unitn.ds.rmi.NodeServer;
import it.unitn.ds.util.InputUtil;
import it.unitn.ds.util.RemoteUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.Map;

public final class ClientLauncher {

    private static final Logger logger = LogManager.getLogger();

    /**
     * ./client.jar {methodName},{host},{Node ID},{key},{value}
     * <p/>
     * Example: update,localhost,10,12,New Value Item
     * Example: get,localhost,10,12
     * Example: view,localhost,10
     */
    public static void main(String args[]) {
        logger.info("Client is ready for request>>");
        logger.info("Example: {methodName},{host},{Node ID},{key},{value}");
        logger.info("Example: update,localhost,10,12,New Value Item");
        logger.info("Example: get,localhost,10,12");
        logger.info("Example: view,localhost,10");
        InputUtil.readInput(ClientLauncher.class.getName());
    }

    /**
     * Get item given node id and item key
     *
     * @param nodeId of the known node, does not have to contain item key
     * @param key    of the item
     */
    public static void get(String host, int nodeId, int key) throws RemoteException {
        NodeClient node = RemoteUtil.getRemoteNode(host, nodeId, NodeClient.class);
        if (node == null) {
            logger.warn("Cannot get remote nodeId=" + nodeId);
            return;
        }
        Item item = node.getItem(key);
        logger.info("Got item=" + item + " from nodeId=" + nodeId);
    }

    /**
     * Update item given node id and item key
     *
     * @param nodeId of the known node, does not have to contain item key
     * @param key    of the item
     * @param value  new item value
     */
    public static void update(String host, int nodeId, int key, String value) throws RemoteException {
        NodeClient node = RemoteUtil.getRemoteNode(host, nodeId, NodeClient.class);
        if (node == null) {
            logger.warn("Cannot get remote nodeId=" + nodeId);
            return;
        }
        Item item = node.updateItem(key, value);
        logger.info("Updated item=" + item + " from nodeId=" + nodeId);
    }

    /**
     * View ring topology from the given node id
     *
     * @param targetNodeId of the known node
     */
    public static void view(String host, int targetNodeId) throws RemoteException {
        NodeServer remoteNode = RemoteUtil.getRemoteNode(host, targetNodeId, NodeServer.class);
        if (remoteNode == null) {
            logger.warn("Cannot get remote nodeId=" + targetNodeId);
            return;
        }
        for (Map.Entry<Integer, String> entry : remoteNode.getNodes().entrySet()) {
            NodeServer node = RemoteUtil.getRemoteNode(entry.getValue(), entry.getKey(), NodeServer.class);
            if (node == null) {
                logger.warn("Cannot get remote nodeId=" + entry.getKey());
            } else {
                logger.debug("Node=" + node.getNode());
            }
        }
        logger.info("Viewed topology from targetNodeId=" + targetNodeId);
    }
}
