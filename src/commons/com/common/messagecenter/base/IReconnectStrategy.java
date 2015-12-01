package com.common.messagecenter.base;

/**
 * @author yang-chen
 */
public interface IReconnectStrategy {
	public void beginReconnect();

	public void endReconnect();
	
	public boolean isInReconnect();
}
