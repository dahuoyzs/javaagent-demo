# Java Agent教程



### 简介

Java Agent是在JDK1.5以后，我们可以使用agent技术构建一个独立于应用程序的代理程序（即为Agent），用来协助监测、运行甚至替换其他JVM上的程序。使用它可以实现虚拟机级别的AOP功能。

Agent分为两种，一种是在主程序之前运行的Agent，一种是在主程序之后运行的Agent（前者的升级版，1.6以后提供）。



### JavaAgent的作用

##### Agent给我们程序带来的影响.jpg

![Java-Agent](https://gitee.com/dahuoyzs/res/raw/master/img/Java-Agent.jpg)



##### 使用Agent-premain方法影响的程序效果图.jpg

![image-20200802151946748](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200802151946748.png)

##### 使用Agent-agentmain方法影响的程序效果图.jpg

![image-20200802153238246](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200802153238246.png)





### JavaAgent相关的API

在java.lang.instrument包下 给我们提供了相关的API

而最为主要的就是Instrumentation这个接口中的几个方法

![image-20200802151335773](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200802151335773.png)



```java
public interface Instrumentation {
    
    /**
     * 添加Transformer(转换器) 
     * ClassFileTransformer类是一个接口，通常用户只需实现这个接口的  byte[] transform()方法即可；
     * transform这个方法会返回一个已经转换过的对象的byte[]数组
     * @param transformer            拦截器
     * @return canRetransform        是否能重新转换
     */
 	void addTransformer(ClassFileTransformer transformer, boolean canRetransform);    

    /**
     * 重新触发类加载，
     * 该方法可以修改方法体、常量池和属性值，但不能新增、删除、重命名属性或方法，也不能修改方法的签名
     * @param classes           Class对象
     * @throws  UnmodifiableClassException       异常
     */
    void retransformClasses(Class<?>... classes) throws UnmodifiableClassException;

    /**
     * 直接替换类的定义
     * 重新转换某个对象,并已一个新的class格式，进行转化。
     * 该方法可以修改方法体、常量池和属性值，但不能新增、删除、重命名属性或方法，也不能修改方法的签名
     * @param definitions           ClassDefinition对象[Class定义对象]
     * @throws  ClassNotFoundException,UnmodifiableClassException       异常
     */
    void redefineClasses(ClassDefinition... definitions)throws  ClassNotFoundException, UnmodifiableClassException;

    /**
     * 获取当前被JVM加载的所有类对象
     * @return Class[]        class数组
     */
    Class[] getAllLoadedClasses();
}
```

后面我们会在代码中具体用到这些方法。在详细说明。


[Java官方文档对Agent的解释-译](data/doc/Java官方文档对Agent的解释-译.md)

[JavaAgent教程](data/doc/JavaAgent教程.md)

[JavaAgent环境准备](data/doc/JavaAgent环境准备.md)

[JavaAgent-premain方法1-初探](data/doc/JavaAgent-premain方法1-初探.md)

[JavaAgent-premain方法2-实现修改代码逻辑](data/doc/JavaAgent-premain方法2-实现修改代码逻辑.md)

[JavaAgent-premain方法3-实现方法耗时统计](data/doc/JavaAgent-premain方法3-实现方法耗时统计.md)

[JavaAgent-agentmain方法1-实现运行时修改程序](data/doc/JavaAgent-agentmain方法1-实现运行时修改程序.md)

[JavaAgent-agentmain方法2-实现动态修改日志界别](data/doc/JavaAgent-agentmain方法2-实现动态修改日志界别.md)

[JavaAgent特性实现热加载](data/doc/JavaAgent特性实现热加载.md)

[几种class文件的处理方法](data/doc/几种class文件的处理方法.md)

[参考链接](data/doc/参考链接.md)








