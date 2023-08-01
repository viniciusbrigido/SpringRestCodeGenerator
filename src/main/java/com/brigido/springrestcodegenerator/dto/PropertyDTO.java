package com.brigido.springrestcodegenerator.dto;

public class PropertyDTO {

    private String urlProject;
    private String pathClass;
    private String packageEntity;
    private String packageController;
    private boolean useLombok;
    private boolean useSerializable;

    public String getUrlProject() {
        return urlProject;
    }

    public void setUrlProject(String urlProject) {
        this.urlProject = urlProject;
    }

    public String getPathClass() {
        return pathClass;
    }

    public void setPathClass(String pathClass) {
        this.pathClass = pathClass;
    }

    public String getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(String packageEntity) {
        this.packageEntity = packageEntity;
    }

    public String getPackageController() {
        return packageController;
    }

    public void setPackageController(String packageController) {
        this.packageController = packageController;
    }

    public boolean isUseLombok() {
        return useLombok;
    }

    public void setUseLombok(boolean useLombok) {
        this.useLombok = useLombok;
    }

    public boolean isUseSerializable() {
        return useSerializable;
    }

    public void setUseSerializable(boolean useSerializable) {
        this.useSerializable = useSerializable;
    }

    public String getEntitySuffix() {
        return packageEntity.toLowerCase();
    }
}
