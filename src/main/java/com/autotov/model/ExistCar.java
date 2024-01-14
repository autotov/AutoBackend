package com.autotov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document(indexName = "existcars")
public class ExistCar extends GeneralModel {

    @Field(name = "mispar_rechev")
    private Long misparRechev;

    @Field(name = "tozeret_cd")
    private Long tozeretCd;

    @Field(name = "sug_degem")
    private String sugDegem;

    //tozeret_nm
    @Field(name = "tozeret_nm")
    private Long tozeretNm;

    //degem_cd
    @Field(name = "degem_cd")
    private Long degemCd;

    //degem_nm
    @Field(name = "degem_nm")
    private String degemNm;

    //ramat_gimur
    @Field(name = "ramat_gimur")
    private String ramatGimur;

    //ramat_eivzur_betihuty
    @Field(name = "ramat_eivzur_betihuty")
    private String ramatEivzurBetihuty;

    //kvutzat_zihum
    @Field(name = "kvutzat_zihum")
    private Long kvutzatZihum;

    //shnat_yitzur
    @Field(name = "shnat_yitzur")
    private Long shnatYitzur;

    //degem_manoa
    @Field(name = "degem_manoa")
    private String degemManoa;

    //mivchan_acharon_dt
    @Field(name = "mivchan_acharon_dt")
    private Date mivchanAcharonDt;

    //tokef_dt
    @Field(name = "tokef_dt")
    private Date tokefDt;

    //baalut
    @Field(name = "baalut")
    private String baalut;

    //misgeret
    @Field(name = "misgeret")
    private String misgeret;

    //tzeva_cd
    @Field(name = "tzeva_cd")
    private Long tzevaCd;

    //tzeva_rechev
    @Field(name = "tzevaRechev")
    private String tzevaRechev;

    //zmig_kidmi
    @Field(name = "zmigKidmi")
    private String zmigKidmi;

    //zmig_ahori
    @Field(name = "zmig_ahori")
    private String zmigAhori;

    //sug_delek_nm
    @Field(name = "sug_delek_nm")
    private String sugDelekNm;

    //horaat_rishum
    @Field(name = "horaat_rishum")
    private Long horaatRishum;

    //moed_aliya_lakvish
    @Field(name = "moed_aliya_lakvish")
    private String moedAliyaLakvish;

    //kinuy_mishari
    @Field(name = "kinuy_mishari")
    private String kinuyMishari;
}
