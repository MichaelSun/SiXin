package com.sixin.widgets.fakechrome;

public interface IFakeChrome {
    void switchMode();

    void setAdapter(FakeChromeAdapter adapter);

    FakeChromeAdapter getAdapter();

    int getCurrentIndex();
}
