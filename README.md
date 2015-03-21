# Message extractor for PlayFramework

This project is forked from xitrum-framework/scala-xgettext.

This plugin extract Internationalized message keys from scala source codes and twirl templates
where `play.api.i18n.Messages` is used in your PlayFramework project.


## Usage

Clone project and publish to local(Sorry not published to sonatype yet)

```
git clone https://github.com/georgeOsdDev/play-xgettext.git
cd play-xgettext
sbt publish-local
```

Add to application's build.sbt

```
autoCompilerPlugins := true

addCompilerPlugin("tv.cntt" %% "play-xgettext" % "1.3")

# If you don't need comment about scala code line
# scalacOptions += "-P:play-xgettext:hideLines"
```

Create empty "messages.default" to project root

```
  cd Your_PlayFramework_Project_Root
  rm -rf ./messages.default
  touch messages.default
```

Compile project

```
  activater clean compile
```

All keys used with ``play.api.i18n.Messages`` in your project will be extracted into ``messages.default``.
And then you can copy it to your internationalized message file like ``conf/messages.ja``