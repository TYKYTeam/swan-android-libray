package com.vachel.editor;

import android.graphics.Bitmap;

public interface IEditSave {
    void onSaveSuccess(Bitmap bitmap);

    void onSaveFailed();
}
