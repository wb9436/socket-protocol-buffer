package com.bomu.game;

import com.bomu.game.socket.protobuf.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync //开启异步
@EnableScheduling
public class GameServerApplication implements CommandLineRunner {

    @Autowired
    WebSocketServer webSocketServer;

    @Override
    public void run(String... args) throws Exception {
        webSocketServer.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(GameServerApplication.class);
    }
}
