package com.zk;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author huangxinhe
 */
public class HighLock extends AbstractLock{
 
    private static final String PARENT_NODE_PATH="/high_lock";
 
    //当前节点路径
    private String currentNodePath;
 
    //前一个节点的路径
    private String preNodePath;
 
    private CountDownLatch countDownLatch;
 
    @Override
    public boolean tryLock() {
 
        //判断父节点是否存在
        if (!zkClient.exists(PARENT_NODE_PATH)){
            //不存在
            zkClient.createPersistent(PARENT_NODE_PATH);
        }
 
        //创建第一个临时有序子节点
        if (currentNodePath == null || "".equals(currentNodePath)){
 
            //根节点下没有节点信息，将当前节点作为第一个子节点,类型：临时有序
            currentNodePath = zkClient.createEphemeralSequential(PARENT_NODE_PATH+"/","lock");
        }
 
        //不是第一个子节点，获取父节点下所有子节点
        List<String> childrenNodeList = zkClient.getChildren(PARENT_NODE_PATH);
 
        //子节点升序排序
        Collections.sort(childrenNodeList);
 
        //判断是否加锁成功
        if (currentNodePath.equals(PARENT_NODE_PATH+"/"+childrenNodeList.get(0))){
            //当前节点是序号最小的节点
            return true;
        }else {
            //当前节点不是序号最小的节点，获取其前面的节点名称，并赋值
            int length = PARENT_NODE_PATH.length();
            int currentNodeNumber = Collections.binarySearch(childrenNodeList, 
currentNodePath.substring(length + 1));
            preNodePath = PARENT_NODE_PATH+"/"+childrenNodeList.get(currentNodeNumber-1);
        }
        return false;
    }
 
    @Override
    public void waitLock() {
 
        IZkDataListener zkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
 
            }
 
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
 
                if (countDownLatch != null){
                    countDownLatch.countDown();
                }
            }
        };
 
        //监听前一个节点的改变
        zkClient.subscribeDataChanges(preNodePath,zkDataListener);
 
        if (zkClient.exists(preNodePath)){
            countDownLatch = new CountDownLatch(1);
 
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
 
            }
        }
 
        zkClient.unsubscribeDataChanges(preNodePath,zkDataListener);
    }
 
    @Override
    public void releaseLock() {
        zkClient.delete(currentNodePath);
        zkClient.close();
    }
}