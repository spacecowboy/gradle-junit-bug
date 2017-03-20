package com.example;

import org.junit.Test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class BuggerTest {

    @Test
    public void thisTestShouldBeMarkedAsFailed() throws Exception {
        RuntimeException mockedException = mock(RuntimeException.class);
        Bugger bugger = mock(Bugger.class);
        doThrow(mockedException).when(bugger).run();
        // This line is required to get correct behavior
        // doReturn(new StackTraceElement[0]).when(mockedException).getStackTrace();

        Runner runner = new Runner();
        runner.runBugger(bugger);
    }
}
