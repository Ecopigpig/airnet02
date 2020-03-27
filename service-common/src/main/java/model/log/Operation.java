package model.log;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Operation implements Serializable {

    private static final long serialVersionUID = -3749194215895381193L;

    private String id;
    private Long userId;//用户id 操作人ID
    private String userName;//用户名称 关联admin_user
    private String loginIp;//登录ip
    private int type;//操作类型(0登录、1查询、2修改)
    private String url ; // 操作的url
    private String operation;//操作内容
    private Date createTime;//操作时间
    private String remark;//备注
}
