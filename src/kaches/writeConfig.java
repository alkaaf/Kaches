/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaches;

import java.util.prefs.Preferences;

/**
 *
 * @author alkaaf
 */
public class writeConfig {
    Preferences pf;

    public writeConfig() {
        pf = Preferences.userRoot();
    }
    public void setConfig(String key, String value){
        pf.put(key, value);
    }
    public String getConfig(String key){
        return pf.get(key, key);
    }
}
