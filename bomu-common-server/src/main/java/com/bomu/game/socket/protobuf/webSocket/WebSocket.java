package com.bomu.game.socket.protobuf.webSocket;

import com.bomu.socket.protocol.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.MetaDataBase;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * webSocket对象
 *
 * @author guiyuan
 */
public class WebSocket {
    private static final Logger logger = LogManager.getLogger(WebSocket.class);

    private String id;
    private ChannelHandlerContext ctx;
    private Channel channel;
    private InetSocketAddress localAddr;
    private InetSocketAddress remoteAddr;
    private long createTime;
    private Integer userId;
    private String areaId;
    private String tableId;
    private String ip;

    private Map<String, Object> atrrs = new ConcurrentHashMap<>();

    public WebSocket(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.channel = ctx.channel();
        this.id = channel.id().asShortText();
        this.localAddr = (InetSocketAddress) channel.localAddress();
        this.remoteAddr = (InetSocketAddress) channel.remoteAddress();
        this.createTime = System.currentTimeMillis();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public InetSocketAddress getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(InetSocketAddress localAddr) {
        this.localAddr = localAddr;
    }

    public InetSocketAddress getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(InetSocketAddress remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Map<String, Object> getAtrrs() {
        return atrrs;
    }

    public void setAtrrs(Map<String, Object> atrrs) {
        this.atrrs = atrrs;
    }

    public void addAttr(String key, Object value) {
        this.atrrs.putIfAbsent(key, value);
    }

    public Object getAttr(String key) {
        return this.atrrs.get(key);
    }

    public boolean hasAttr(String key) {
        return this.atrrs.containsKey(key);
    }

    public boolean isOpen() {
        return this.channel.isOpen();
    }

    public boolean isActive() {
        return this.channel.isActive();
    }

    public void flush() {
        this.channel.flush();
    }

    private ChannelFuture send(MetaDataBase.MetaData response) {
        logger.info("server 拿到处理后的返回结果 :{}", response.getData());

        ChannelFuture future = this.ctx.writeAndFlush(response);
        if (logger.isDebugEnabled()) {
            logger.debug("send msg,userId=" + userId + " status is:" + future.isSuccess() + "; socket=" + this.toString());
        }
        return future;
    }

    public ChannelFuture send(Protocol protocol) {
        return send(protocol.getMetaData());
    }

    public ChannelFuture close() {
        ChannelFuture future = null;
        if (this.channel.isActive()) {
            future = this.channel.close().addListener(ChannelFutureListener.CLOSE);
            if (logger.isDebugEnabled()) {
                logger.debug("close webSocket, userId=" + userId + " sessionId:" + this.id);
            }
        }
        return future;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSocket webSocket = (WebSocket) o;
        return Objects.equals(id, webSocket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "WebSocket{" +
                "id='" + id + '\'' +
                ", channel=" + channel +
                ", localAddr=" + localAddr +
                ", remoteAddr=" + remoteAddr +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", areaId='" + areaId + '\'' +
                ", tableId='" + tableId + '\'' +
                ", ip='" + ip + '\'' +
                ", atrrs=" + atrrs +
                '}';
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     * 玩家离开桌子清除桌子信息
     */
    public void cleanTable() {
        this.tableId = "";
    }
}
