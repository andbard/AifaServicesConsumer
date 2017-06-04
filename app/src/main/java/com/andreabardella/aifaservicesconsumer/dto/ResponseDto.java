package com.andreabardella.aifaservicesconsumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {

    @JsonProperty("response")
    public Response response;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Response {

        @JsonProperty("numFound")
        public Integer numFound;

        @JsonProperty("start")
        public Integer start;

        @JsonProperty("docs")
        public List<DrugDto> drugList;

    }

}