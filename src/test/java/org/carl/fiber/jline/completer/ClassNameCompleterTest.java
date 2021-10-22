package org.carl.fiber.jline.completer;

import org.carl.fiber.jline.compeleter.ClassNameCache;
import org.carl.fiber.jline.compeleter.ClassNameCompleter;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ClassNameCompleterTest {

    @Test
    public void fun() {
        ClassNameCache nameCache = new ClassNameCache();
        nameCache.preLoad();
        ClassNameCompleter completer = new ClassNameCompleter(nameCache.getClassNameCache());
        List<CharSequence> arrayList = new ArrayList<>();
        System.out.println(completer.complete("", 2, arrayList));
        System.out.println(arrayList);
    }
}
