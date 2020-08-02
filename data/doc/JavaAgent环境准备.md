

使用JavaAgent，我们的JDK版本要在1.6以上。

JDK的环境的安装,可参考这个文章
[Java 开发环境配置](https://www.runoob.com/java/java-environment-setup.html)

本项目在开发过程中使用的的开发环境是 IDEA 2019版

IDEA的环境,可参考这个文章
[IDEA 2019.3破解激活详解，可激活到2089年](https://mp.weixin.qq.com/s?src=11&timestamp=1596356489&ver=2497&signature=*wqOm7Nst2I0Z7TR*m5rlepTZWuIj3ZjNSDIylS9acO9TVaNjjbzJibdN3FMTKPTdMlhAxeGIpP2GdImnRaTGpnXmevdCx2kxtR1L5NrPdAsqNYEN-bOH38InIl3PReP&new=1)

Maven 3.2 要求 JDK 1.6 或以上
[Maven 环境配置](https://www.runoob.com/maven/maven-setup.html)



打包准备

使用maven打包插件，将MANIFEST.MF打包到jar中

> 提示MANIFEST.MF文件最后一行要换行，否则可能会出错

```xml
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifestFile>
                            src/main/resources/META-INF/MANIFEST.MF
                        </manifestFile>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
```



