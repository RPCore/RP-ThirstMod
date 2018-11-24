/*
 * Class By M4TH1EU
 */

package ch.m4th1eu.thirstmod.common.content;

import net.minecraft.item.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Drink
        implements Serializable {
    public static List<Drink> ALL_DRINKS = new ArrayList();
    public String drinkName;
    public int thirstReplenish;
    public float saturationReplenish;
    public int drinkColor;
    public boolean alwaysDrinkable;
    public boolean shiny;
    public String recipeItem;
    public float poisonChance;

    public Drink(String name, int thirst, float sat, int color) {
        this.drinkName = name;
        this.thirstReplenish = thirst;
        this.saturationReplenish = sat;
        this.drinkColor = color;
    }

    public Item getItem() {
        return Item.getByNameOrId(this.recipeItem);
    }

    public static void registerDrink(Drink drink) {
        ALL_DRINKS.add(drink);
    }

    public static Drink getDrinkByIndex(int i) {
        return ALL_DRINKS.get(i);
    }
}
