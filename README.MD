## Fiber
* 一个基于JDB且对其使用体验进行优化

### 支持的版本
* 支持`JDK8`以及之后的版本

### 优化JDB内容
* 这部分由 `jline` 库功能来进行实现
    1. 增加了控制台的控制按键的实现(包括且不限于)
        * `ctrl + u`,清空输入行数据.
        * `ctrl + e`,移动光标到行末.
        * `ctrl + l`,清空整个控制台屏幕.
        * 能够在输入时自由的移动光标,而非将对应移动光标按键读取为输入内容.
    2. 增加了命令的提示与补全.
        * 在输入时,通过 `tab` 按键来获取对应的命令提示
        * 在 `jline` 的基础上,添加了类名称检索的补全,可以按照提示来找到想找的类名称.

### 实现原理
* 通过字节码修改覆盖掉原来 `JDB` 的主类 `com.sun.tools.example.debug.tty.TTY`
    * 主要是修改了读取控制台输入的逻辑,由`jline`来进行读取输入内容.

### 使用程序
* `JDK8`
    1. 将程序需要使用到的`四个包`的路径地址`组成classpath`.
    2. 通过命令行启动该程序
        * `java -cp Fiber.jar:jline.jar:tools.jar:asm.jar org.carl.fiber.JDB`
    3. 由于只对JDB读取输入内容进行了修改,并未对其内部读取命令后的执行进行修改.故其操作方式与JDB一致.
* `JDK9+`
    1. JDK在版本9之后的更新,不再提供了`${JAVA_HOME}/lib/tools.jar`.
    2. 需要使用三个包 `jline.jar`,`asm.jar`,`Fiber.jar`,`组成classpath`.
    3. 添加反射权限 `--add-opens jdk.jdi/com.sun.tools.example.debug.tty=ALL-UNNAMED` 放开反射权限
    4. 完整命令为 `java --add-opens jdk.jdi/com.sun.tools.example.debug.tty=ALL-UNNAMED -cp Fiber.jar:jline.jar:asm.jar org.carl.fiber.JDB`

