package com.pixelplex.qtum.datastorage;


public class SharedTemplate {

    private String uuid;
    private String templateName;

    public SharedTemplate(String uuid, String templateName){
        this.uuid = uuid;
        this.templateName = templateName;
    }

    public String getUuid(){
        return uuid;
    }

    public String getTemplateName(){
        return templateName;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public void setTemplateName(String templateName){
        this.templateName = templateName;
    }

}
