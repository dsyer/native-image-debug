Debug a very basic Spring Boot project using a `CommandLineRunner` bean.

Pre-requisites: GraalVM with `native-image` version 21.3.0. You need the Enterprise Edition to get local variable information. You need `gdb` at least 10.1 (not the one from your distro whetever that is).

## Setup

Take a normal `@SpringBootApplication` and add some native hints for the debug symbols:

```
@SpringBootApplication
@NativeHint(options = {"-g", "-O0"})
public class CommandlinerunnerApplication {
	...
}
```

These flags work with the Community Edition too, but you won't get any local variable information.

## Build and Run

First build a native image:

```
$ ./mvnw clean install -DskipTests -P native
```

The result is an executable in `target`:

```
$ $ ./target/commandlinerunner
Oct 21, 2021 12:08:57 PM org.springframework.boot.SpringApplication <init>
INFO: AOT mode enabled
Oct 21, 2021 12:08:57 PM org.springframework.nativex.NativeListener onApplicationEvent
INFO: This application is bootstrapped with code generated with Spring AOT

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::             (v2.6.0-M3)

Oct 21, 2021 12:08:57 PM org.springframework.boot.StartupInfoLogger logStarting
INFO: Starting CommandlinerunnerApplication v0.0.1-SNAPSHOT using Java 17.0.1 on tower with PID 14766 (/home/dsyer/dev/scratch/graal/commandlinerunner/target/commandlinerunner started by dsyer in /home/dsyer/dev/scratch/graal/commandlinerunner)
...
commandlinerunner running! 0
Oct 21, 2021 12:08:57 PM com.example.commandlinerunner.CLR run
INFO: INFO log message 3
Oct 21, 2021 12:08:57 PM com.example.commandlinerunner.CLR run
WARNING: WARNING log message 4
Oct 21, 2021 12:08:57 PM com.example.commandlinerunner.CLR run
SEVERE: ERROR log message 5
```

The process doesn't exit by itself - you have to CTRL-C out of it.

## Debug in VSCode

In VSCode you can install either the standard C++ extension which has support for the `cppdbg` launch type, or the GraalVM extension which has a `nativeimage` launch type. The downside of using the GraalVM extension is you can't use the Redhat Java extension or any of the Spring Boot tools at the same time.

With the standard C++ extension, the launcher looks like this (in `.vscode/launch.json`):

```
{
	"name": "(gdb) Launch",
	"type": "cppdbg",
	"request": "launch",
	"program": "${workspaceFolder}/target/commandlinerunner",
	"args": [],
	"stopAtEntry": false,
	"cwd": "${workspaceFolder}/target",
	"environment": [],
	"externalConsole": false,
	"MIMode": "gdb",
	"setupCommands": [
		{
			"description": "Enable pretty-printing for gdb",
			"text": "-enable-pretty-printing",
			"ignoreFailures": true
		},
		{
			"description": "Map sources",
			"text": "set substitute-path /home/dsyer/spring-native/target/sources/com/example ../src/main/java/com/example",
			"ignoreFailures": true
		}
	]
},
```

Most of this is generated for you by the wizard when you create a new launcher. The bits that are not are the `program`, `cwd` and the `substitute-path`. The `substitute-path` is necessary so that `gdb` can find the application sources for visual debugging. Note that the path in the example above is specific to my set up - you would need a different local version with the correct absolute path to the source cache.

The GraalVM launcher is simpler, but the resulting experience debugging is the same:

```
{
	"type": "nativeimage",
	"request": "launch",
	"name": "Launch Native Image",
	"nativeImagePath": "${workspaceFolder}/target/commandlinerunner",
	"miDebugger": "gdb"
},
```