package com.zsc.servicedata.entity.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessagePageParam implements Serializable {
    private static final long serialVersionUID = 4965609690079559501L;
    private int page;
    private int size;
    private Long userId;
}
