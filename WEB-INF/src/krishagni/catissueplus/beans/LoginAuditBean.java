
package krishagni.catissueplus.beans;

import java.sql.Timestamp;

public class LoginAuditBean
{
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String userName;
	private String ipAddress;
	private Timestamp loginTimeStamp;
	private String lastLoginState;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Timestamp getLoginTimeStamp() {
		return loginTimeStamp;
	}
	public void setLoginTimeStamp(Timestamp loginTimeStamp) {
		this.loginTimeStamp = loginTimeStamp;
	}
	public String getLastLoginState() {
		return lastLoginState;
	}
	public void setLastLoginState(String lastLoginState) {
		this.lastLoginState = lastLoginState;
	}
	
	

}
