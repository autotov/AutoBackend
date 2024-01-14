package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralModel {

    @Id
    private Integer numberId;

    private Date creationDate;

    private Date lastUpdateDate;

    private String updateBy;
}
