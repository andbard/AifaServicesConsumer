package com.andreabardella.aifaservicesconsumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugDto {

    public static final String ID = "id";
    public static final String SITE = "site";
    public static final String HASH = "hash";
    public static final String ENTITY_ID = "entity_id";
    public static final String ENTITY_TYPE = "entity_type";
    public static final String BUNDLE = "bundle";
    public static final String BUNDLE_NAME = "bundle_name";
    public static final String SS_LANGUAGE = "ss_language";
    public static final String PATH = "path";
    public static final String URL = "url";
    public static final String LABEL = "label";
    public static final String SPELL = "spell";
    public static final String CONTENT = "content";
    public static final String TEASER = "teaser";
    public static final String SS_NAME = "ss_name";
    public static final String SS_NAME_FORMATTED = "ss_name_formatted";
    public static final String TOS_NAME_FORMATTED = "tos_name_formatted";
    public static final String IS_UID = "is_uid";
    public static final String BS_STATUS = "bs_status";
    public static final String BS_STICKY = "bs_sticky";
    public static final String BS_PROMOTE = "bs_promote";
    public static final String IS_TNID = "is_tnid";
    public static final String BS_TRANSLATE = "bs_translate";
    public static final String DS_CREATED = "ds_created";
    public static final String DS_CHANGED = "ds_changed";
    public static final String DS_LAST_COMMENT_OR_CHANGE = "ds_last_comment_or_change";
    public static final String SM_FIELD_CODICE_DITTA = "sm_field_codice_ditta";
    public static final String SM_FIELD_DESCRIZIONE_DITTA = "sm_field_descrizione_ditta";
    public static final String SM_FIELD_CODICE_FARMACO = "sm_field_codice_farmaco";
    public static final String SM_FIELD_DESCRIZIONE_FARMACO = "sm_field_descrizione_farmaco";
    public static final String SM_FIELD_CODICE_CONFEZIONE = "sm_field_codice_confezione";
    public static final String SM_FIELD_DESCRIZIONE_CONFEZIONE = "sm_field_descrizione_confezione";
    public static final String SM_FIELD_STATO_FARMACO = "sm_field_stato_farmaco";
    public static final String SM_FIELD_CODICE_ATC = "sm_field_codice_atc";
    public static final String SM_FIELD_DESCRIZIONE_ATC = "sm_field_descrizione_atc";
    public static final String SM_FIELD_TIPO_PROCEDURA = "sm_field_tipo_procedura";
    public static final String DM_FIELD_UPLOAD_DATE = "dm_field_upload_date";
    public static final String DM_FIELD_LAST_UPDATE = "dm_field_last_update";
    public static final String SM_FIELD_LINK_FI = "sm_field_link_fi";
    public static final String SM_FIELD_LINK_RCP = "sm_field_link_rcp";
    public static final String SM_FIELD_CHIAVE_CONFEZIONE = "sm_field_chiave_confezione";
    public static final String SM_FIELD_AIC = "sm_field_aic";
    public static final String TIMESTAMP = "timestamp";

    @JsonProperty(ID)
    public String id;

    @JsonProperty(SITE)
    public String site;

    @JsonProperty(HASH)
    public String hash;

    @JsonProperty(ENTITY_ID)
    public Integer entityId;

    @JsonProperty(ENTITY_TYPE)
    public String entityType;

    @JsonProperty(BUNDLE)
    public String bundle;

    @JsonProperty(BUNDLE_NAME)
    public String bundleName;

    @JsonProperty(SS_LANGUAGE)
    public String ssLanguage;

    @JsonProperty(PATH)
    public String path;

    @JsonProperty(URL)
    public String url;

    @JsonProperty(LABEL)
    public String label;

    @JsonProperty(SPELL)
    public List<String> spellList;

    @JsonProperty(CONTENT)
    public String content;

    @JsonProperty(TEASER)
    public String teaser;

    @JsonProperty(SS_NAME)
    public String ssName;

    @JsonProperty(SS_NAME_FORMATTED)
    public String ssNameFormatted;

    @JsonProperty(TOS_NAME_FORMATTED)
    public String tosNameFormatted;

    @JsonProperty(IS_UID)
    public Integer isUid;

    @JsonProperty(BS_STATUS)
    public Boolean bsStatus;

    @JsonProperty(BS_STICKY)
    public Boolean bsSticky;

    @JsonProperty(BS_PROMOTE)
    public Boolean bsPromote;

    @JsonProperty(IS_TNID)
    public Integer isTnid;

    @JsonProperty(BS_TRANSLATE)
    public Boolean bsTranslate;

    //    @JsonProperty("ds_created")
//    String dsCreated; // "2014-07-08T11:53:50Z" // com.fasterxml.jackson.databind.util.ISO8601DateFormat
    @JsonProperty(DS_CREATED)
    public Date dsCreated; // "2014-07-08T11:53:50Z" // com.fasterxml.jackson.databind.util.ISO8601DateFormat

    //    @JsonProperty("ds_changed")
//    String dsChanged;
    @JsonProperty(DS_CHANGED)
    public Date dsChanged;

    //    @JsonProperty("ds_last_comment_or_change")
//    String dsLastCommentOrChange; // "2014-07-08T16:49:23Z"
    @JsonProperty(DS_LAST_COMMENT_OR_CHANGE)
    public Date dsLastCommentOrChange; // "2014-07-08T16:49:23Z"

    @JsonProperty(SM_FIELD_CODICE_DITTA)
    public List<String> industryCodeList; // ["2999"]

    @JsonProperty(SM_FIELD_DESCRIZIONE_DITTA)
    public List<String> industryDescriptionList; // ["ACTAVIS GROUP PTC EHF"]

    @JsonProperty(SM_FIELD_CODICE_FARMACO)
    public List<String> drugCodeList; // ["028510"]

    @JsonProperty(SM_FIELD_DESCRIZIONE_FARMACO)
    public List<String> drugDescriptionList; // ["NIMESULIDE ACTAVIS"]

    @JsonProperty(SM_FIELD_CODICE_CONFEZIONE)
    public List<String> bundleCodeList; // ["028"]

    @JsonProperty(SM_FIELD_DESCRIZIONE_CONFEZIONE)
    public List<String> bundleDescriptionList; // ["&quot;100 MG GRANULATO PER SOLUZIONE ORALE&quot;30 BUSTINE"]

    @JsonProperty(SM_FIELD_STATO_FARMACO)
    public List<String> drugStateList; // ["R"]

    @JsonProperty(SM_FIELD_CODICE_ATC)
    public List<String> atcCodeList; // ["M01AX17"]

    @JsonProperty(SM_FIELD_DESCRIZIONE_ATC)
    public List<String> atcDescriptionList; // ["Nimesulide"]

    @JsonProperty(SM_FIELD_TIPO_PROCEDURA)
    public List<String> procedureTypeList; // ["N"]

//    @JsonProperty("dm_field_upload_date")
//    List<String> uploadDateList; // ["2014-03-27T00:00:00Z"]
    @JsonProperty(DM_FIELD_UPLOAD_DATE)
    public List<Date> uploadDateList; // ["2014-03-27T00:00:00Z"]

//    @JsonProperty("dm_field_last_update")
//    List<String> lastUpdateList; // ["2014-03-27T00:00:00Z"]
    @JsonProperty(DM_FIELD_LAST_UPDATE)
    public List<Date> lastUpdateList; // ["2014-03-27T00:00:00Z"]

    @JsonProperty(SM_FIELD_LINK_FI) // Foglio Illustrativo
    public List<String> linkFiList; // ["https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet?pdfFileName=footer_002999_028510_FI.pdf&amp;retry=0&amp;sys=m0b1l3"]

    @JsonProperty(SM_FIELD_LINK_RCP) // Riassunto delle Caratteristiche del Prodotto
    public List<String> linkRcpList; // ["https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet?pdfFileName=footer_002999_028510_RCP.pdf&amp;retry=0&amp;sys=m0b1l3"]

    @JsonProperty(SM_FIELD_CHIAVE_CONFEZIONE)
    public List<String> bundleKeyList; // ["028510028"]

    @JsonProperty(SM_FIELD_AIC)
    public List<String> aicList; // ["028510028"]

    //    @JsonProperty("timestamp")
//    String timestamp; // "2016-11-08T08:57:49.279Z"
    @JsonProperty(TIMESTAMP)
    public Date timestamp; // "2016-11-08T08:57:49.279Z"

}
