#项目简介
http-server是一个基于netty实现的http post请求的业务处理框架，post内部body内容采用自定义的二进制协议。  
http-server的定位是一套http处理框架，而不是传统的http web处理框架，所以它不用实现http-servlet那套接口，
因此也就不用嵌入tomcat，jetty之类的web容器。  
也正因为http-server是http处理框架，所以它能干的事，SpringMVC，SSH等web框架也能干，但是它更加轻量级，也
更加容易去控制请求到响应的每一步。  
基于netty的实现决定了http-server底层用的是NIO的非阻塞式的io模型，NIO模型在高并发的情况下支撑性更佳，
相比tomcat默认的BIO模型。  
http-server整体采用Spring+netty实现，cache层采用的是ehcache(方便日后集群)，dao层采用的是myibatis，数据源
采用的是druid。
