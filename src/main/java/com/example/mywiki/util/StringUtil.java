package com.example.mywiki.util;

import java.util.LinkedList;
import java.util.List;

public class StringUtil {
 public static List<Long> StrToLong(List<String> s) {
     List<Long> l = new LinkedList<>();
     for (String value : s) {
         assert false;
         l.add(Long.parseLong(value));
     }
     return l;

 }

}
