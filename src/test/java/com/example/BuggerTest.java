package com.example;

import org.junit.Test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class BuggerTest
{

    @Test
    public void thisTestShouldBeMarkedAsFailed() throws Exception
    {
        RuntimeException mockedException = mock( RuntimeException.class );
        Bugger bugger = mock(Bugger.class);
        doThrow( mockedException ).when( bugger ).run();

        Runner runner = new Runner();
        runner.runBugger( bugger );
    }
}
