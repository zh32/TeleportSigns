package de.zh32.teleportsigns;

import lombok.Getter;

/**
 *
 * @author zh32
 */
public class TeleportSigns {
  
    @Getter
    private static TeleportSigns instance;
    private final TeleportSignsPlugin plugin;

    protected TeleportSigns(TeleportSignsPlugin plugin) {
        instance = this;
        this.plugin = plugin;
    }
    
    public void addSignLayout(SignLayout layout) {
        plugin.getData().getSignLayouts().put(layout.getName(), layout);
    }
    
    public void removeSignLayout(SignLayout layout) {
        plugin.getData().getSignLayouts().remove(layout.getName());
    }
}