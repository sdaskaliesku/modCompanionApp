package com.manson.fo76.processor.gui;

import com.manson.fo76.settings.SettingsService;
import javafx.scene.control.Tab;

public abstract class ModGuiController {

  protected SettingsService settingsService;

  public ModGuiController(SettingsService settingsService) {
    this.settingsService = settingsService;
  }

  public abstract Tab createModSettingsTab();

}
