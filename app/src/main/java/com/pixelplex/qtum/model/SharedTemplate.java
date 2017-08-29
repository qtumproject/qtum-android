package com.pixelplex.qtum.model;


public class SharedTemplate {

    private String mUuid;
    private String mTemplateName;

    public SharedTemplate(String uuid, String templateName){
        this.mUuid = uuid;
        this.mTemplateName = templateName;
    }

    public String getUuid(){
        return mUuid;
    }

    public String getTemplateName(){
        return mTemplateName;
    }

    public void setUuid(String uuid){
        this.mUuid = uuid;
    }

    public void setTemplateName(String templateName){
        this.mTemplateName = templateName;
    }

}
