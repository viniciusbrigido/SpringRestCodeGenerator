package com.brigido.springrestcodegenerator.dto;

import static java.util.Objects.requireNonNullElse;

public class PropertyDTO {

    private String urlProject;
    private String pathClass;
    private String packageEntity;
    private String packageController;
    private boolean useLombok;
    private boolean useSerializable;

    private String repositorySuffix;
    private String repositoryPath;

    private String serviceSuffix;
    private String serviceImplSuffix;

    private String persistDTOSuffix;
    private String updateDTOSuffix;

    private String servicePath;
    private String serviceImplPath;

    private String persistDTOPath;
    private String updateDTOPath;

    private String responseDTOPath;
    private String responseDTOSuffix;

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

    public String getRepositorySuffix() {
        return requireNonNullElse(repositorySuffix, "Repository");
    }

    public void setRepositorySuffix(String repositorySuffix) {
        this.repositorySuffix = repositorySuffix;
    }

    public String getServiceSuffix() {
        return requireNonNullElse(serviceSuffix, "Service");
    }

    public void setServiceSuffix(String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    public String getServiceImplSuffix() {
        return requireNonNullElse(serviceImplSuffix, "ServiceImpl");
    }

    public void setServiceImplSuffix(String serviceImplSuffix) {
        this.serviceImplSuffix = serviceImplSuffix;
    }

    public String getPersistDTOSuffix() {
        return requireNonNullElse(persistDTOSuffix, "PersistDTO");
    }

    public void setPersistDTOSuffix(String persistDTOSuffix) {
        this.persistDTOSuffix = persistDTOSuffix;
    }

    public String getUpdateDTOSuffix() {
        return requireNonNullElse(updateDTOSuffix, "UpdateDTO");
    }

    public void setUpdateDTOSuffix(String updateDTOSuffix) {
        this.updateDTOSuffix = updateDTOSuffix;
    }

    public String getResponseDTOSuffix() {
        return requireNonNullElse(responseDTOSuffix, "ResponseDTO");
    }

    public void setResponseDTOSuffix(String responseDTOSuffix) {
        this.responseDTOSuffix = responseDTOSuffix;
    }


    public String getRepositoryPath() {
        return requireNonNullElse(repositoryPath, "repository");
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public String getServicePath() {
        return requireNonNullElse(servicePath, "service");
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getServiceImplPath() {
        return requireNonNullElse(serviceImplPath, "service/impl");
    }

    public void setServiceImplPath(String serviceImplPath) {
        this.serviceImplPath = serviceImplPath;
    }

    public String getPersistDTOPath() {
        return requireNonNullElse(persistDTOPath, "dto");
    }

    public void setPersistDTOPath(String persistDTOPath) {
        this.persistDTOPath = persistDTOPath;
    }

    public String getUpdateDTOPath() {
        return requireNonNullElse(updateDTOPath, "dto");
    }

    public void setUpdateDTOPath(String updateDTOPath) {
        this.updateDTOPath = updateDTOPath;
    }

    public String getResponseDTOPath() {
        return requireNonNullElse(responseDTOPath, "dto");
    }

    public void setResponseDTOPath(String responseDTOPath) {
        this.responseDTOPath = responseDTOPath;
    }
}
