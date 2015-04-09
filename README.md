# i18n messages extractor for PlayFramework

This project is forked from xitrum-framework/scala-xgettext.

This plugin extract Internationalized message keys from scala source codes and twirl templates
where `play.api.i18n.Messages` is used in your PlayFramework project.


## Usage

Add to application's build.sbt

```
autoCompilerPlugins := true

addCompilerPlugin("com.github.georgeosddev" %% "play-xgettext" % "0.1")

// If you don't need comment about scala code line
// scalacOptions += "-P:play-xgettext:hideLines"
```

Create empty ``messages.default`` to project root

```
cd Your_PlayFramework_Project_Root
rm -rf ./messages.default
touch messages.default
```

Compile project

```
activator clean compile
```

All keys used with ``play.api.i18n.Messages`` in your project will be extracted into ``messages.default``.
And then you can copy it to your internationalized message file like ``conf/messages.ja``
