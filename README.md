编译 
```bash
javac -Djava.ext.dirs=lib/ src/club/qixqi/vote/*.java
```

#### /config/springmvc.xml
```xml
<load-on-startup>1</load-on-startup>
```
1. load-on-startup元素标记容器是否在启动的时候就加载这个servlet(实例化并调用其init()方法)。
2. 它的值必须是一个整数，表示servlet应该被载入的顺序
3. 当值为0或者大于0时，表示容器在应用启动时就加载并初始化这个servlet；
4. 当值小于0或者没有指定时，则表示容器在该servlet被选择时才会去加载。
5. 正数的值越小，该servlet的优先级越高，应用启动时就越先加载。
6. 当值相同时，容器就会自己选择顺序来加载。


## Problem
#### /config/springmvc.xml
视图解析器 InternalResourceViewResolver 为什么能访问到 /WEB-INF/jsp/下的文件，还是只是浏览器不能通过 url 访问 /WEB-INF/jsp/ 下的文件



## 后续

使用 redis 缓存数据，nginx + tomcat 集群，自定义投票系统等。