package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "autotovtenants")
public class Tenant extends GeneralModel {

    private String tenant;

    private Map<Integer, Company> companies = new HashMap() {
    };
}
