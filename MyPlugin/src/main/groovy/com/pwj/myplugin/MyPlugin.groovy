import org.gradle.api.Plugin
import org.gradle.api.Project



/**
 *  Gradle 插件有三种实现方式，分别为 Build script、buildSrc project 和 Standalone project
 *
 *  Gradle Standalone project 插件项目:
 *  在 main 文件夹下分为 groovy 文件夹与 resources 文件夹：
 *  groovy 文件夹下是源码文件（Gradle 插件也支持使用 Java 与 Kotlin 编写，此处文件夹名根据实际语言确定）；
 *  resources 文件夹下面是资源文件。
 *  其中，resources 文件夹下是固定格式的META-INF/gradle-plugins/XXXX.properties，XXXX 就代表以后使用插件时需要指定的 plugin id。
 *
 *
 *  目前 Android Studio 对于 Gradle 插件开发的支持不够好，很多 IDE 本可以完成的工作都需要我们手动完成，例如：
 *
 *  1、Android Studio 不能够直接新建 Gradle 插件的 Module，只能先新建一个 Java Library 类型的 Module，再把多余的文件夹删除；
 *
 *  2、新建类默认是新建 Java 的类，新建的文件名后缀是 “.java”，想要新建 Groovy 语法的类需要手动新建一个后缀为 “.groovy” 的文件，然后添加上 package、class 声明；
 *
 *  3、resources 整个都需要手动创建，文件夹名需要注意拼写；
 *
 *  4、删除掉 Module 的 build.gradle 全部内容，新加上 Gradle 插件开发需要的 Gradle 插件、依赖等。
 *
 *
 *  apply 方法就是我们整个 Gradle 插件的入口方法，作用类似于各种语言的 main 方法。
 *  apply 方法的入参类型 Project 在第二节中已经进行了解释，这里不再赘述。
 *  由于 Plugin 类和 Project 类有非常多的同名类，在导入的时候一定注意选择 org.gradle.api 包下的类。
 *
 *  最后，还需要做一项准备工作：Gradle 插件并不会自动寻找入口类，
 *  而是要求开发者把入口类的类名写在 resources/META-INF/gradle-plugins/XXXX.properties 里，
 *  内容格式为 “implementation-class=入口类的全限定名”
 *
 */
class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println 'Hello,World!'
    }
}