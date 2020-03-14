package model.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = -8888296389873331488L;

    private Boolean msg;

    private Object data;

    private Long total;

}
