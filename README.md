# WebSocket JS 客户端访问地址
```
http://localhost:18110/client/index

```

# .proto文件生成js文件
```
1.去github下载protobuf最新版的protoc文件

2.编写一个messages.proto文件

3.将protoc拷贝到MetaDataBase.proto所在目录下面，执行如下命令

protoc.exe --js_out=import_style=commonjs,binary:./ 将protoc拷贝到MetaDataBase.proto

编译完成后在同目录下会生成将protoc拷贝到MetaDataBase_pb.js文件

```

# 使用.proto文件生成的js文件打包成web使用的文件
```
    1. 安装库文件的引用库: npm install -g require
    
    2. 安装打包成前端使用的js文件: npm install -g browserify
    
    3. 安装protobuf的库文件: npm install google-protobuf  
    
    4. 创建打包js文件 export.js
      
        var metaData = require('./MetaDataBase_pb');
        module.exports = {
            MetaData : metaData
        };
        
    5. 编译生成可用js文件: browserify exports.js -o  MetaDataBase_pb_web.js

```