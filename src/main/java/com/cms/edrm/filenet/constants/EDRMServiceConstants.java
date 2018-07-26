package com.cms.edrm.filenet.constants;

import java.util.ResourceBundle;

/**
 * 
 * Class for defining all the constants related to EDRM filenet services
 *
 */
public class EDRMServiceConstants 
{
	public static final String EDRM_RESOURCELOADER_PROPERTIESFILE = "com.cms.edrm.filenet.properties.filenetservice";
	public static ResourceBundle resourceBundle = ResourceBundle.getBundle(EDRM_RESOURCELOADER_PROPERTIESFILE);
	
	//Connection information
	public static final String SUBJECT_STANZANAME = resourceBundle.getString("edrm.filenet.subject.stanzaname");
	public static final String CONNECT_URL = resourceBundle.getString("edrm.filenet.connect.url");
	public static final String CONNECT_USER_NAME = resourceBundle.getString("edrm.filenet.connect.username");
	public static final String CONNECT_PASSWORD = resourceBundle.getString("edrm.filenet.connect.password");
	public static final String CONNECT_DOMAIN = resourceBundle.getString("edrm.filenet.connect.domainname");
	public static final String CONNECT_OBJECTSTORE = resourceBundle.getString("edrm.filenet.connect.objectstore");
	public static final String REQUEST_DETAILS = resourceBundle.getString("edrm.document.requestdetails");
	public static final String ACTIVE_DIRECTORY = resourceBundle.getString("edrm.filenet.connect.active.directory");
	
	//sql server connection details
	public static final String SQL_CONNECTION_CLASSNAME = resourceBundle.getString("edrm.sql.connection.classname");
	public static final String SQL_CONNECTION_STRING = resourceBundle.getString("edrm.sql.connection.string");
	public static final String SQL_DATABASE_NAME = resourceBundle.getString("edrm.sql.database.name");
	public static final String SQL_USERNAME = resourceBundle.getString("edrm.sql.username");
	public static final String SQL_PASSWORD = resourceBundle.getString("edrm.sql.password");
	public static final String SQL_MINUTESHEET_QUERY = resourceBundle.getString("edrm.sql.minute.sheet.query");
	
	//System Properties
	public static final String CREATOR = resourceBundle.getString("edrm.document.creator");
	public static final String DATE_CREATED = resourceBundle.getString("edrm.document.date.created");
	public static final String LAST_MODIFIER = resourceBundle.getString("edrm.document.last.modifier");
	public static final String DATE_LAST_MODIFIED = resourceBundle.getString("edrm.document.date.last.modified");
	public static final String CMS_DEMO_CLASS = resourceBundle.getString("edrm.document.class.cmsDemo");
	public static final String CMS_CHILD_CLASS = resourceBundle.getString("edrm.document.class.cmsChildClass");
	public static final String NAME = resourceBundle.getString("edrm.document.property.name");
	public static final String DOCUMENT_PROPERTY_SYMBOLIC_NAME = resourceBundle.getString("edrm.document.property.symbolicname");
	public static final String DOCUMENT_CLASS_DESCRIPTION = resourceBundle.getString("edrm.document.class.description");
	public static final String DOCUMENT_PROPERTY_TEMPLATE = resourceBundle.getString("edrm.document.property.template");
	public static final String DOCUMENT_CLASS_DEFINITION = resourceBundle.getString("edrm.document.class.definition");
	public static final String CLASS_NAME = resourceBundle.getString("edrm.class.name");
	public static final String CLASS_SYMBOLIC_NAME = resourceBundle.getString("edrm.class.symbolic.name");
	public static final String PROPERTY_DEFINITION_STRING_IMPL = resourceBundle.getString("edrm.property.definition.string.impl");
	public static final String PROPERTY_DEFINITION_INTEGER_IMPL = resourceBundle.getString("edrm.property.definition.integer.impl");
	public static final String PROPERTY_DEFINITION_BOOLEAN_IMPL = resourceBundle.getString("edrm.property.definition.boolean.impl");
	public static final String PROPERTY_DEFINITION_DATE_TIME_IMPL = resourceBundle.getString("edrm.property.definition.date.time.impl");
	
	//Rest Service Constants
	public static final String REST_JSON_FORM = resourceBundle.getString("edrm.rest.api.json.form");
	public static final String RECORD_DECLARE_REST_API = resourceBundle.getString("edrm.rest.api.declare.record.service");
	public static final String REST_DOCUMENT_ID = resourceBundle.getString("edrm.rest.api.documentId");
	public static final String FOLDER_UNIQUE_IDENTIFIER = resourceBundle.getString("edrm.rest.api.folder.unique.identifier");
	public static final String IS_VITAL = resourceBundle.getString("edrm.rest.api.is.vital");
	
	//Document information
	public static final String DOCUMENT_MESG = resourceBundle.getString("edrm.document");
	public static final String DOCUMENTS = resourceBundle.getString("edrm.documents");
	public static final String DOCUMENT_CLASS_CREATED = resourceBundle.getString("edrm.document.class.success");
	public static final String DOCUMENT_CLASS_CREATED_SECURITY_UPDATED = resourceBundle.getString("edrm.document.class.security.success");
	public static final String DOCUMENT_CLASS_CREATED_SECURITY_FAILED = resourceBundle.getString("edrm.document.class.security.failed");
	public static final String DOCUMENT_PROPERTIES = resourceBundle.getString("edrm.document.properties");
	public static final String DOCUMENT_FILEPATH = resourceBundle.getString("edrm.document.filepath");
	public static final String SECURITY = resourceBundle.getString("edrm.document.security");
	public static final String DOCUMENT_MULTI = resourceBundle.getString("edrm.document.multi");
	public static final String DOCUMENT_IS_RECORD = resourceBundle.getString("edrm.document.isrecord");
	public static final String DOCUMENT_CLASS = resourceBundle.getString("edrm.document.documentclass");
	public static final String DOCUMENT_SYMBOLIC_NAME = resourceBundle.getString("edrm.document.symbolic.name");
	public static final String DOCUMENT_DISPLAY_NAME = resourceBundle.getString("edrm.document.display.name");
	public static final String DOCUMENT_DISPLAY_TITLE = resourceBundle.getString("edrm.document.display.title");
	public static final String PUBLICATION_SOURCE = resourceBundle.getString("edrm.publication.source");
	public static final String DOCUMENT_FOLDER = resourceBundle.getString("edrm.document.folder");
	public static final String PROPERTY_STRING = resourceBundle.getString("edrm.document.property.string");
	public static final String PROPERTY_DATE_TIME = resourceBundle.getString("edrm.document.property.date");
	public static final String PROPERTY_DATE = resourceBundle.getString("edrm.property.date");
	public static final String PROPERTY_SINGLE = resourceBundle.getString("edrm.document.property.single");
	public static final String FOLDER = resourceBundle.getString("edrm.folder");
	public static final String FOLDERS = resourceBundle.getString("edrm.folders");
	public static final String DOCUMENT_OBJECT_CUSTOM_OBJECT = resourceBundle.getString("edrm.document.object.customobject");
	public static final String RECORD_OBJECT = resourceBundle.getString("edrm.document.object.record");
	public static final String RECORD_PROPERTIES = resourceBundle.getString("edrm.document.record.properties");
	public static final String DOCUMENT_OBJECT_ALLOW = resourceBundle.getString("edrm.document.object.allow");
	public static final String DOCUMENT_OBJECT_DENY = resourceBundle.getString("edrm.document.object.deny");
	public static final String DOCUMENT_OBJECT_PROPERTY_TYPE = resourceBundle.getString("edrm.document.object.propertyname");
	public static final String DOCUMENT_OBJECT_DATA_TYPE = resourceBundle.getString("edrm.document.object.datatype");
	public static final String OBJECT_VALUE = resourceBundle.getString("edrm.document.object.value");
	public static final String DOCUMENT_OBJECT_CARDINALITY = resourceBundle.getString("erdm.document.object.cardinality");
	public static final String DOCUMENT_OBJECT_MIME_TYPE = resourceBundle.getString("edrm.document.mimetype");	
	public static final String DOCUMENT_NO_MIME_TYPE = resourceBundle.getString("edrm.document.mime.metadata");
	public static final String MIMETYPE_INVALID_CODE = resourceBundle.getString("edrm.filenet.mime.code");
	public static final String MIMETYPE_INVALID_MESSAGE = resourceBundle.getString("edrm.filenet.mime.message");
	public static final String OBJECT_PRIVILEGE_TYPE = resourceBundle.getString("edrm.document.privilegetype");
	public static final String OBJECT_PRIVILEGE = resourceBundle.getString("edrm.document.privilege");
	public static final String OBJECT_USERNAME = resourceBundle.getString("edrm.document.userName");
	public static final String PROPERTY_FLOAT = resourceBundle.getString("edrm.document.property.float");
	public static final String PROPERTY_DOUBLE = resourceBundle.getString("edrm.document.property.double");
	public static final String PROPERTY_LONG = resourceBundle.getString("edrm.document.property.long");
	public static final String PROPERTY_BOOLEAN = resourceBundle.getString("edrm.document.property.boolean");
	public static final String PROPERTY_INTEGER = resourceBundle.getString("edrm.document.property.integer");
	public static final String DOCUMENT_COMMIT_SUCCESS = resourceBundle.getString("edrm.document.commit.success");
	public static final String DOCUMENT_COMMIT_FAIL = resourceBundle.getString("edrm.document.commit.failed");
	public static final String DOCUMENT_PARENT_CLASS = resourceBundle.getString("edrm.document.parent.class");
	public static final String FOLDER_CREATED_SUCCESS = resourceBundle.getString("edrm.folder.created.success");
	public static final String FOLDER_CREATED_FAIL = resourceBundle.getString("edrm.folder.created.fail");
	
	//Exception details
	public static final String ENGINE_RUNTIME_EXCEPTION = resourceBundle.getString("edrm.document.exception.engineruntimeexception");
	public static final String JSON_EXCEPTION = resourceBundle.getString("edrm.document.exception.jsonexception");
	public static final String GENERAL_EXCEPTION = resourceBundle.getString("edrm.document.exception.exception");
	public static final String PARSE_EXCEPTION = resourceBundle.getString("edrm.document.exception.parseexception");
	public static final String FILE_NOT_FOUND_EXCEPTION = resourceBundle.getString("edrm.document.exception.filenotfound");
	
	//Folder status message details
	public static final String FOLDER_FILE_SUCCESS = resourceBundle.getString("edrm.folder.file.success");
	public static final String FOLDER_FILE_FAILED = resourceBundle.getString("edrm.folder.file.failed");
	public static final String FOLDER_ID = resourceBundle.getString("edrm.folder.id");
	public static final String FOLDER_PATH_NAME = resourceBundle.getString("edrm.folder.path.name");
	
	//sql operations
	public static final String RECORD_SEARCH_CRITERIA_SQL = resourceBundle.getString("edrm.records.search.criteria.sql");
	public static final String UNIQUE_FOLDER_SEARCH_SQL = resourceBundle.getString("edrm.unique.folder.search.sql");
	public static final String SEARCH_CRITERIA_SQL_OPTIONS = resourceBundle.getString("edrm.records.search.criteria.sql.options");
	public static final String USER_SEARCH_CRITERIA_SQL = resourceBundle.getString("edrm.user.search.criteria.sql");
	
	//Record information
	public static final String RECORD_JSON_FAILED = resourceBundle.getString("edrm.record.json.failure");
	public static final String PROPERTY_BOOLEAN_TRUE = resourceBundle.getString("edrm.document.boolean.true");
	public static final String PROPERTY_BOOLEAN_FALSE = resourceBundle.getString("edrm.document.boolean.false");
	public static final String RECORD_TYPE = resourceBundle.getString("edrm.document.record.type");
	public static final String FLOWER_BRACE_OPEN_LITERAL = resourceBundle.getString("edrm.document.literal");
	public static final String FLOWER_BRACE_CLOSE_LITERAL = resourceBundle.getString("edrm.document.slash.literal");
	public static final String DESCRIPTION = resourceBundle.getString("edrm.record.description");
	public static final String TITLE = resourceBundle.getString("edrm.record.title");
	public static final String RECORD_SERIAL_NUMBER = resourceBundle.getString("edrm.record.serial.number");
	public static final String RECORD_ID = resourceBundle.getString("edrm.document.record.id");
	public static final String RECORD_RESPONSE = resourceBundle.getString("edrm.document.record.response");
	public static final String RELATED_ITEM_CLASS_NAME = resourceBundle.getString("edrm.record.related.items.classname");
	
	//document acl update
	public static final String DOCUMENT_ACL_SUCCESS = resourceBundle.getString("edrm.document.success.acl");
	public static final String DOCUMENT_ACL_FAILED = resourceBundle.getString("erdm.document.failure.acl");
	public static final String DOCUMENT_SUCCESSFULLY_DELETED = resourceBundle.getString("edrm.document.instance.deleted.success");
	public static final String DOCUMENT_DELETE_FAILED = resourceBundle.getString("edrm.document.instance.deleted.failed");
	
	//security
	public static final String SECURITY_FULL_CONTROL_MASK = resourceBundle.getString("edrm.security.fullControl.mask");
	public static final String SECURITY_CREATE_INSTANCE_MASK = resourceBundle.getString("edrm.security.createInstance.mask");
	public static final String SECURITY_VIEW_CONTENT_MASK = resourceBundle.getString("edrm.security.viewContent.mask");
	public static final String SECURITY_LINK_DOCUMENT_ANNOTATE_MASK = resourceBundle.getString("edrm.security.linkDocumentAnnotate.mask");
	public static final String SECURITY_DELETE_MASK = resourceBundle.getString("edrm.security.delete.mask");
	public static final String SECURITY_MODIFY_ALL_PROPERTIES_MASK = resourceBundle.getString("edrm.security.modifyAllProperties.mask");
	public static final String SECURITY_VIEW_PROPERTIES_MASK = resourceBundle.getString("edrm.security.viewProperties.mask");
	public static final String SECURITY_FULL_CONTROL = resourceBundle.getString("edrm.security.fullControl");
	public static final String SECURITY_CREATE_INSTANCE = resourceBundle.getString("edrm.security.createInstance");
	public static final String SECURITY_VIEW_CONTENT = resourceBundle.getString("edrm.security.viewContent");
	public static final String SECURITY_LINK_DOCUMENT_ANNOTATE = resourceBundle.getString("edrm.security.linkDocumentAnnotate");
	public static final String SECURITY_DELETE = resourceBundle.getString("edrm.security.delete");
	public static final String SECURITY_MODIFY_ALL_PROPERTIES = resourceBundle.getString("edrm.security.modifyAllProperties");
	public static final String SECURITY_VIEW_PROPERTIES = resourceBundle.getString("edrm.security.viewProperties");
	
	//status codes
	public static final String STATUS_CODE_NO_CONTENT = resourceBundle.getString("edrm.status.nocontent");
	public static final String SUCCESS_STATUS_CODE = resourceBundle.getString("edrm.status.success");
	public static final String INTERNAL_SERVER_ERROR_STATUS_CODE = resourceBundle.getString("edrm.status.internal.server.error");
	public static final String FILE_NOT_FOUND_STATUS_CODE = resourceBundle.getString("edrm.status.not.found");
	public static final String STATUS_CODE = resourceBundle.getString("edrm.status.code");
	public static final String OBJECT_ID = resourceBundle.getString("edrm.object.id");
	
	//messages information
	public static final String RESPONSE_CODE = resourceBundle.getString("edrm.response.code");
	public static final String RESPONSE = resourceBundle.getString("edrm.response");
	public static final String EXCEPTION = resourceBundle.getString("edrm.exception");
	public static final String RESPONSE_MESSAGE = resourceBundle.getString("edrm.response.message");
	public static final String EXCEPTION_CODE = resourceBundle.getString("edrm.exception.code");
	public static final String EXCEPTION_MESSAGE = resourceBundle.getString("edrm.exception.message");
	public static final String DOCUMENT_VS_ID = resourceBundle.getString("edrm.document.vs.id");
	public static final String CMS_RECORD_NAME = resourceBundle.getString("edrm.record.name");
	
	//document link resource bundle
	public static final String PARENT_DOCUMENT_VSID = resourceBundle.getString("edrm.parent.document.vsid");
	public static final String CHILD_DOCUMENT_LINK_DETAILS = resourceBundle.getString("edrm.child.link.details");
	public static final String CHILD_DOCUMENT_VSID = resourceBundle.getString("edrm.child.document.vsid");
	public static final String DOCUMENT_LINK_TYPE = resourceBundle.getString("edrm.document.link.type");
	public static final String DOCUMENT_LINK_NAME = resourceBundle.getString("edrm.document.link.name");
	public static final String DOCUMENT_LINKS = resourceBundle.getString("edrm.document.links");
	public static final String LINK_ID = resourceBundle.getString("edrm.document.link.id");
	public static final String DOCUMENT_DELINK_SUCCESS = resourceBundle.getString("edrm.document.de.link.created");
	public static final String DOCUMENT_DELINK_FAILED = resourceBundle.getString("edrm.document.de.link.failed");
	
	//Document search details
	public static final String DOCUMENT_SEARCH_CRITERIA = resourceBundle.getString("edrm.document.search.criteria");
	public static final String DOCUMENT_ID = resourceBundle.getString("edrm.document.documentid");
	public static final String DOCUMENT_NAME = resourceBundle.getString("edrm.document.documentname");
	public static final String DOCUMENT_TITLE = resourceBundle.getString("edrm.document.title");
	public static final String NO_DOCUMENT_CONTENT = resourceBundle.getString("edrm.document.no.content");
	public static final String DOCUMENT_RESPONSE = resourceBundle.getString("edrm.document.response");
	public static final String DOCUMENT_UPDATE_FAILED = resourceBundle.getString("edrm.document.update.failure");
	public static final String DOCUMENT_JSON_FAILED = resourceBundle.getString("edrm.document.json.failure");
	public static final String DOCUMENT_CONTENT_ADDED = resourceBundle.getString("edrm.document.content.added");
	public static final String DOCUMENT_PROPERTIES_UPDATED = resourceBundle.getString("edrm.document.properties.updated");
	public static final String DOCUMENT_SEARCH_OPERATION = resourceBundle.getString("edrm.document.search.operation");
	public static final String EQUAL_EXPRESSION = resourceBundle.getString("edrm.document.search.equal.expression");
	public static final String GREATER_THAN_EXPRESSION = resourceBundle.getString("edrm.document.search.greaterthan.expression");
	public static final String LESS_THAN_EXPRESSION = resourceBundle.getString("edrm.document.search.lessthan.expression");
	public static final String SEARCH_NO_RESULTS = resourceBundle.getString("edrm.document.search.noresults");
	public static final String AND_LITERAL = resourceBundle.getString("edrm.document.search.and.literal");
	public static final String HEAD_DOCUMENT_CRITERIA = resourceBundle.getString("edrm.head.document.criteria");
	public static final String TAIL_DOCUMENT_CRITERIA = resourceBundle.getString("edrm.tail.document.criteria");
	public static final String LINK_DETAILS = resourceBundle.getString("edrm.document.link.details");
	public static final String HEAD_DOCUMENT_CLASS = resourceBundle.getString("edrm.head.document");
	public static final String TAIL_DOCUMENT_CLASS = resourceBundle.getString("edrm.tail.document");
	public static final String HEAD_STATUS_ERROR = resourceBundle.getString("edrm.head.document.fail.msg");
	public static final String TAIL_STATUS_ERROR = resourceBundle.getString("edrm.tail.document.fail.msg");
	public static final String DOCUMENT_LINK_CREATED = resourceBundle.getString("edrm.document.link.created");
	public static final String DOCUMENT_LINK_FAILED = resourceBundle.getString("edrm.document.link.failed");
	public static final String DOCUMENT_LINKING_FAILED = resourceBundle.getString("edrm.document.linking.failed");
	public static final String DOCUMENT_NO_METADATA = resourceBundle.getString("edrm.document.metadata");
	public static final String NO_SEARCH_RESULTS_FOUND = resourceBundle.getString("edrm.document.no.search.results");
	public static final String NO_SEARCH_RESULTS_EXCEPTION_MESSAGE = resourceBundle.getString("edrm.no.search.result.exception.message");
	public static final String DOCUMENT_NO_METADATA_MSG = resourceBundle.getString("edrm.document.metadata.msg");
	public static final String VERIFY_LINK_DATA = resourceBundle.getString("edrm.verify.link.data");
	public static final String DOCUMENT_LINK_DESCRIPTION = resourceBundle.getString("edrm.document.link.description");
	public static final String DOCUMENT_STATUS_CODE = resourceBundle.getString("edrm.document.status.code");
	public static final String DOCUMENT_RECORD_COMMIT_SUCCESS = resourceBundle.getString("edrm.document.record.commit.success");
	public static final String DOCUMENT_UPDATED_SUCCESS = resourceBundle.getString("edrm.document.updated");
	public static final String DOCUMENT_CONTENT_ADD_FAILED = resourceBundle.getString("edrm.document.content.failed");
	public static final String PROPERTIES_UPDATE_FAILED = resourceBundle.getString("edrm.document.properties.failed");
	public static final String NO_TAIL_DOCUMENT_SEARCH_CRITERIA_RESULT = resourceBundle.getString("edrm.no.tail.document.search.criteria.result");
	public static final String NO_HEAD_DOCUMENT_SEARCH_CRITERIA_RESULT = resourceBundle.getString("edrm.no.head.document.search.criteria.result");
	public static final String NO_TAIL_DOCUMENT_SEARCH_CRITERIA = resourceBundle.getString("edrm.no.tail.document.search.criteria");
	public static final String NO_HEAD_DOCUMENT_SEARCH_CRITERIA = resourceBundle.getString("edrm.no.head.document.search.criteria");
	public static final String DOCUMENT_VERSION_FETCH_FAILED = resourceBundle.getString("edrm.document.version.fetch.failed");
	
	//folder details
	public static final String FOLDER_DETAILS = resourceBundle.getString("edrm.folder.details");
	public static final String FOLDERID = resourceBundle.getString("edrm.folder.json.id");
	public static final String FOLDER_NAME = resourceBundle.getString("edrm.folder.name");
	public static final String FOLDER_TYPE = resourceBundle.getString("edrm.folder.type");
	public static final String FOLDER_PROPERTIES = resourceBundle.getString("edrm.folder.properties");
	public static final String FOLDER_CREATION_SUCCESS = resourceBundle.getString("edrm.folder.create.success");
	public static final String FOLDER_DELETED_SUCCESS = resourceBundle.getString("edrm.folder.deleted.success");
	public static final String FOLDER_CREATION_FAILED = resourceBundle.getString("edrm.folder.create.failed");
	public static final String FOLDER_UPDATE_FAILED = resourceBundle.getString("edrm.folder.update.failed");
	public static final String FOLDER_DELETE_FAILED = resourceBundle.getString("edrm.folder.delete.failed");
	public static final String FOLDER_UPDATED_SUCCESS = resourceBundle.getString("edrm.folder.update.success");
	public static final String FOLDER_SEARCH_CRITERIA = resourceBundle.getString("edrm.folder.search.criteria");
	public static final String NO_SEARCH_CRITERIA = resourceBundle.getString("edrm.search.criteria.no.data");
	public static final String NO_PROPERTIES_UPDATE_DATA = resourceBundle.getString("edrm.properties.update.no.data");
	
	//marking set constants
	public static final String MARKING_SET = resourceBundle.getString("edrm.marking.set");
	public static final String MARKING_SETS = resourceBundle.getString("edrm.marking.sets");
	public static final String MARKING_SET_NAME = resourceBundle.getString("edrm.marking.set.name");
	public static final String MARKING_SET_SECURITY_MASK = resourceBundle.getString("edrm.marking.set.security.mask");
	public static final String MARKING_SET_PRIVILEGE_TYPE = resourceBundle.getString("edrm.marking.set.privilegetype");
	public static final String MARKING_SET_USERNAME = resourceBundle.getString("edrm.marking.set.userName");
	public static final String MARKING_SET_USERS = resourceBundle.getString("edrm.marking.set.users");
	public static final String MARKING_SET_MARKING_VALUES = resourceBundle.getString("edrm.marking.set.marking.values");
	public static final String MARKING_SET_MARKING_VALUE = resourceBundle.getString("edrm.marking.set.marking.value");
	public static final String MARKING_SET_PRIVILEGE = resourceBundle.getString("edrm.marking.set.privilege");
	public static final String MARKING_SET_CREATION_SUCCESS = resourceBundle.getString("edrm.marking.set.creation.successfully");
	public static final String MARKING_SET_CREATION_FAILED = resourceBundle.getString("edrm.marking.set.creation.failed");
	public static final String MARKING_SET_UPDATE_SUCCESS = resourceBundle.getString("edrm.marking.set.update.successfully");
	public static final String MARKING_SET_UPDATE_FAILED = resourceBundle.getString("edrm.marking.set.update.failed");
	public static final String MARKING_SET_SECURITY_LEVELS = resourceBundle.getString("edrm.marking.set.security.levels");
	public static final String ACCESS_MASK = resourceBundle.getString("edrm.access.mask");
	public static final String ACCESS_TYPE = resourceBundle.getString("edrm.access.type");
	public static final String USER = resourceBundle.getString("edrm.user");
	public static final String CONSTRAINT_MASK = resourceBundle.getString("edrm.constraint.mask");
	
	//master data constants
	public static final String MASTER_DATA = resourceBundle.getString("edrm.master.data");
	
	//minute sheet details
	public static final String MINUTE_SHEET = resourceBundle.getString("edrm.minute.sheet");
	public static final String MINUTE_SHEET_AUDIT = resourceBundle.getString("edrm.minute.sheet.audit");
	public static final String RM_AUDIT = resourceBundle.getString("edrm.rm.audit");
	public static final String EVENT_NAME = resourceBundle.getString("edrm.event.name");
	public static final String EVENT_STATUS = resourceBundle.getString("edrm.event.status");
	public static final String INITIATING_USER = resourceBundle.getString("edrm.initiating.user");
	public static final String EVENT_DATE = resourceBundle.getString("edrm.event.date");
	public static final String EVENTS = resourceBundle.getString("edrm.events");
	public static final String SIZE = resourceBundle.getString("edrm.size");
	public static final String RECORD_GUID = resourceBundle.getString("edrm.record.guid");
	public static final String RECORD_SERIAL_NO = resourceBundle.getString("edrm.record.serial.no");
	public static final String ACTION_TAKEN_BY = resourceBundle.getString("edrm.action.takenby");
	public static final String ACTION_DATE = resourceBundle.getString("edrm.action.date");
	public static final String ACTION_TYPE = resourceBundle.getString("edrm.action.type");
	public static final String REMARKS = resourceBundle.getString("edrm.remarks");
	public static final String WOB_PROCESS_ID = resourceBundle.getString("edrm.web.process.id");
	public static final String USER_ID = resourceBundle.getString("edrm.user.id");
	public static final String NEW_DESIGNATION = resourceBundle.getString("edrm.new.designation");
	
	//Annotations constant
	public static final String ANNOTATIONS = resourceBundle.getString("edrm.annotations"); 
	public static final String ANNOTATION = resourceBundle.getString("edrm.annotation");
	public static final String STICKY_NOTE = resourceBundle.getString("edrm.annotation.sticky.note");
	public static final String PROPERTIES = resourceBundle.getString("edrm.annotation.properties");
	public static final String CONTENT_ELEMENTS = resourceBundle.getString("edrm.content.elements");
	public static final String EDRM_LIST = resourceBundle.getString("edrm.list");
	
	//Sticky Note constant details
	public static final String STICKY_ENCODING = resourceBundle.getString("edrm.stickynote.encoding");
	public static final String STICKY_UNICODE = resourceBundle.getString("edrm.stickynote.unicode");
	public static final String STICKY_F_TEXT = resourceBundle.getString("edrm.stickynote.ftext");
	public static final String STICKY_PROP_DESC = resourceBundle.getString("edrm.stickynote.propdesc");
	public static final String STICKY_F_ANNO = resourceBundle.getString("edrm.stickynote.fnanno");
	public static final String STICKY_REPLACER = resourceBundle.getString("edrm.stickynote.decoding.replacer");
	
	public static final String CORRESPONDENCE_TYPES = resourceBundle.getString("edrm.correspondence.types");
	public static final String CORRESPONDENCE = resourceBundle.getString("edrm.correspondence");
	public static final String AUDIT_DATA = resourceBundle.getString("edrm.audit.data");
	public static final String SUCCESS = resourceBundle.getString("edrm.success");
	public static final String FAIL = resourceBundle.getString("edrm.fail");
	public static final String DESIGNATION = resourceBundle.getString("edrm.designation");
	
	public static final String TASKID = resourceBundle.getString("edrm.task.object.id");
	public static final String TASK_DELETE_FAILED = resourceBundle.getString("edrm.task.delete.failed");
	public static final String TASK_DELETED_SUCCESS = resourceBundle.getString("edrm.task.delete.success");
	
	public static final String FROM_FOLDER = resourceBundle.getString("edrm.folder.from.id");
	public static final String TO_FOLDER = resourceBundle.getString("edrm.folder.to.id");
	public static final String FOLDER_MOVED_SUCCESS = resourceBundle.getString("edrm.folder.moved.success");
	public static final String FOLDER_MOVED_FAILED = resourceBundle.getString("edrm.folder.moved.fail");
	
	// added by lavanya
	public static final String PROPERTY = resourceBundle.getString("edrm.property");
	public static final String ACTION = resourceBundle.getString("edrm.service.action");
	public static final String PROPERTY_DETAILS = resourceBundle.getString("edrm.property.details");
	public static final String PROPERTY_DATA_TYPE = resourceBundle.getString("edrm.property.dataType");
	public static final String PROPERTY_CARDINALITY = resourceBundle.getString("edrm.property.cardinality");
	public static final String PROPERTY_MAX_LENGTH = resourceBundle.getString("erdm.property.max.length");
	public static final String PROPERTY_IS_REQUIRED = resourceBundle.getString("erdm.property.is.required");
	public static final String PROPERTY_ID = resourceBundle.getString("erdm.property.id");
	public static final String PROPERTY_NAME=resourceBundle.getString("edrm.property.name");
	public static final String PROPERTY_VALUE=resourceBundle.getString("edrm.property.value");
	public static final String PROPERTY_SYMBOLIC_NAME=resourceBundle.getString("edrm.property.symbolic.name");
	public static final String SUCCESS_RESPONSE_CODE_VALUE= resourceBundle.getString("edrm.success.responsecode.value");
	public static final String FAILURE_RESPONSE_CODE_VALUE= resourceBundle.getString("edrm.failure.responsecode.value");
	public static final String FAILURE_EXCEPTION_CODE_VALUE= resourceBundle.getString("edrm.failure.exceptioncode.value");
	public static final String RESPONSE_MESSAGE_VALUE= resourceBundle.getString("edrm.response.message.value");
	public static final String EDRM_LIST_DETAILS= resourceBundle.getString("edrm.list.details");
	public static final String EDRM_NAME= resourceBundle.getString("edrm.name");
	public static final String EDRM_LISTS_NAME= resourceBundle.getString("edrm.lists.name");
	public static final String EDRM_LIST_VALUES= resourceBundle.getString("edrm.list.values");
	public static final String EDRM_LIST_TYPE= resourceBundle.getString("edrm.list.type");
	public static final String EDRM_SEARCH_CHOICELIST= resourceBundle.getString("edrm.search.choicelist");
	public static final String EDRM_SEARCH_CHOICELIST_NAME= resourceBundle.getString("edrm.search.choicelist.name");
	public static final String EDRM_DOCUMENT_VSID= resourceBundle.getString("edrm.document.vsid");
	public static final String EDRM_RECORD_ID= resourceBundle.getString("edrm.record.id");
	public static final String EDRM_CREATEPROPERTY_SUCCESS_MESG= resourceBundle.getString("edrm.create.property.success");
	public static final String EDRM_CREATEPROPERTY_FAILURE_MESG= resourceBundle.getString("edrm.create.property.failure");
	public static final String EDRM_DELETEPROPERTY_SUCCESS_MESG= resourceBundle.getString("edrm.delete.property.success");
	public static final String EDRM_DELETEPROPERTY_FAILURE_MESG= resourceBundle.getString("edrm.delete.property.failure");
	public static final String EDRM_CREATECHOICELIST_SUCCESS_MESG= resourceBundle.getString("edrm.create.choicelist.success");
	public static final String EDRM_CREATECHOICELIST_FAILURE_MESG= resourceBundle.getString("edrm.create.choicelist.failure");
	public static final String EDRM_UPDATECHOICELIST_SUCCESS_MESG= resourceBundle.getString("edrm.update.choicelist.success");
	public static final String EDRM_UPDATECHOICELIST_FAILURE_MESG= resourceBundle.getString("edrm.update.choicelist.failure");
	public static final String EDRM_DELETECHOICELIST_SUCCESS_MESG= resourceBundle.getString("edrm.delete.choicelist.success");
	public static final String EDRM_DELETECHOICELIST_FAILURE_MESG= resourceBundle.getString("edrm.delete.choicelist.failure");
	public static final String EDRM_CHOICELIST_CREATE= resourceBundle.getString("edrm.choice.list.create");
	public static final String EDRM_CHOICELIST_UPDATE= resourceBundle.getString("edrm.choice.list.update");
	public static final String EDRM_TASK= resourceBundle.getString("edrm.task");
	public static final String EDRM_TASK_NAME= resourceBundle.getString("edrm.task.name");
	public static final String EDRM_TASK_DETAILS= resourceBundle.getString("edrm.task.details");
	public static final String CUSTOMOBJECT_UPDATED_SUCCESS = resourceBundle.getString("edrm.customObject.updated");
	public static final String CUSTOMOBJECT_COMMIT_SUCCESS = resourceBundle.getString("edrm.customObject.commit.success");
	
	//Reverse sync properties
	public static final String LAST_SYNC_DATE = resourceBundle.getString("edrm.lastsync.date");
	public static final String DISPLAY_NAME = resourceBundle.getString("edrm.display.name");
	public static final String CHOICE_LIST = resourceBundle.getString("edrm.choice.list");
	
}
