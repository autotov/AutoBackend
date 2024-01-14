package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "autotovcardocs")
public class CarDocument {
    private Integer tenant;
    private Integer company;
    private Date creationDate;
    private Date lastUpdateDate;

    @Id
    private String docId;

    private String carLicense;

    private String uploadUserName;

    private String docName;

    private String docType;

    private String docPath;
}
