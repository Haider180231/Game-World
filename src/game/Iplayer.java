package game;

import java.util.List;

/**
 * Represents a player in the game.
 */
public interface Iplayer {

  String getName();

  Tuple<Integer, Integer> getCoordinates();

  void move(Tuple<Integer, Integer> newCoordinates);

  List<Iitem> getItems();

  void addItem(Iitem item);

  int getMaxItems();

  String lookAround(Igameworld world);

  /**
   * Attacks the target character.
   *
   * @param target the target character to attack
   * @param world the game world to check visibility
   * @return the result of the attack as a string
   */
  String attack(Itarget target, Igameworld world);

  /**
   * Determines if this player can see another player.
   *
   * @param other the other player to check
   * @param world the game world
   * @return true if this player can see the other player, false otherwise
   */
  boolean canSee(Iplayer other, Igameworld world);
  
  public void takeTurn(Igameworld world);
}
