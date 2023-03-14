package com.scu.ds.dfs.dfscoordinator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    @ApiModelProperty(example = "1234")
    private String id;

    @ApiModelProperty(example = "ani_kadam")
    private String username;

    @ApiModelProperty(example = "secret")
    private String password;

    @ApiModelProperty(example = "Anirudha Kadam")
    private String name;

    @ApiModelProperty(example = "2018-08-11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date memberSince;
}
