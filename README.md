# AndroidInterceptor
使用Aspectj检查是否有输入Demo

### 项目 build.gradle 依赖

```
dependencies {
  classpath fileTree(dir:'plugins', include:['*.jar'])
}
```

### app build.gradle

```
apply plugin: 'android-aspectjx'
```
