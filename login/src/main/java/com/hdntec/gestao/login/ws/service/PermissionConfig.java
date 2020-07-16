/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.login.ws.service;

/**
 *
 * @author doleron
 */
public class PermissionConfig {

    private boolean profile1Action1;
    private boolean profile1Action2;
    private boolean profile2Action1;
    private boolean anAction;

    public boolean isAnAction() {
        return anAction;
    }

    public void setAnAction(boolean anAction) {
        this.anAction = anAction;
    }

    public boolean isProfile1Action1() {
        return profile1Action1;
    }

    public void setProfile1Action1(boolean profile1Action1) {
        this.profile1Action1 = profile1Action1;
    }

    public boolean isProfile1Action2() {
        return profile1Action2;
    }

    public void setProfile1Action2(boolean profile1Action2) {
        this.profile1Action2 = profile1Action2;
    }

    public boolean isProfile2Action1() {
        return profile2Action1;
    }

    public void setProfile2Action1(boolean profile2Action1) {
        this.profile2Action1 = profile2Action1;
    }

}
