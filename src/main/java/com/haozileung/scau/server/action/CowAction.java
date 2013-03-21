/**
 * 
 * <b>项目名：</b>CowHealth<br />
 * <b>包名：</b>com.haozileung.scau.server.action<br />
 * <b>文件名：</b>CowAction.java<br />
 * <b>文件描述：</b>TODO<br />
 * <b>创建人：</b>lianghaopeng<br />
 * <b>修改人：</b>lianghaopeng<br />
 * <b>修改时间：</b>2013-1-30 下午2:09:37<br />
 * <b>修改备注：</b><br />
 * 
 * @version 1.0.0
 * 
 */
package com.haozileung.scau.server.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ExceptionMappings;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.haozileung.scau.server.common.action.BaseAction;
import com.haozileung.scau.server.service.ICowService;

/**
 * 
 * <b>类名称：</b>CowAction<br/>
 * <b>类描述：</b>TODO<br/>
 * <b>创建人：</b>lianghaopeng<br/>
 * <b>修改人：</b>lianghaopeng<br/>
 * <b>修改时间：</b>2013-1-30 下午2:09:37<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0
 * 
 */
@ParentPackage("json-default")
@Namespace("/cow")
@Results({ @Result(name = "success", location = "/index.jsp"),
		@Result(name = "error", location = "/error.html") })
@ExceptionMappings({ @ExceptionMapping(exception = "java.lange.RuntimeException", result = "error") })
public class CowAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8059333964126602031L;

	@Autowired
	private ICowService cowService;

	private String cowId;

	private String name;

	private int age;

	private String sex;

	@Action(value = "getCow", results = { @Result(name = SUCCESS, type = "json", params = {
			"includeProperties",
			"response\\.\\w+,response\\.data\\[\\d+\\]\\.\\w+",
			"ignoreHierarchy", "false", "excludeNullProperties", "true" }) })
	public String getCowInfoList() {
		return null;
	}

	@Action(value = "addCow", results = { @Result(name = SUCCESS, type = "json", params = {
			"includeProperties",
			"response\\.\\w+,response\\.data\\[\\d+\\]\\.\\w+",
			"ignoreHierarchy", "false", "excludeNullProperties", "true" }) })
	public String addCowInfo() {
		return null;
	}

	@Action(value = "updateCow", results = { @Result(name = SUCCESS, type = "json", params = {
			"includeProperties",
			"response\\.\\w+,response\\.data\\[\\d+\\]\\.\\w+",
			"ignoreHierarchy", "false", "excludeNullProperties", "true" }) })
	public String updateCowInfo() {
		return null;
	}

	@Action(value = "deleteCow", results = { @Result(name = SUCCESS, type = "json", params = {
			"includeProperties",
			"response\\.\\w+,response\\.data\\[\\d+\\]\\.\\w+",
			"ignoreHierarchy", "false", "excludeNullProperties", "true" }) })
	public String deleteCowInfo() {
		return null;
	}

	public String getCowId() {
		return cowId;
	}

	public void setCowId(String cowId) {
		this.cowId = cowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
