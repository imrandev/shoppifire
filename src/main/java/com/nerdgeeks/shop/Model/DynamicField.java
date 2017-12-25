package com.nerdgeeks.shop.Model;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

public class DynamicField {

    private JFXComboBox jfxComboBox;
    private JFXTextField jfxTextField;

    public DynamicField() {
    }

    public DynamicField(JFXComboBox jfxComboBox, JFXTextField jfxTextField) {
        this.jfxComboBox = jfxComboBox;
        this.jfxTextField = jfxTextField;
    }

    public JFXComboBox getJfxComboBox() {
        return jfxComboBox;
    }

    public void setJfxComboBox(JFXComboBox jfxComboBox) {
        this.jfxComboBox = jfxComboBox;
    }

    public JFXTextField getJfxTextField() {
        return jfxTextField;
    }

    public void setJfxTextField(JFXTextField jfxTextField) {
        this.jfxTextField = jfxTextField;
    }
}
