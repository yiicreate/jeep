package com.jeeplus.modules.flowable.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.flowable.entity.FlowModel;
import com.jeeplus.modules.flowable.service.FlowableModelService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.flowable.ui.common.service.exception.ConflictingRequestException;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelKeyRepresentation;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * 流程模型相关Controller
 * @author liugaofeng
 * @version 2018-07-29
 */
@RestController
@RequestMapping("/flowable/model")
public class FlowableModelController extends BaseController {

	@Autowired
	private FlowableModelService flowableModelService;
	@Autowired
	protected ModelService modelService;
	@Autowired
	protected ModelRepository modelRepository;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	private RepositoryService repositoryService;


	@GetMapping(value="account", produces = "application/json")
	public UserRepresentation account() {
		User user = UserUtils.getUser();
		UserEntityImpl fUser = new UserEntityImpl();

		fUser.setId(user.getId());
		fUser.setFirstName(user.getName());
		fUser.setLastName("");
		fUser.setEmail(user.getEmail());

		UserRepresentation userRepresentation = new UserRepresentation(fUser);

		return userRepresentation;
	}

	/**
	 * 流程模型列表
	 */
	@GetMapping("list")
	public AjaxJson data(String filter, HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model model) {
		Page<FlowModel> page = flowableModelService.getModels(new Page<FlowModel>(request, response),filter, "modifiedDesc", 0, request);
		return AjaxJson.success().put("page", page);
	}


	/**
	 * 导出model的xml文件
	 */
	@GetMapping("getBpmnXml")
	public String export(String id, HttpServletResponse response) {
		return flowableModelService.export(id, response);
	}

	/**
	 * 更新Model分类
	 */
	@PostMapping( value = "updateCategory")
	public AjaxJson updateCategory(String id, String category) {
		repositoryService.setProcessDefinitionCategory(id, category);
		return AjaxJson.success("设置成功，模块ID=" + id);
	}

	/**
	 * 删除Model
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping("delete")
	public AjaxJson deleteAll(String ids) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			flowableModelService.delete(id);
		}
        if(jeePlusProperites.isDemoMode()){
            return AjaxJson.error("演示模式，不允许操作!");
        }
		return AjaxJson.success("删除成功!");
	}
	/**
	 * 根据Model复制流程
	 */
	@PostMapping("copy")
	public AjaxJson copy(String id) throws Exception{

		org.flowable.ui.modeler.domain.Model sourceModel = modelService.getModel(id);

		ModelRepresentation modelRepresentation = new ModelRepresentation ();
		modelRepresentation.setKey ("Process_"+ UUID.randomUUID ());
		modelRepresentation.setName (sourceModel.getName ()+"_copy");
		modelRepresentation.setModelType (0);
		modelRepresentation.setDescription ("");
		modelRepresentation.setKey(modelRepresentation.getKey().replaceAll(" ", ""));
		this.checkForDuplicateKey(modelRepresentation);
		String json = modelService.createModelJson(modelRepresentation);
		User user = UserUtils.getUser();
		UserEntityImpl fUser = new UserEntityImpl();

		fUser.setId(user.getId());
		fUser.setFirstName(user.getName());
		fUser.setLastName("");
		fUser.setEmail(user.getEmail());

		org.flowable.ui.modeler.domain.Model newModel = modelService.createModel(modelRepresentation, json, fUser);
		String modelId = newModel.getId ();


		ObjectNode sourceObjectNode = (ObjectNode) objectMapper.readTree(sourceModel.getModelEditorJson());
		ObjectNode editorNode = sourceObjectNode.deepCopy();
		ObjectNode properties = objectMapper.createObjectNode();
		properties.put("process_id", newModel.getKey());
		properties.put("name", newModel.getName());
		editorNode.set("properties", properties);

		newModel.setModelEditorJson (editorNode.toString());


		modelService.saveModel(modelId, newModel.getName (), newModel.getKey (), newModel.getDescription (), newModel.getModelEditorJson (), true, "", fUser);

		return  AjaxJson.success("拷贝成功!");
	}

	/**
	 * 根据Model部署流程
	 */
	@PostMapping("deploy")
	public AjaxJson deploy(String id, String category) {
		String result = flowableModelService.deploy(id, category);
		return  AjaxJson.success(result);
	}



	@PostMapping(
			value = {"/rest/models"},
			produces = {"application/json"}
	)
	public ModelRepresentation createModel(@RequestBody ModelRepresentation modelRepresentation) {
		modelRepresentation.setKey(modelRepresentation.getKey().replaceAll(" ", ""));
		this.checkForDuplicateKey(modelRepresentation);
		String json = this.modelService.createModelJson(modelRepresentation);
		Model newModel = this.modelService.createModel(modelRepresentation, json, SecurityUtils.getCurrentUserObject());
		return new ModelRepresentation(newModel);
	}
	@PostMapping("saveModel/{modelId}")
	public ModelRepresentation saveModel(@PathVariable String modelId,  @RequestBody MultiValueMap<String, String> values) {
		long lastUpdated = -1L;
		String lastUpdatedString = (String) values.getFirst("lastUpdated");
		if (lastUpdatedString == null) {
			throw new BadRequestException("Missing lastUpdated date");
		} else {
			try {
				Date readValue = this.objectMapper.getDeserializationConfig().getDateFormat().parse(lastUpdatedString);
				lastUpdated = readValue.getTime();
			} catch (ParseException var12) {
				throw new BadRequestException("Invalid lastUpdated date: '" + lastUpdatedString + "'");
			}

			Model model = this.modelService.getModel(modelId);
			boolean currentUserIsOwner = model.getLastUpdatedBy().equals(UserUtils.getUser().getId());
			String resolveAction = (String) values.getFirst("conflictResolveAction");
			if (model.getLastUpdated().getTime() != lastUpdated) {
				String isNewVersionString;
				if ("saveAs".equals(resolveAction)) {
					isNewVersionString = (String) values.getFirst("saveAs");
					String json = (String) values.getFirst("json_xml");
					json = this.flowableModelService.changeXmlToJson(json);
					return this.createNewModel(isNewVersionString, model.getDescription(), model.getModelType(), json);
				} else if ("overwrite".equals(resolveAction)) {
					return this.updateModel(model, values, false);
				} else if ("newVersion".equals(resolveAction)) {
					return this.updateModel(model, values, true);
				} else {
					isNewVersionString = (String) values.getFirst("newversion");
					if (currentUserIsOwner && "true".equals(isNewVersionString)) {
						return this.updateModel(model, values, true);
					} else {
						ConflictingRequestException exception = new ConflictingRequestException("Process model was updated in the meantime");
						exception.addCustomData("userFullName", model.getLastUpdatedBy());
						exception.addCustomData("newVersionAllowed", currentUserIsOwner);
						throw exception;
					}
				}
			} else {
				return this.updateModel(model, values, false);
			}
		}
	   }

		protected ModelRepresentation updateModel(Model model, MultiValueMap<String, String> values, boolean forceNewVersion) {
			String name = (String)values.getFirst("name");
			String key = ((String)values.getFirst("key")).replaceAll(" ", "");
			String description = (String)values.getFirst("description");
			String isNewVersionString = (String)values.getFirst("newversion");
			String newVersionComment = null;
			ModelKeyRepresentation modelKeyInfo = this.modelService.validateModelKey(model, model.getModelType(), key);
			if (modelKeyInfo.isKeyAlreadyExists()) {
				throw new BadRequestException("Model with provided key already exists " + key);
			} else {
				boolean newVersion = false;
				if (forceNewVersion) {
					newVersion = true;
					newVersionComment = (String)values.getFirst("comment");
				} else if (isNewVersionString != null) {
					newVersion = "true".equals(isNewVersionString);
					newVersionComment = (String)values.getFirst("comment");
				}

				String json = (String)values.getFirst("json_xml");
				json = this.flowableModelService.changeXmlToJson(json);

				try {
					ObjectNode editorJsonNode = (ObjectNode)this.objectMapper.readTree(json);
					ObjectNode propertiesNode = (ObjectNode)editorJsonNode.get("properties");
					propertiesNode.put("process_id", key);
					propertiesNode.put("name", name);
					if (StringUtils.isNotEmpty(description)) {
						propertiesNode.put("documentation", description);
					}

					editorJsonNode.set("properties", propertiesNode);
					model = this.modelService.saveModel(model.getId(), name, key, description, editorJsonNode.toString(), newVersion, newVersionComment, SecurityUtils.getCurrentUserObject());
					return new ModelRepresentation(model);
				} catch (Exception var15) {
					throw new BadRequestException("Process model could not be saved " + model.getId());
				}
			}
		}


	protected void checkForDuplicateKey(ModelRepresentation modelRepresentation) {
		ModelKeyRepresentation modelKeyInfo = this.modelService.validateModelKey((Model)null, modelRepresentation.getModelType(), modelRepresentation.getKey());
		if (modelKeyInfo.isKeyAlreadyExists()) {
			throw new ConflictingRequestException("Provided model key already exists: " + modelRepresentation.getKey());
		}
	}


	protected ModelRepresentation createNewModel(String name, String description, Integer modelType, String editorJson) {
			ModelRepresentation model = new ModelRepresentation();
			model.setName(name);
			model.setDescription(description);
			model.setModelType(modelType);
			Model newModel = this.modelService.createModel(model, editorJson, SecurityUtils.getCurrentUserObject());
			return new ModelRepresentation(newModel);
		}
	}

