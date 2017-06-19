package com.zemian.spacejoystick;

/**
 * Created by Oleksandr on 12/06/2017.
 */

public interface IPlayerListener {
    //void onAttributeValueChange(Attribute attr);
    void onSkinChange(ShipSkin skin);
    void onPointsValueChange(int newValue);
}
