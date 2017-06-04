package com.andreabardella.aifaservicesconsumer.dto.mapper;

import com.andreabardella.aifaservicesconsumer.dto.DrugDto;
import com.andreabardella.aifaservicesconsumer.model.Drug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DrugDtoToDrugModelMapper {

    public static Drug map(DrugDto input) {
        Drug output = null;
        if (input != null) {
            output = new Drug();
            output.setId(input.id);
            output.setSite(input.site);
            output.setHash(input.hash);
            output.setEntityId(input.entityId);
            output.setEntityType(input.entityType);
            output.setBundle(input.bundle);
            output.setBundleName(input.bundleName);
            output.setSsLanguage(input.ssLanguage);
            output.setPath(input.path);
            output.setUrl(input.url);
            output.setLabel(input.label);
            output.setSpellList(input.spellList);
            output.setContent(input.content);
            output.setTeaser(input.teaser);
            output.setSsName(input.ssName);
            output.setSsNameFormatted(input.ssNameFormatted);
            output.setTosNameFormatted(input.tosNameFormatted);
            output.setIsUid(input.isUid);
            output.setBsStatus(input.bsStatus);
            output.setBsSticky(input.bsSticky);
            output.setBsPromote(input.bsPromote);
            output.setIsTnid(input.isTnid);
            output.setBsTranslate(input.bsTranslate);
            output.setDsCreated(input.dsCreated);
            output.setDsChanged(input.dsChanged);
            output.setDsLastCommentOrChange(input.dsLastCommentOrChange);
            output.setIndustryCodeList(input.industryCodeList);
            output.setIndustryDescriptionList(input.industryDescriptionList);
            output.setDrugCodeList(input.drugCodeList);
            output.setDrugDescriptionList(input.drugDescriptionList);
            output.setDrugStateList(input.drugStateList);
            output.setAtcCodeList(input.atcCodeList);
            output.setAtcDescriptionList(input.atcDescriptionList);
            output.setProcedureTypeList(input.procedureTypeList);
            output.setUploadDateList(input.uploadDateList);
            output.setLastUpdateList(input.lastUpdateList);
            output.setLinkFiList(input.linkFiList);
            output.setLinkRcpList(input.linkRcpList);
            output.setBundleKeyList(input.bundleKeyList);
            output.setAicList(input.aicList);
            output.setTimestamp(input.timestamp);
        }
        return output;
    }

    public static List<Drug> map(Collection<DrugDto> inputs) {
        List<Drug> outputs = new ArrayList<>();
        if (inputs != null && inputs.size() > 0) {
            for (DrugDto input : inputs) {
                Drug output = map(input);
                outputs.add(output);
            }
        }
        return outputs;
    }

}
