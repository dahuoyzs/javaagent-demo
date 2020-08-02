提供的服务允许Java编程语言代理检测运行在JVM上的程序。检测的机制是方法字节码的修改。
代理被部署为JAR文件。JAR文件清单中的属性指定了将被加载以启动代理的代理类。代理可以通过几种方式启动：

对于支持命令行界面的实现，可以通过在命令行上指定选项来启动代理。

一个实现可能支持一种机制，可以在VM启动后的某个时间启动代理。例如，一种实现可以提供一种机制，该机制允许工具附加到正在运行的应用程序，并启动将工具的代理加载到正在运行的应用程序中。

代理可以与应用程序打包在可执行JAR文件中。

下面介绍了每种启动代理的方式。

从命令行界面启动代理
如果实现提供了从命令行界面启动代理的方法，则通过在命令行中添加以下选项来启动代理：

-javaagent:<jarpath>[=<options>]
其中<jarpath>是代理JAR文件的路径，并且 <options>是代理选项。
代理JAR文件的清单必须 Premain-Class在其主清单中包含该属性。此属性的值是代理类的名称。代理类必须实现一个premain原则上类似于main 应用程序入口点的公共静态方法。初始化Java虚拟机（JVM）之后，premain将先调用该方法，然后再调用实际的应用程序main方法。该premain方法必须返回才能继续启动。

该premain方法具有两个可能的签名之一。JVM首先尝试在代理类上调用以下方法：

public static void premain(String agentArgs, Instrumentation inst)
如果代理类未实现此方法，则JVM将尝试调用：

public static void premain(String agentArgs)
代理类还可能具有agentmain在VM启动后启动代理时使用的方法（请参见下文）。使用命令行选项启动代理时，agentmain不会调用该方法。

每个代理都会通过agentArgs参数传递其代理选项。代理选项作为单个字符串传递，任何其他解析都应由代理本身执行。

如果无法启动代理（例如，因为无法加载代理类，或者因为代理类没有适当的 premain方法），则JVM将中止。如果premain方法抛出未捕获的异常，则JVM将中止。

不需要实现即可提供从命令行界面启动代理的方法。如果这样做，则它支持-javaagent上面指定的 选项。该-javaagent选项可以在同一命令行上多次使用，从而启动多个代理。该premain方法将在该试剂在命令行上指定的顺序调用。一个以上的代理可以使用相同的代理<jarpath>。

代理premain 方法可以做什么没有建模限制。应用程序main可以做的任何事情，包括创建线程，都是合法的premain。

VM启动后启动代理
一个实现可以提供一种机制，可以在VM启动之后的某个时间启动代理。有关如何启动的详细信息是特定于实现的，但通常应用程序已启动且其main方法已被调用。在VM启动后实现支持启动代理的情况下，适用以下规则：

代理JAR的清单必须 Agent-Class在其主要清单中包含该属性。此属性的值是代理类的名称。

代理类必须实现公共静态agentmain 方法。

该agentmain方法具有两个可能的签名之一。JVM首先尝试在代理类上调用以下方法：

public static void agentmain(String agentArgs, Instrumentation inst)
如果代理类未实现此方法，则JVM将尝试调用：

public static void agentmain(String agentArgs)
代理类还可能具有premain使用命令行选项启动代理时使用的方法。在VM启动后启动代理时，premain不会调用该方法。

代理通过agentArgs 参数传递其代理选项。代理选项作为单个字符串传递，任何其他解析都应由代理本身执行。

该agentmain方法应该执行启动代理所需的任何必要的初始化。启动完成后，该方法应返回。如果无法启动代理（例如，因为无法加载代理类，或者因为代理类没有一致的 agentmain方法），则JVM不会中止。如果该agentmain 方法引发未捕获的异常，它将被忽略（但JVM可能会记录该日志以进行故障排除）。

在可执行的JAR文件中包含代理
JAR文件规范为打包为可执行JAR文件的独立应用程序定义清单属性。如果实现支持将应用程序启动为可执行JAR的机制，则主清单可能包含Launcher-Agent-Class 用于指定在main调用应用程序方法之前启动的代理的类名的属性 。Java虚拟机尝试在代理类上调用以下方法：

public static void agentmain(String agentArgs, Instrumentation inst)
如果代理类未实现此方法，则JVM将尝试调用：

public static void agentmain(String agentArgs)
agentArgs参数的值始终是空字符串。

该agentmain方法应执行启动代理并返回所需的任何必要的初始化。如果无法启动代理，例如无法加载代理类，代理类未定义一致性agentmain方法，或者该agentmain方法引发未捕获的异常或错误，则JVM将中止。

加载代理类以及该代理类可用的模块/类
从代理JAR文件加载的类由 系统类加载器加载，并且是系统类加载器的未命名模块的成员。系统类加载器通常也定义包含应用程序main方法的类。

代理类可见的类是系统类加载器可见的类，并且至少包括：

引导层中的模块导出的包中的类。引导层是否包含所有平台模块将取决于初始模块或应用程序的启动方式。

可以由系统类加载器定义的类（通常是类路径）为其未命名模块的成员。

代理程序安排由引导程序类装入器定义的任何类均是其未命名模块的成员。

如果代理类需要链接到不在引导层中的平台（或其他）模块中的类，则可能需要以确保这些模块在引导层中的方式启动应用程序。例如，在JDK实现中，--add-modules可以使用命令行选项将模块添加到一组根模块中，以便在启动时进行解析。

代理程序安排由引导程序类加载器加载的支持类（借助于appendToBootstrapClassLoaderSearch或Boot-Class-Path下面指定的属性）必须仅链接到定义为引导程序类加载器的类。不能保证所有平台类都可以由引导类加载器定义。

如果配置了自定义系统类加载器（通过方法中java.system.class.loader指定的系统属性 getSystemClassLoader），则它必须定义appendToClassPathForInstrumentation中指定的方法 appendToSystemClassLoaderSearch。换句话说，定制系统类装入器必须支持将代理JAR文件添加到系统类装入器搜索的机制。

清单属性
为代理JAR文件定义了以下清单属性：

Premain-Class
在JVM启动时指定了代理时，此属性指定代理类。即，包含该premain方法的类。在JVM启动时指定代理时，此属性是必需的。如果该属性不存在，JVM将中止。注意：这是一个类名，而不是文件名或路径。
Agent-Class
如果实现支持在VM启动后某个时间启动代理的机制，则此属性指定代理类。即，包含该agentmain方法的类。如果不存在此属性，那么将不会启动代理。注意：这是一个类名，而不是文件名或路径。
Launcher-Agent-Class
如果实现支持将应用程序作为可执行JAR启动的机制，则主清单可以包括此属性，以指定在main 调用应用程序方法之前启动的代理的类名。
Boot-Class-Path
引导类加载器要搜索的路径列表。路径代表目录或库（在许多平台上通常称为JAR或zip库）。平台特定类的定位机制失败后，引导类加载器将搜索这些路径。按照列出的顺序搜索路径。列表中的路径由一个或多个空格分隔。路径采用分层URI的路径组件的语法。如果路径以斜杠（'/'）开头，则为绝对路径，否则为相对路径。相对路径针对代理JAR文件的绝对路径进行解析。格式错误和不存在的路径将被忽略。在VM启动后的某个时间启动代理时，将忽略不代表JAR文件的路径。此属性是可选的。
Can-Redefine-Classes
布尔值（true或false，大小写无关）。是重新定义此代理所需的类的能力。值比其他true考虑false。此属性是可选的，默认值为 false。
Can-Retransform-Classes
布尔值（true或false，大小写无关）。是重新转换此代理所需的类的能力。值比其他true 考虑false。此属性是可选的，默认值为 false。
Can-Set-Native-Method-Prefix
布尔值（true或false，大小写无关）。能够设置此代理所需的本机方法前缀。值比其他 true考虑false。此属性是可选的，默认值为false。
代理JAR文件在清单中可能同时具有Premain-Class和 Agent-Class属性。使用该-javaagent选项在命令行上启动代理后，该 Premain-Class属性将指定代理类的名称，并且该 Agent-Class属性将被忽略。同样，如果在虚拟机启动后的某个时间启动了代理，则该Agent-Class属性指定代理类的名称（Premain-Class忽略属性值）。

在模块中插入代码
为了帮助在引导类加载器的搜索路径上或在加载主代理类的类加载器的搜索路径上部署支持类的代理，Java虚拟机安排转换后的类的模块读取未命名的模块。两种装载机。

以来：
1.5

[原文链接](https://docs.oracle.com/javase/10/docs/api/java/lang/instrument/package-summary.html)



