#!/bin/bash
echo "--------run gateServer start------->>"
if [ x$1 != x ]
then
   nohup java -DZK_HOSTS=10.0.140.22:2181 -Djava.ext.dirs=lib/ com.zipeiyi.game.gateServer.netty.NettyServerStart $1 &
else
  nohup java -DZK_HOSTS=10.0.140.22:2181 -Djava.ext.dirs=lib/ com.zipeiyi.game.gateServer.netty.NettyServerStart &
fi
echo "--------run gateserver stop------->>"