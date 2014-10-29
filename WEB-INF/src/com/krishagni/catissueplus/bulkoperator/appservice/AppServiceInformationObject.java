/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.appservice;

public class AppServiceInformationObject
{
	private String userName;
	private String password;
	private String serviceImplementorClassName;

	public final String getUserName()
	{
		return userName;
	}

	public final void setUserName(String userName)
	{
		this.userName = userName;
	}

	public final String getPassword()
	{
		return password;
	}

	public final void setPassword(String password)
	{
		this.password = password;
	}

	public final String getServiceImplementorClassName()
	{
		return serviceImplementorClassName;
	}

	public final void setServiceImplementorClassName(String serviceImplementorClassName)
	{
		this.serviceImplementorClassName = serviceImplementorClassName;
	}

}
