/**
 * Project Name:liz-common-com.twinkle.cloud.common.asm.utils
 * File Name:ResultCode.java
 * Package Name:com.gemii.lizcloud.common.constant
 * Date:Jul 18, 20165:58:14 PM
 * Copyright (c) 2016, chenxj All Rights Reserved.
 *
*/

package com.twinkle.framework.api.constant;

/**
 * ClassName:ResultCode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jul 18, 2016 5:58:14 PM <br/>
 *
 * @author chenxj
 * @version
 * @since JDK 1.8
 * @see
 */
public interface ResultCode {
	/**
	 * OPERATION_SUCCESS: for all of the successful operations.
	 */
	String OPERATION_SUCCESS = "100";
	/**
	 * 11002001 = "11" || "002" || "001"
	 *
	 * "11" FOR COMPOSITE SERVICES "10" FOR CORE SERVICES "12" FOR BASEPLATFORM
	 * SERVICES "13" FOR COMMON PACKAGES
	 *
	 * "002" FOR SERVICES'S SEQ IN THEIR CATEGORY
	 *
	 * "001" FOR THE ERROR CODE FOR EACH EXCEPTION
	 *
	 */
	String OPERATION_FAILED_UNKOWN = "101";
	String ORG_USER_RETRIEVE_FAILED = "10002001";

	String PLATFORMACCESS_UNEXPECTED_EXCEPTION = "10006001";

    String PLATFORMACCESS_RPC_FAILED = "10006002";

    String PLATFORMACCESS_SYNC_FAILED = "10006003";
    
    String PLATFORMACCESS_QUOTA_VALIDATE_FAILED = "10006004";

	String USERMGMT_UNEXPECTED_EXCEPTION = "10001001";

	String USERMGMT_USER_NOT_FOUND = "10001002";
	
	String USERMGMT_ORG_EXISTED = "10001003";

	String USERMGMT_USER_EXISTED = "10001004";
}
