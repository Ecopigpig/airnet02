package model.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    private static final long serialVersionUID = 6840673675954893215L;
    private int page;
    private int size;
}
