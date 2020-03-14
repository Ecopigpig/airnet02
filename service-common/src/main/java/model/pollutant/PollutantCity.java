package model.pollutant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(value = "城市污染情况")
public class PollutantCity implements Serializable {

    private static final long serialVersionUID = -4000833675419779099L;

    @ApiModelProperty(value = "城市污染排名")
    private String num;

    @ApiModelProperty(value = "基本污染指数模型")
    private PollutionEpisode pollutionEpisode;

    @ApiModelProperty(value = "城市污染监测点污染情况list")
    private List<PollutionSite> pollutionSiteList;

}
