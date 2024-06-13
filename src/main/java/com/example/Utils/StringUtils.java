package com.example.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
  private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

  public static boolean containsSpecialCharacters(String s) {
    Matcher matcher = SPECIAL_CHAR_PATTERN.matcher(s);
    if (s.isEmpty()) {
      return true;
    }
    return matcher.find();
  }
}
