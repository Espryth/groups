package me.espryth.groupsplugin.tab;

import org.jetbrains.annotations.NotNull;

/**
 * A utility class to get the tab weight.
 */
public interface TabWeight {

  char[] ABC = "ZYXWVUTSRQPONMLKJIHGFEDCBAzyxwvutsrqponmlkjihgfedcba".toCharArray();

  /**
   * Get the weight of the tab.
   *
   * @param weight The weight number.
   * @return The weight character.
   */
  static @NotNull String get(final int weight) {
    if (weight <= 0 || weight >= ABC.length) {
      return String.valueOf(ABC[0]);
    }
    return String.valueOf(ABC[weight]);
  }
}
