package com.sixin.widgets.layout.toast;

public interface IToast {
    void show(long ms);

    void show();

    void hide();

    void setText(String text);

    String getText();

    void setOnVisibilityChangedListener(OnVisibilityChangedListener listener);

    interface OnVisibilityChangedListener {
        void onShown();

        void onHidden();
    }
}
