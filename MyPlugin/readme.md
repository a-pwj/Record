3.2 构建独立项目的 Gradle 插件

Gradle 插件有三种实现方式，分别为 Build script、buildSrc project 和 Standalone project：

1、Build script 会把逻辑直接写在 build.gradle 文件中，Plugin 只对当前 build.gradle 文件可见；

2、buildSrc project 是将逻辑写在 rootProjectDir/buildSrc/src/main/java（最后一个路径文件夹也可以是 groovy 或 kotlin，主要取决于你用什么语言去实现自定义插件）
目录下，Plugin 只对当前项目生效；

3、Standalone project 是将逻辑写在独立项目里，可以直接编译后把 JAR 包发布到远程仓库或者本地。

基于本文的写作目的，这里我们主要讲解 Standalone project，即独立项目的 Gradle 插件。

3.2.1 目录结构解析

在 main 文件夹下分为 groovy 文件夹与 resources 文件夹：


 groovy 文件夹下是源码文件（Gradle 插件也支持使用 Java 与 Kotlin 编写，此处文件夹名根据实际语言确定）；
 resources 文件夹下面是资源文件。


其中，resources 文件夹下是固定格式的
META-INF/gradle-plugins/XXXX.properties，XXXX 就代表以后使用插件时需要指定的 plugin id。

目前 Android Studio 对于 Gradle 插件开发的支持不够好，很多 IDE 本可以完成的工作都需要我们手动完成，例如：

1、Android Studio 不能够直接新建 Gradle 插件的 Module，只能先新建一个 Java Library 类型的 Module，再把多余的文件夹删除；

2、新建类默认是新建 Java 的类，新建的文件名后缀是 “.java”，想要新建 Groovy 语法的类需要手动新建一个后缀为 “.groovy” 的文件，然后添加上 package、class 声明；

3、resources 整个都需要手动创建，文件夹名需要注意拼写；

4、删除掉 Module 的 build.gradle 全部内容，新加上 Gradle 插件开发需要的 Gradle 插件、依赖等。

3.2.2 编写插件

在写插件的代码之前，我们需要对 build.gradle 做些修改，如下所示：
apply plugin: 'groovy'
apply plugin: 'maven' 
dependencies {  
  implementation gradleApi()   
  implementation localGroovy()
} 
uploadArchives{  
  repositories.mavenDeployer {       
   //本地仓库路径，以放到项目根目录下的 repo 的文件夹为例     
   repository(url: uri('../repo'))       
   //groupId ，自行定义        
   pom.groupId = 'com.sensorsdata.myplugin'       
   //artifactId        
   pom.artifactId = 'MyPlugin'        
   //插件版本号      
   pom.version = '1.0.0'    
  }
}

这里主要分为三部分内容：

1、apply 插件：应用 'groovy' 插件是因为我们的项目是使用 Groovy 语言开发的，'maven' 插件在后面发布插件时会用到；

2、dependencies：声明依赖；

3、uploadArchive：这里是一些 maven 相关的配置，包括发布仓库的位置、groupId、artifactId、版本号，这里为了调试方便把位置选在项目根目录下的 repo 文件夹。

做好以上准备之后，就可以开始源码的编写。Gradle 插件要求入口类需要实现 org.gradle.api.Plugin 接口，然后在实现方法 apply 中实现自己的逻辑：
package com.sensorsdata.pluginclass 
MyPlugin implements Plugin<Project>{  
  @Override    
  void apply(Project project) {  
      println 'Hello,World!' 
  }
}

在这里的示例中，apply 方法就是我们整个 Gradle 插件的入口方法，作用类似于各种语言的 main 方法。apply 方法的入参类型 Project 在第二节中已经进行了解释，这里不再赘述。由于 Plugin 类和 Project 类有非常多的同名类，在导入的时候一定注意选择 org.gradle.api 包下的类。

最后，还需要做一项准备工作：Gradle 插件并不会自动寻找入口类，而是要求开发者把入口类的类名写在 resources/META-INF/gradle-plugins/XXXX.properties 里，内容格式为 “implementation-class=入口类的全限定名”，此处示例项目的配置如下所示：
// com.sensorsdata.plugin.propertiesimplementation-class=com.sensorsdata.plugin.MyPlugin

3.2.3 发布插件

完成编写插件的所有内容后，在终端执行./gradlew uploadArchive复制代码

就可以发布插件。在上一小节编写插件的 build.gradle 文件中提前配置好了发布到 maven 仓库相关的配置，因此我们这里执行该命令后，在项目根目录下就会出现 repo
文件夹，文件夹中包含打包后的 JAR 文件。

3.2.4 使用插件

使用插件主要分别两个步骤：

（1）声明插件

声明插件需要在 Project 级别的 build.gradle 文件中完成，在 build.gradle 文件中有一个块叫做 buildscript，buildscript 块又分为 repositories 块和 dependencies 块。repositories 块用来声明需要引用的依赖所在的远程仓库地址，dependencies 块用来声明具体引用的依赖。这里使用刚刚发布到本地 repo 文件夹 JAR 包为例，参考代码如下：
buildscript {    
    repositories {       
        maven{            
         // 刚刚我们把插件发布到了根目录下面的 repo 文件夹           
         url 'repo'        
    }   
}    
dependencies {       
   // classpath '$group_id:$artifactId:$version'       
    classpath 'com.sensorsdata.myplugin:MyPlugin:1.0.0' 
    }
}
       

（2）应用插件

应用插件需要在 Module 级别的 build.gradle 文件中完成：// apply plugin: 'plugin id'apply plugin: 'com.sensorsdata.plugin'复制代码

完成上述步骤之后，在每次编译的时候都可以在编译日志中看到插件输出的 “Hello,World!”。

3.3 可配置的插件

如果希望插件的功能更加灵活的话，一般会预留一些可配置的参数，就像可以在主 Module 的 “android” 块配置编译的 Android SDK 版本、Build-Tools 版本等。“android” 块的这个配置就是 Gradle 的 Extension，下面我们来做一个自定义的 Extension。

3.3.1
创建 Extension 类

创建一个用于 Extension 的类非常简单：只需要新建一个普通的类，类中定义的属性就是 Extension 可以接收的配置。它不需要继承任何类，也不需要实现任何接口，如下所示：class MyExtension{    public String nam = "name"    public String sur = "surname"}复制代码

3.3.2
实例化 Extension 对象

可以通过 ExtensionContainer 来创建和管理 Extension，ExtensionContainer 对象可以通过 Project 对象的 getExtensions 方法获取：def extension = project.getExtensions().create('myExt',MyExtension)project.afterEvaluate {    println("Hello from " + extension.toString())}复制代码

上面的代码片段可以直接复制到 apply 方法中或者放在
build.gradle 文件中使用。这里使用到了 create 方法来创建 Extension，我们来看下 create 方法的定义：<T> T create(String name, Class<T> type, Object... constructionArguments);复制代码

1、name：代表要创建的 Extension 的名字，例如：build.gradle 中名为 “android” 的块，Android Gradle 插件在创建这个 Extension 的时候 name 就需要填 “android”。Extension 的 name 不能和已有的重复，例如： Android Gradle 插件创建的 Extension name 为 “android”，那么其它 Extension name 就不可以再使用 “android”；

2、type：该
Extension 的类类型，这里的类就是上一小节创建的类，注意类的属性名与 Extension 中的属性名需要一致；

3、constructionArguments：类的构造函数参数值。

使用 create 方法之后，你可能会迫不及待的在下一行立即打印出
Extension 对象的值，不过这么做的话你会发现 Extension 对象打印出来的值并不对。不论你在 build.gradle 中怎么配置，Extension 对象就是读不到值。具体原因可以回顾下这里的示例，你会发现示例里打印的逻辑写在了 afterEvaluate 方法中。这里的写法跟插件的生命周期有很大的关系，我们将在下一节中介绍 Gradle 插件的生命周期。

四、Gradle
构建的生命周期

官方对于 Gradle 构建的生命周期的定义：Gradle 的核心是一种基于依赖的语言，用 Gradle 的术语来说这意味着你能够定义 Task 和 Task 之间的依赖关系。Gradle 会保证这些 Task 按照依赖关系的顺序执行并且每个 Task 只会被执行一次，这些 Task 根据依赖关系构成了一个有向无环图。Gradle 在执行任何 Task 之前都会用内部的构建工具去完成这样这样一个图，这就是 Gradle 的核心。这种设计使得很多原本不可能的事情成为可能。

每次 Gradle 构建都需要经过三个不同的阶段：

1、初始化阶段：Gradle 是支持单项目和多项目构建的，因此在初始化阶段，Gradle 会根据 settings.gradle 确定需要参与构建的项目，并为每个项目创建一个 Project 实例。Android Studio 的项目和 Module 对 Gradle 来说都是项目；

2、配置阶段：在这个阶段会配置 Project 对象，并且所有项目的构建脚本都会被执行。例如：Extension 对象、Task 对象等都是在这个阶段被放到 Project 对象里；

3、执行阶段：经过了配置阶段，此时所有的 Task 对象都在 Project 对象中。然后，会根据终端命令指定的 Task 名字从 Project 对象中寻找对应的 Task 对象并执行。

Gradle 提供了很多生命周期的监听方法，用来在特定的阶段执行特定的任务。这里选取了部分回调方法，按照执行顺序关系画了一份 Gradle 生命周期流程简图，如图 4-1 所示：

图 4-1 Gradle 生命周期流程简图

图中的生命周期回调方法里，属于 Project 有
project.beforeEvaluate 和 project.afterEvaluate，它们的触发时机分别是在 Project 进行配置前和配置结束后。在之前的示例中，正是使用了这里的
afterEvaluate，由于方法的最后一个参数是闭包所以写法优化成了 afterEvaluate{}。

使用 create 创建的对象，在对应 Project 还没配置完成的时候打印出来的值自然是不正确的，需要在配置完成后才能正确获取到写在 build.gradle 中的 Extension 值。因为直接写在 apply 方法里的逻辑是在配置阶段执行的，所以会出现这种情况。
