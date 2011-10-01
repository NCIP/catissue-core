function openLinkUserWindow(operation, pageOf)
{
	document.getElementById("errorRow").textContent = '';
	var pageOperation = "SearchRemote";
	var emailAddress = document.getElementById('emailAddress').value;
	var lastName = document.getElementById('lastName').value;
	var firstName = document.getElementById('firstName').value
	var inputParams = "&emailAddress="+emailAddress+"&lastName="+lastName+"&firstName="+firstName;
	var ctrpUserAjaxUrl= 'CTRPUserAjax.do?pageOperation='+pageOperation+inputParams;
	var ds = new Ext.data.Store(
			{proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
			reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
					[{name: 'firstName', mapping: 'firstName'},
					{name: 'lastName', mapping: 'lastName'},
					{name: 'emailAddress', mapping: 'emailAddress'},
					{name: 'street', mapping: 'street'},
					{name: 'city', mapping: 'city'},
					{name: 'state', mapping: 'state'},
					{name: 'country', mapping: 'country'},
					{name: 'zipCode', mapping: 'zipCode'},
					{name: 'phoneNumber', mapping: 'phoneNumber'},
					{name: 'faxNumber', mapping: 'faxNumber'}]
					)
			}
	);
	ds.on('beforeload', function() {
		document.getElementById("storeLoadingDiv").style.display="";
		document.getElementById("findUserDiv").style.display="none";
	});
	ds.on('load', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		if (ds.getTotalCount()>0) {
			//Remote matches found
		var ctrpUserURL = 'CTRPUser.do?operation='+operation+'&pageof='+pageOf+'&'+inputParams;
		userwindow=dhtmlmodal.open('CTRPSelectUser', 'iframe', ctrpUserURL,'Select NCI Enterprise User', 'width=600px,height=300px,max-height=200px,center=1,resize=0,scrolling=1')
		userwindow.onclose=function()
	    {
			document.getElementById("findUserDiv").style.display="";
			return true;
	    }
		}else{
			//No remote matches found
			 document.getElementById("errorRow").textContent = ctrpSearchNoMatchMessage;
			 document.getElementById("errorRow").className = "messagetexterror";
			 document.getElementById("emailAddress").focus();
			document.getElementById("storeLoadingDiv").style.display="none";
			document.getElementById("findUserDiv").style.display="";
		}
	});
	ds.on('loadexception', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		document.getElementById("findUserDiv").style.display="";
	});
	ds.load();
}

function getRemoteUserDetails(remoteId,operation)
{
	var inputParams = "&remoteId="+remoteId;
	var pageOperation = "GetRemoteById";
	var ctrpUserAjaxUrl= 'CTRPUserAjax.do?pageOperation='+pageOperation+inputParams;
	var ds = new Ext.data.Store(
			{proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
			reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
					[{name: 'firstName', mapping: 'firstName'},
					{name: 'lastName', mapping: 'lastName'},
					{name: 'emailAddress', mapping: 'emailAddress'},
					{name: 'street', mapping: 'street'},
					{name: 'city', mapping: 'city'},
					{name: 'state', mapping: 'state'},
					{name: 'country', mapping: 'country'},
					{name: 'zipCode', mapping: 'zipCode'},
					{name: 'phoneNumber', mapping: 'phoneNumber'},
					{name: 'faxNumber', mapping: 'faxNumber'}]
					)
			}
	);
	ds.on('beforeload', function() {
		document.getElementById("storeLoadingDiv").style.display="";
	});
	ds.on('load', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		var remoteRecord =  ds.getAt(0);
		//Pre-populate fields from CTRP 
		document.getElementById('emailAddress').value = remoteRecord.get('emailAddress');
		document.getElementById('confirmEmailAddress').value = remoteRecord.get('emailAddress');
		document.getElementById('firstName').value = remoteRecord.get('firstName');
		document.getElementById('lastName').value = remoteRecord.get('lastName');
		document.getElementById('street').value = remoteRecord.get('street');
		document.getElementById('city').value = remoteRecord.get('city');
		document.getElementById('phoneNumber').value = remoteRecord.get('phoneNumber');
		document.getElementById('faxNumber').value = remoteRecord.get('faxNumber');
		document.getElementById('zipCode').value = remoteRecord.get('zipCode');
		document.getElementById('state').value = remoteRecord.get('state');
		document.getElementById('country').value = remoteRecord.get('country');
		//Hide Find Enterprise User Link
//		document.getElementById('lookupNCI').style.display = "none";
		if (operation == 'Link') { 
			document.getElementById('remoteId').value = remoteRecord.id;
			document.getElementById('remoteManagedFlag').value = 'true';
			document.getElementById('dirtyEditFlag').value = 'false';
			disableRemoteManagedFields();
			updateRemoteInstitutions(remoteRecord.id);
		}else {
			document.getElementById("findUserDiv").style.display="";
		}
	});
	ds.on('loadexception', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
	});
	ds.load();
}
function disableRemoteManagedFields() {
	//Disable Remote managed fields
	document.getElementById('emailAddress').readOnly = true;
	document.getElementById('confirmEmailAddress').readOnly = true;
	document.getElementById('firstName').readOnly = true;
	document.getElementById('lastName').readOnly = true;
	document.getElementById('street').readOnly = true;
	document.getElementById('city').readOnly = true;
	document.getElementById('phoneNumber').readOnly = true;
	document.getElementById('faxNumber').readOnly = true;
	document.getElementById('zipCode').readOnly = true;
	document.getElementById('state').readOnly = true;
	document.getElementById('country').readOnly = true;
	//Change the CSS class to gray out
	document.getElementById('emailAddress').className = "black_ar_disabled";
	document.getElementById('confirmEmailAddress').className = "black_ar_disabled";
	document.getElementById('firstName').className = "black_ar_disabled";
	document.getElementById('lastName').className = "black_ar_disabled";
	document.getElementById('street').className = "black_ar_disabled";
	document.getElementById('city').className = "black_ar_disabled";
	document.getElementById('phoneNumber').className = "black_ar_disabled";
	document.getElementById('faxNumber').className = "black_ar_disabled";
	document.getElementById('zipCode').className = "black_ar_disabled";
	document.getElementById('state').className = "black_ar_disabled";
	document.getElementById('country').className = "black_ar_disabled";
}

function enableRemoteManagedFields() {
	//Disable Remote managed fields
	document.getElementById('emailAddress').readOnly = false;
	document.getElementById('confirmEmailAddress').readOnly = false;
	document.getElementById('firstName').readOnly = false;
	document.getElementById('lastName').readOnly = false;
	document.getElementById('street').readOnly = false;
	document.getElementById('city').readOnly = false;
	document.getElementById('phoneNumber').readOnly = false;
	document.getElementById('faxNumber').readOnly = false;
	document.getElementById('zipCode').readOnly = false;
	document.getElementById('state').readOnly = false;
	document.getElementById('country').readOnly = false;
	//Change the CSS class to gray out
	document.getElementById('emailAddress').className = "black_ar";
	document.getElementById('confirmEmailAddress').className = "black_ar";
	document.getElementById('firstName').className = "black_ar";
	document.getElementById('lastName').className = "black_ar";
	document.getElementById('street').className = "black_ar";
	document.getElementById('city').className = "black_ar";
	document.getElementById('phoneNumber').className = "black_ar";
	document.getElementById('faxNumber').className = "black_ar";
	document.getElementById('zipCode').className = "black_ar";
	document.getElementById('state').className = "black_ar";
	document.getElementById('country').className = "black_ar";
}

function markupRemoteFields() {
	// Remote Managed 
	if (("true" == document.getElementById("remoteManagedFlag").value) 
			&& (("edit" == document.getElementById("operation").value))
			&& ("false" == document.getElementById("dirtyEditFlag").value)){
		disableRemoteManagedFields();
		document.getElementById("findUserDiv").style.display = "none";
		document.getElementById("editLocalDiv").style.display = "";
		document.getElementById("syncRemoteDiv").style.display = "none";
	}
	//Remote Managed and Dirty Edit
	if (("true" == document.getElementById("remoteManagedFlag").value) 
			&& (("edit" == document.getElementById("operation").value))
			&& ("true" == document.getElementById("dirtyEditFlag").value)){
		document.getElementById("findUserDiv").style.display = "none";
		document.getElementById("editLocalDiv").style.display = "none";
		document.getElementById("syncRemoteDiv").style.display = "";
	}
}
function enableLocalChangesFunc(){
	var prompt = confirm(ctrpDirtyEditConfMessage);
	if (prompt == false){
		return;
	}
	enableRemoteManagedFields();
	document.getElementById("dirtyEditFlag").value = true;
}

function syncRemoteChangesFunc() {
	var prompt = confirm(ctrpSyncConfMessage);
	if (prompt == false){
		return;
	}
	var inputParams = "&remoteId="+document.getElementById("remoteId").value;
	var pageOperation = "GetRemoteById";
	var ctrpUserAjaxUrl= 'CTRPUserAjax.do?pageOperation='+pageOperation+inputParams;
	var ds = new Ext.data.Store(
			{proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
			reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
					[{name: 'firstName', mapping: 'firstName'},
					{name: 'lastName', mapping: 'lastName'},
					{name: 'emailAddress', mapping: 'emailAddress'},
					{name: 'street', mapping: 'street'},
					{name: 'city', mapping: 'city'},
					{name: 'state', mapping: 'state'},
					{name: 'country', mapping: 'country'},
					{name: 'zipCode', mapping: 'zipCode'},
					{name: 'phoneNumber', mapping: 'phoneNumber'},
					{name: 'faxNumber', mapping: 'faxNumber'}]
					)
			}
	);
	ds.on('beforeload', function() {
		document.getElementById("storeLoadingDiv").style.display="";
		document.getElementById("syncRemoteDiv").style.display="none";
	});
	ds.on('load', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		var remoteRecord =  ds.getAt(0);
		document.getElementById('remoteId').value = remoteRecord.id;
		document.getElementById('remoteManagedFlag').value = 'true';
		document.getElementById('dirtyEditFlag').value = 'false';
		//Pre-populate fields from CTRP 
		document.getElementById('emailAddress').value = remoteRecord.get('emailAddress');
		document.getElementById('confirmEmailAddress').value = remoteRecord.get('emailAddress');
		document.getElementById('firstName').value = remoteRecord.get('firstName');
		document.getElementById('lastName').value = remoteRecord.get('lastName');
		document.getElementById('street').value = remoteRecord.get('street');
		document.getElementById('city').value = remoteRecord.get('city');
		document.getElementById('phoneNumber').value = remoteRecord.get('phoneNumber');
		document.getElementById('faxNumber').value = remoteRecord.get('faxNumber');
		document.getElementById('zipCode').value = remoteRecord.get('zipCode');
		document.getElementById('state').value = remoteRecord.get('state');
		document.getElementById('country').value = remoteRecord.get('country');
		disableRemoteManagedFields();
		//Hide Find Enterprise User Link
		document.getElementById('lookupNCI').style.display = "none";
		document.getElementById("dirtyEditFlag").value = false;
		var form = document.forms[0];
		form.submit();
	});
	ds.on('loadexception', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		document.getElementById("syncRemoteDiv").style.display="";
	});
	ds.load();
}

function removeRemoteLinkFunc(){
	var prompt = confirm(ctrpUnlinkConfMessage);
	if (prompt == false){
		return;
	}
	document.getElementById("dirtyEditFlag").value = 'false';
	document.getElementById("remoteManagedFlag").value = 'false';
	document.getElementById("remoteId").value = '';
	var form = document.forms[0];
	form.submit();
}

function updateRemoteInstitutions(remoteId){
	var box = Ext.getCmp('ins_user_cb');
	box.store.reload({params:{remoteId:remoteId}});
}

function populateInstitutionExtCombobox(){
   Ext.QuickTips.init();
   
	var pageOperation = "GetInstitutions";
	var ctrpUserAjaxUrl=  'CTRPUserAjax.do?pageOperation='+pageOperation;
	var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
	        reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'
	        }, [{name: 'excerpt', mapping: 'name'},
	            {name: 'remoteId', mapping: 'remoteInstId'},
	            {name: 'localId', mapping: 'localInstId'},
	            {name: 'remoteFlag', mapping: 'remoteFlag'}])
	    });
	var initValue="";
	var remoteId = 0;
	var userId = 0;
	
//	ds.on('beforeload', function() {
////		document.getElementById("storeLoadingDiv").style.display="";
//
//		
//	});
	ds.on('load', function() {
//		document.getElementById("storeLoadingDiv").style.display="none";
		if ((ds.getCount()>0) && (userId !=0)){
			// Pre select the value only for edit functionality and if any institutions exist
			var insRecord = ds.getAt(0);
			initValue = insRecord.get('excerpt');
			document.getElementById("institutionId").value = insRecord.get('localId');
			document.getElementById("displayinstitutionId").innerText = insRecord.get('excerpt');
			document.getElementById("remoteInstitutionId").value = insRecord.get('remoteId');
		}
		 Ext.getCmp('ins_user_cb').setValue(initValue);
	});

	if (document.getElementById('remoteId') != null){
		remoteId = document.getElementById('remoteId').value;
	}
	if (document.getElementById('id') != null){
		userId = document.getElementById('id').value;
	}
//	updateInstitutions(userId,remoteId);
	ds.load({params:{'userId':userId,'remoteId':remoteId}});
	
	
	var cbtpl = new Ext.XTemplate(
			'<tpl for=".">',
				'<tpl if="remoteFlag==\'Y\'">',
					'<div ext:qtip="{excerpt}" class="x-combo-list-item"><img hspace="0" height="15" width="15" vspace="0" title="NCI enterprise entity managed locally" src="images/uIEnhancementImages/nci_remote.png">{excerpt}</div>',
				'</tpl>',
				'<tpl if="remoteFlag==\'N\'">',
					'<div ext:qtip="{excerpt}" class="x-combo-list-item">{excerpt}</div>',
				'</tpl>',
			'</tpl>'
			);
	var combo = new Ext.form.ComboBox({
	    store: ds,displayField:'excerpt',typeAhead: false, pageSize:0,forceSelection: true ,
		queryParam : 'query',mode: 'local',triggerAction: 'all',minChars : 1,queryDelay : 300,
		typeAheadDelay : 400,selectOnFocus:true,
		tpl: cbtpl,
	    applyTo: 'displayinstitutionId', valueField :'excerpt',
	    id:'ins_user_cb', 
	    triggerClass:'x-form-trigger-auto-complete',fieldClass:'x-form-field-auto-complete',
	    value:initValue
	   });
	combo.on("expand", function(){if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "270");combo.innerList.setStyle("width", "270");}else{combo.list.setStyle("width", "260");combo.innerList.setStyle("width", "260");}}, {single: true});
	combo.on("select", function(box,record,index){
		
		document.getElementById("institutionId").value = record.get('localId');
		document.getElementById("displayinstitutionId").innerText = record.get('excerpt');
		document.getElementById("remoteInstitutionId").value = record.get('remoteId');
	});
}
