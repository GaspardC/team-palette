package com.epfl.computational_photography.paletizer.SlideMenu;

/**
 * Created by Johan on 30.11.2015.
 */
public class NavItem {
    String name;
    String description;
    int icon;
    Class<?> linkedActivity = null;
    Runnable action = null;

    /**
     * Create a NavItem.
     *
     * If you want to launch a new activity, you may want to use
     * @see #NavItem(String, String, int, Class)
     *
     * @param name name of the item_drink
     * @param description short description, will be visible under the name
     * @param icon
     * @param action action performed when the item_drink is clicked. It will be run <b>synchronously</b>
     */
    public NavItem(String name, String description, int icon, Runnable action){
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.action = action;
    }
    /**
     * @param name name of the item_drink
     * @param description short description, will be visible under the name
     * @param icon
     * @param linkedActivity Activity launched when the item_drink is clicked
     */
    public NavItem(String name, String description, int icon, Class<?> linkedActivity){
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.linkedActivity = linkedActivity;
    }
    /**<b>
     *  The NavItem created will have no effect !
     * </b>
     * <p>
     *  To set an effect, you must add it to
     *  @see SlideMenuActivity#selectItemFromList(int)
     * </p>
     *
     * @param name name of the item_drink
     * @param description short description, will be visible under the name
     * @param icon
     */
    public NavItem(String name, String description, int icon){
        this.name = name;
        this.description = description;
        this.icon = icon;
    }
}
