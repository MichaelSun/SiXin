package com.renren.mobile.chat.ui.groupchat;


import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;

/**
 * ContactTitleSelectHolder
 * @author xiaoguang.zhang
 * Date: 12-8-21
 * Time: 下午4:34
 * @说明 一个联系人title中切换的插件，实现基本方法以及样式
 */
public class D_ContactTitleSelectHolder {

    @ViewMapping(ID = R.id.contact_title_middle)
    public LinearLayout contactTitleMiddle;

    @ViewMapping(ID = R.id.title_select_left)
    public FrameLayout contactTitleLeftBtn;

    @ViewMapping(ID = R.id.title_select_right)
    public FrameLayout contactTitleRightBtn;

    @ViewMapping(ID = R.id.title_select_left_text)
    public TextView contactTitleLeftText;

    @ViewMapping(ID = R.id.title_select_right_text)
    public TextView contactTitleRightText;

    @ViewMapping(ID = R.id.title_select_middle_text)
    public TextView contactTitleMiddleText;

    @ViewMapping(ID = R.id.title_select_middle)
    public FrameLayout contactTitleMiddleBtn;

    private boolean leftBtnSelected = false;

    private boolean rightBtnSelected = false;

    private boolean middleBtnSelected = false;

    private boolean isRightBtnVisible = false;

    public void setRightBtnVisible() {
        contactTitleRightBtn.setVisibility(View.VISIBLE);
        contactTitleMiddleBtn.setBackgroundResource(R.drawable.contact_title_middle_selector);
        isRightBtnVisible = true;
    }

    public void setLeftBtnSelect() {
        contactTitleLeftBtn.setSelected(true);
        contactTitleLeftText.setSelected(true);
        leftBtnSelected = true;
        contactTitleMiddleBtn.setSelected(false);
        contactTitleMiddleText.setSelected(false);
        middleBtnSelected = false;
        if(isRightBtnVisible) {
            contactTitleRightBtn.setSelected(false);
            contactTitleRightText.setSelected(false);
            rightBtnSelected = false;
        }
    }

    public void setRightBtnSelect() {
        contactTitleRightBtn.setSelected(true);
        contactTitleRightText.setSelected(true);
        rightBtnSelected = true;
        contactTitleMiddleBtn.setSelected(false);
        contactTitleMiddleText.setSelected(false);
        middleBtnSelected = false;
        contactTitleLeftBtn.setSelected(false);
        contactTitleLeftText.setSelected(false);
        leftBtnSelected = false;
    }

    public void setMiddleBtnSelect() {
        contactTitleMiddleBtn.setSelected(true);
        contactTitleMiddleText.setSelected(true);
        middleBtnSelected = true;
        contactTitleLeftBtn.setSelected(false);
        contactTitleLeftText.setSelected(false);
        leftBtnSelected = false;
        if(isRightBtnVisible) {
            contactTitleRightBtn.setSelected(false);
            contactTitleRightText.setSelected(false);
            rightBtnSelected = false;
        }
    }

    public boolean isLeftBtnSelected() {
        return leftBtnSelected;
    }

    public boolean isRightBtnSelected() {
        return rightBtnSelected;
    }

    public boolean isMiddleBtnSelected() {
        return middleBtnSelected;
    }
}
