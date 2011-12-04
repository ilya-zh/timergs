package com.homedev.tests;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 09/05/11
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class JunitTest extends TestCase
{

    @org.junit.Test public void testSimpleMath()
    {
      int i = 2;
        int a = 2;
        int expected = 4;
        int result = i+a;
        assertEquals(result, expected);

    }

}
