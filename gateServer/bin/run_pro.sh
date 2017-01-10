#!/bin/bash
echo "--------run gateServer start------->>"
cd /app/zpy/
if [ x$1 != x ]
then
   nohup /opt/java/jdk1.8.0_51/bin/java -DZK_HOSTS=10.0.140.22:2181 -Djava.ext.dirs=lib/ com.zipeiyi.game.gateServer.netty.NettyServerStart $1 &
else
  nohup /opt/java/jdk1.8.0_51/bin/java -DZK_HOSTS=10.0.140.22:2181 -Djava.ext.dirs=lib/ com.zipeiyi.game.gateServer.netty.NettyServerStart &
fi
echo "--------run gateserver stop------->>"