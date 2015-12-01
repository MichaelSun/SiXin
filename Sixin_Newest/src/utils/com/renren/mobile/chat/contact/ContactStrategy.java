package com.renren.mobile.chat.contact;
/**
 * 获取联系人的策略 目的是为了整合逻辑 统一调用
 * @author zhenning.yang
 *
 */
public interface ContactStrategy {
    /**
     * 处理联系人
     */
    public void handleContacts();
}
