package com.homedev.tests;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 05/03/11
 * Time: 22:19
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    private List<?> slu;

    public Test(List<?> sl) {
        this.slu = sl;
    }

    public void test() {

        List<String> sl = new ArrayList<String>();
        ArrayList<Object> ol = new ArrayList<Object>();
//        sl.add(ol.get(0))

//           String[] sa = new String[0];
//        Object o = sa;
//        Object[] oa = new Object[1];
//        test2(sl);
    }

    public static void test2(List<? extends String> l)
    {
//        final Object t = l.get(0).equals();
//        l.add(t);
    }


}
