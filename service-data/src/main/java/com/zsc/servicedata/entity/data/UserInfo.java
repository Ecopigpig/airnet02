package com.zsc.servicedata.entity.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "用户信息数据模型")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 3680521484767029494L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "角色(0-普通用户;1-管理员)")
    private Integer role;

    @ApiModelProperty(value = "是否允许申请API(0-不允许;1-允许)")
    private Integer auth;

    @ApiModelProperty(value = "API-token")
    private String token;

}
