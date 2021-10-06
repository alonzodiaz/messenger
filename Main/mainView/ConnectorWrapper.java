package main.mainView;

import common.IChatAppConnector;

/**
 * @author lonzd
 *
 */
public class ConnectorWrapper {
	/**
	 * The name of the IChatAppConnector
	 */
	String name;
	
	/**
	 * The IChatAppConnector object
	 */
	IChatAppConnector connector;
	
	/**
	 * @param _connector : The IChatAppConnector object
	 * @param _name : The name of the IChatAppConnector
	 */
	public ConnectorWrapper(IChatAppConnector _connector, String _name) {
		this.name = _name;
		this.connector = _connector;
	}
	
	@Override
    public String toString()
    {
        return name;
    }
	
	/**
	 * @return The IChatAppConnector
	 */
	public IChatAppConnector getConnector() {
		return this.connector;
	}
}
