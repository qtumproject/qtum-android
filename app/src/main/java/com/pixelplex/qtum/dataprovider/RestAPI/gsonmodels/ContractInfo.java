package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels;

/**
 * Created by maksimromanovskij on 03.06.17.
 */

public class ContractInfo {

    private String name;
    private String templateName;
    private Boolean hasBeenCreated;

    public ContractInfo(String name, String templateName, Boolean hasBeenCreated){
        this.name = name;
        this.templateName = templateName;
        this.hasBeenCreated = hasBeenCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Boolean isHasBeenCreated() {
        return hasBeenCreated;
    }

    public void setHasBeenCreated(Boolean hasBeenCreated) {
        this.hasBeenCreated = hasBeenCreated;
    }
}
