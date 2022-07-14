package com.tyky.bean;

import com.tyky.webviewBase.model.ParamModel;

import java.util.List;

public class FaceParamModel extends ParamModel {

    /**
     * isOpenSound
     */
    private boolean isOpenSound;
    /**
     * qualityLevel
     */
    private int qualityLevel;
    /**
     * isActionLive
     */
    private boolean isActionLive;
    /**
     * isLivenessRandom
     */
    private boolean isLivenessRandom;
    /**
     * iivenessAction
     */
    private List<Integer> iivenessAction;

    public boolean isIsOpenSound() {
        return isOpenSound;
    }

    public void setIsOpenSound(boolean isOpenSound) {
        this.isOpenSound = isOpenSound;
    }

    public int getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(int qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public boolean isIsActionLive() {
        return isActionLive;
    }

    public void setIsActionLive(boolean isActionLive) {
        this.isActionLive = isActionLive;
    }

    public boolean isIsLivenessRandom() {
        return isLivenessRandom;
    }

    public void setIsLivenessRandom(boolean isLivenessRandom) {
        this.isLivenessRandom = isLivenessRandom;
    }

    public List<Integer> getIivenessAction() {
        return iivenessAction;
    }

    public void setIivenessAction(List<Integer> iivenessAction) {
        this.iivenessAction = iivenessAction;
    }
}
