package com.brigido.springrestcodegenerator.dto;

import static java.util.Objects.requireNonNullElse;

public class PropertyDTO {

    private String urlProject;
    private String pathClass;
    private boolean useLombok;
    private boolean useSerializable;

    private String controllerSuffix;
    private String controllerPath;

    private String entitySuffix;
    private String entityPath;

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


    private String functionFindById;
    private String endpointFindById;

    private String functionCreate;
    private String endpointCreate;

    private String functionDelete;
    private String endpointDelete;

    private String functionUpdate;
    private String endpointUpdate;

    private String functionFindAll;
    private String endpointFindAll;

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

    public String getControllerSuffix() {
        return requireNonNullElse(controllerSuffix, "Controller");
    }

    public void setControllerSuffix(String controllerSuffix) {
        this.controllerSuffix = controllerSuffix;
    }

    public String getEntitySuffix() {
        return requireNonNullElse(entitySuffix, "");
    }

    public void setEntitySuffix(String entitySuffix) {
        this.entitySuffix = entitySuffix;
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

    public String getControllerPath() {
        return requireNonNullElse(controllerPath, "controller");
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public String getEntityPath() {
        return requireNonNullElse(entityPath, "entity");
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public String getEnumerationPath() {
        return "enumeration";
    }

    public String getFunctionFindById() {
        return requireNonNullElse(functionFindById, "findById");
    }

    public void setFunctionFindById(String functionFindById) {
        this.functionFindById = functionFindById;
    }

    public String getFunctionCreate() {
        return requireNonNullElse(functionCreate, "create");
    }

    public void setFunctionCreate(String functionCreate) {
        this.functionCreate = functionCreate;
    }

    public String getFunctionDelete() {
        return requireNonNullElse(functionDelete, "delete");
    }

    public void setFunctionDelete(String functionDelete) {
        this.functionDelete = functionDelete;
    }

    public String getFunctionUpdate() {
        return requireNonNullElse(functionUpdate, "update");
    }

    public void setFunctionUpdate(String functionUpdate) {
        this.functionUpdate = functionUpdate;
    }

    public String getFunctionFindAll() {
        return requireNonNullElse(functionFindAll, "findAll");
    }

    public void setFunctionFindAll(String functionFindAll) {
        this.functionFindAll = functionFindAll;
    }

    public String getEndpointFindById() {
        return requireNonNullElse(endpointFindById, "");
    }

    public void setEndpointFindById(String endpointFindById) {
        this.endpointFindById = endpointFindById;
    }

    public String getEndpointCreate() {
        return requireNonNullElse(endpointCreate, "");
    }

    public void setEndpointCreate(String endpointCreate) {
        this.endpointCreate = endpointCreate;
    }

    public String getEndpointDelete() {
        return requireNonNullElse(endpointDelete, "");
    }

    public void setEndpointDelete(String endpointDelete) {
        this.endpointDelete = endpointDelete;
    }

    public String getEndpointUpdate() {
        return requireNonNullElse(endpointUpdate, "");
    }

    public void setEndpointUpdate(String endpointUpdate) {
        this.endpointUpdate = endpointUpdate;
    }

    public String getEndpointFindAll() {
        return requireNonNullElse(endpointFindAll, "");
    }

    public void setEndpointFindAll(String endpointFindAll) {
        this.endpointFindAll = endpointFindAll;
    }
}
