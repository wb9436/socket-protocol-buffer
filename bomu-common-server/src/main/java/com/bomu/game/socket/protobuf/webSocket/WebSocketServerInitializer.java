/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.bomu.game.socket.protobuf.webSocket;

import com.bomu.game.socket.config.WSConfig;
import com.bomu.game.socket.protobuf.listener.WebSocketCloseListener;
import com.bomu.game.socket.protobuf.listener.WebSocketIdleListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import protocol.MetaDataBase;

/**
 */
@Component
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketCloseListener webSocketCloseListener;

    @Autowired
    private WebSocketIdleListener webSocketIdleListener;

    public WebSocketServerInitializer() {
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
//      //netty的ByteBuf转成PB消息的解码器实例（第一个参数是要解码的消息原型，第二个参数是扩展消息的ExtensionRegistry实例）
        ProtobufDecoder protobufDecoder = new ProtobufDecoder(
                MetaDataBase.MetaData.getDefaultInstance()
        );
        // 解码（将字节流转化成程序中使用的数据类型）
        ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(2 * 1024 * 1024, 0, 4, 0, 4));
        ch.pipeline().addLast("protobufDecoder", protobufDecoder);

        // 编码（将程序中的数据类型转换成字节流）
        ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
        //ProtobufEncoder将pb消息替代netty自己的ByteBuf
        ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());

        ch.pipeline().addLast(new IdleStateHandler(WSConfig.getReadIdleTimeSeconds(), WSConfig.getWriteIdleTimeSeconds(), WSConfig.getIdleTimeSeconds()));
        ch.pipeline().addLast(new HttpServerCodec());

        // 逻辑处理
        WebSocketServerHandler bizHandler = new WebSocketServerHandler();
        bizHandler.setCloseListener(webSocketCloseListener);
        bizHandler.setIdleListener(webSocketIdleListener);
        ch.pipeline().addLast(bizHandler);
    }

}
