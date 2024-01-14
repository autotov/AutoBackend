package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "autotovalerts")
public class Alerts {
    private String tenant;
    private String company;
    private Date creationDate;
    private Date lastUpdateDate;

    @Id
    private String id;

    private long index;

    private String itemType;

    private List<Alert> alerts;
}
