package model.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult implements Serializable {

    private Boolean msg;

    private Object data;

    private Long total;

}
