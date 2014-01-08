<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="1px"></td>
        <td style="left-padding:5px">
            <img src="images/uIEnhancementImages/caTissue_logo_wo.png" />
            
        </td>
        <td>
        <logic:notEmpty scope="session" name="lastLoginTimeStamp">
        <span class="black_ar" style="text-align:center">Last activity on ${lastLoginTimeStamp} was ${lastLoginAttempt}</span>
        </logic:notEmpty>
        </td>
            
    </tr>
</table>
