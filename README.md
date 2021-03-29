# WebSocket JS 客户端访问地址
```
http://localhost:18110/client/index

```

# .proto文件生成js文件
```
1.去github下载protobuf最新版的protoc文件

2.编写一个messages.proto文件

3.将protoc拷贝到messages.proto所在目录下面，执行如下命令

protoc --js_out=import_style=commonjs,binary:./ messages.proto

编译完成后在同目录下会生成messages_pb.js文件

```