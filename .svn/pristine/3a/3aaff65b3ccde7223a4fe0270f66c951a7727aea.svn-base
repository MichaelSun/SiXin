package com.common.messagecenter.base;


/**
 * 	接受到服务器消息的回调
 * @author yang-chen
 */
public interface ConnHandler {
    /**
     * 接受一个服务器消息
     * @param msgFromServer 服务器返回的消息
     * @return 返回false表示允许其他ChatHandler接受本消息，返回true表示要霸占这个消息，消息不向下传递
     */
    boolean receive(String msgFromServer);

    /**
     * 连接丢失时调用
     */
    void connectionLost();
}
