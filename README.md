## How to reproduce problem

Run the following

    ./gradlew check

## Result

Gradle junit runner crashes because stacktrace is null:

```
:compileJava
:processResources NO-SOURCE
:classes
:compileTestJava
:processTestResources NO-SOURCE
:testClasses
:test
Unexpected exception thrown.
org.gradle.internal.remote.internal.MessageIOException: Could not read message from '/0:0:0:0:0:0:0:1:48260'.
        at org.gradle.internal.remote.internal.inet.SocketConnection.receive(SocketConnection.java:90)
        at org.gradle.internal.remote.internal.hub.MessageHub$ConnectionReceive.run(MessageHub.java:263)
        at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:63)
        at org.gradle.internal.concurrent.StoppableExecutorImpl$1.run(StoppableExecutorImpl.java:46)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
        at java.lang.Thread.run(Thread.java:745)
Caused by: java.lang.NullPointerException
        at java.lang.Throwable.setStackTrace(Throwable.java:864)
        at org.gradle.internal.serialize.ExceptionPlaceholder.read(ExceptionPlaceholder.java:154)
        at org.gradle.internal.serialize.TopLevelExceptionPlaceholder.read(TopLevelExceptionPlaceholder.java:24)
        at org.gradle.internal.serialize.ExceptionReplacingObjectInputStream.doResolveObject(ExceptionReplacingObjectInputStream.java:67)
        at org.gradle.internal.serialize.ExceptionReplacingObjectInputStream$1.transform(ExceptionReplacingObjectInputStream.java:31)
        at org.gradle.internal.serialize.ExceptionReplacingObjectInputStream.resolveObject(ExceptionReplacingObjectInputStream.java:62)
        at java.io.ObjectInputStream.checkResolve(ObjectInputStream.java:1583)
        at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1535)
        at java.io.ObjectInputStream.readObject(ObjectInputStream.java:422)
        at org.gradle.internal.serialize.Message.receive(Message.java:55)
        at org.gradle.internal.serialize.BaseSerializerFactory$ThrowableSerializer.read(BaseSerializerFactory.java:235)
        at org.gradle.internal.serialize.BaseSerializerFactory$ThrowableSerializer.read(BaseSerializerFactory.java:233)
        at org.gradle.internal.remote.internal.hub.DefaultMethodArgsSerializer$ArraySerializer.read(DefaultMethodArgsSerializer.java:71)
        at org.gradle.internal.remote.internal.hub.DefaultMethodArgsSerializer$ArraySerializer.read(DefaultMethodArgsSerializer.java:60)
        at org.gradle.internal.remote.internal.hub.MethodInvocationSerializer$MethodInvocationReader.readArguments(MethodInvocationSerializer.java:124)
        at org.gradle.internal.remote.internal.hub.MethodInvocationSerializer$MethodInvocationReader.read(MethodInvocationSerializer.java:119)
        at org.gradle.internal.remote.internal.hub.MethodInvocationSerializer$MethodInvocationReader.read(MethodInvocationSerializer.java:99)
        at org.gradle.internal.serialize.kryo.TypeSafeSerializer$1.read(TypeSafeSerializer.java:34)
        at org.gradle.internal.remote.internal.hub.InterHubMessageSerializer$MessageReader.read(InterHubMessageSerializer.java:66)
        at org.gradle.internal.remote.internal.hub.InterHubMessageSerializer$MessageReader.read(InterHubMessageSerializer.java:52)
        at org.gradle.internal.remote.internal.inet.SocketConnection.receive(SocketConnection.java:79)
        ... 6 more
:check

BUILD SUCCESSFUL
```

Notice that the final line says `BUILD SUCCESSFUL` which is wrong.

## Required workaround

If one makes sure to return a non-null stack trace then the junit runner works as expected:

```
doReturn(new StackTraceElement[0]).when(mockedException).getStackTrace();
```

```
:compileJava
:processResources NO-SOURCE
:classes
:compileTestJava
:processTestResources NO-SOURCE
:testClasses
:test

com.example.BuggerTest > thisTestShouldBeMarkedAsFailed FAILED
    $java.lang.RuntimeException$$EnhancerByMockitoWithCGLIB$$4bc6e699

1 test completed, 1 failed
:test FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':test'.
> There were failing tests. See the report at: file:///home/jonas/work/gradle-junit-bug/build/reports/tests/test/index.html

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.

BUILD FAILED
```
