package com.zsc.servicehi.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    private static final long serialVersionUID = 4965609690079559501L;
    private int page;
    private int size;
}
